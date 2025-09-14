package managers;

import collection.*;
import utility.Console;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс для работы с XML-файлом, содержащим коллекцию городов
 */
public class DumpManager {
    private final String fileName;
    private final Console console;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;

    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    /**
     * Загружает коллекцию городов из XML-файла
     * @return загруженная коллекция городов
     */
    public LinkedHashSet<City> loadFromFile() {
        LinkedHashSet<City> cities = new LinkedHashSet<>();
        File file = new File(fileName);

        if (!file.exists()) {
            console.print("Файл " + fileName + " не существует, файл будет создан при сохранении\n");
            return cities;
        }

        if (!file.canRead()) {
            console.printError("Нет прав на чтение файла: " + fileName);
            return cities;
        }

        try (Scanner scanner = new Scanner(file)) {
            StringBuilder xmlContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                xmlContent.append(scanner.nextLine().trim());
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlContent.toString().getBytes()));

            NodeList cityNodes = document.getDocumentElement().getElementsByTagName("city");
            for (int i = 0; i < cityNodes.getLength(); i++) {
                City city = parseCityElement((Element) cityNodes.item(i));
                if (city != null && city.validate()) {
                    cities.add(city);
                }
            }

            console.println("Городов загружно из файла " + fileName + " - " + cities.size());
        } catch (Exception e) {
            console.printError("Ошибка при загрузке файла: " + e.getMessage());
        }

        return cities;
    }

    /**
     * Сохраняет коллекцию городов в XML-файл
     * @param cities коллекция городов для сохранения
     */
    public void saveToFile(Collection<City> cities) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName))) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("cities");
            document.appendChild(rootElement);

            for (City city : cities) {
                rootElement.appendChild(createCityElement(document, city));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(document), new StreamResult(byteOutput));
            outputStream.write(byteOutput.toByteArray());

            console.println("Сохранено " + cities.size() + " городов в файл " + fileName);
        } catch (Exception e) {
            console.printError("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    /**
     * Создает элемент <city> для XML-документа
     * @param document XML-документ
     * @param city объект City
     * @return элемент <city>
     */
    private Element createCityElement(Document document, City city) {
        Element cityElement = document.createElement("city");

        cityElement.appendChild(createElement(document, "id", city.getId().toString()));
        cityElement.appendChild(createElement(document, "name", city.getName()));
        cityElement.appendChild(createCoordinatesElement(document, city.getCoordinates()));
        cityElement.appendChild(createElement(document, "creationDate", city.getCreationDate().format(dateTimeFormatter)));
        cityElement.appendChild(createElement(document, "area", city.getArea().toString()));
        cityElement.appendChild(createElement(document, "population", String.valueOf(city.getPopulation())));
        cityElement.appendChild(createElement(document, "metersAboveSeaLevel", city.getMetersAboveSeaLevel() != null ? city.getMetersAboveSeaLevel().toString() : ""));
        cityElement.appendChild(createElement(document, "telephoneCode", city.getTelephoneCode() != null ? city.getTelephoneCode().toString() : ""));
        cityElement.appendChild(createElement(document, "agglomeration", String.valueOf(city.getAgglomeration())));
        cityElement.appendChild(createElement(document, "government", city.getGovernment().name()));
        cityElement.appendChild(createHumanElement(document, city.getGovernor()));

        return cityElement;
    }

    /**
     * Создает элемент <coordinates> для XML-документа
     * @param document XML-документ
     * @param coordinates объект Coordinates
     * @return элемент <coordinates>
     */
    private Element createCoordinatesElement(Document document, Coordinates coordinates) {
        Element coordinatesElement = document.createElement("coordinates");
        coordinatesElement.appendChild(createElement(document, "x", coordinates.getX().toString()));
        coordinatesElement.appendChild(createElement(document, "y", String.valueOf(coordinates.getY())));
        return coordinatesElement;
    }

    /**
     * Создает элемент <governor> для XML-документа
     * @param document XML-документ
     * @param governor объект Human
     * @return элемент <governor>
     */
    private Element createHumanElement(Document document, Human governor) {
        Element humanElement = document.createElement("governor");
        if (governor != null && governor.getBirthday() != null) {
            humanElement.appendChild(createElement(document, "birthday", governor.getBirthday().format(dateFormatter)));
        }
        return humanElement;
    }

    /**
     * Создает текстовый элемент для XML-документа
     * @param document XML-документ
     * @param name имя элемента
     * @param value значение элемента
     * @return текстовый элемент
     */
    private Element createElement(Document document, String name, String value) {
        Element element = document.createElement(name);
        if (value != null) {
            element.appendChild(document.createTextNode(value));
        }
        return element;
    }

    /**
     * Парсит элемент <city> из XML
     * @param cityElement элемент XML
     * @return объект City
     */
    private City parseCityElement(Element cityElement) {
        try {
            Integer id = Integer.parseInt(getElementText(cityElement, "id"));
            String name = getElementText(cityElement, "name");
            Coordinates coordinates = parseCoordinates(cityElement);
            LocalDateTime creationDate = LocalDateTime.parse(getElementText(cityElement, "creationDate"), dateTimeFormatter);
            Long area = Long.parseLong(getElementText(cityElement, "area"));
            int population = Integer.parseInt(getElementText(cityElement, "population"));
            Integer metersAboveSeaLevel = parseOptionalInteger(getElementText(cityElement, "metersAboveSeaLevel"));
            Long telephoneCode = parseOptionalLong(getElementText(cityElement, "telephoneCode"));
            long agglomeration = Long.parseLong(getElementText(cityElement, "agglomeration"));
            Government government = Government.valueOf(getElementText(cityElement, "government"));
            Human governor = parseGovernor(cityElement);

            return new City(id, name, coordinates, creationDate, area, population,
                    metersAboveSeaLevel, telephoneCode, agglomeration, government, governor);
        } catch (Exception e) {
            console.printError("Ошибка парсинга элемента city: " + e.getMessage());
            return null;
        }
    }

    /**
     * Парсит координаты из XML
     */
    private Coordinates parseCoordinates(Element cityElement) {
        Element coordElement = (Element) cityElement.getElementsByTagName("coordinates").item(0);
        Float x = Float.parseFloat(getElementText(coordElement, "x"));
        float y = Float.parseFloat(getElementText(coordElement, "y"));
        return new Coordinates(x, y);
    }

    /**
     * Парсит данные губернатора из XML
     */
    private Human parseGovernor(Element cityElement) {
        NodeList governorNodes = cityElement.getElementsByTagName("governor");
        if (governorNodes.getLength() == 0) return null;

        Element governorElement = (Element) governorNodes.item(0);
        String birthdayStr = getElementText(governorElement, "birthday");
        if (birthdayStr == null || birthdayStr.isEmpty()) return null;

        return new Human(LocalDate.parse(birthdayStr, dateFormatter));
    }

    /**
     * Получает текст элемента
     */
    private String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return null;
        return nodes.item(0).getTextContent();
    }

    /**
     * Парсит необязательное целое число
     */
    private Integer parseOptionalInteger(String value) {
        return (value == null || value.isEmpty()) ? null : Integer.parseInt(value);
    }

    /**
     * Парсит необязательное длинное число
     */
    private Long parseOptionalLong(String value) {
        return (value == null || value.isEmpty()) ? null : Long.parseLong(value);
    }
}