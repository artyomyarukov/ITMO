package collection;

import utility.Console;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

/**
 * Класс, запрашивающий у пользователя данные для создания объектов.
 */

public class Ask {
    /**
     * Исключение выбрасывающееся для прерывания ввода
     */

    public static class AskBreak extends Exception {
    }

    /**
     * Запрашивает у пользователя данные для создания объекта City
     * @param console Консоль для ввода/вывода
     * @param id Интдефекатор
     * @return объект City с введенными данными
     * @throws AskBreak Исключение, выбрасываемое при некорректном вводе
     */

    public static City askCity(Console console, Integer id) throws AskBreak {

        try {
            console.print("name: ");
            String name;
            while (true) {
                name = console.readln().trim();
                if (name.equals("exit")) throw new AskBreak();
                if (!name.equals("")) break;
                console.print("name: ");
            }
            var coordinates = askCoordinates(console);
            var area = askArea(console);
            var population = askPopulation(console);
            var metersAboveSeaLevel = askMetersAboveSeaLevel(console);
            var telephoneCode = askTelephoneCode(console);
            var agglomeration = askAgglomeretion(console);
            var government = askGovernment(console);
            var governor = askGovernor(console);

            return new City(id, name, coordinates, area, population, metersAboveSeaLevel, telephoneCode, agglomeration, government, governor);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }
    /**
     * Запрашивает у пользователя данные для создания объекта Human
     * @param console Консоль для ввода/вывода
     * @return объект City с введенными данными
     * @throws AskBreak Исключение, выбрасываемое при некорректном вводе
     */


    public static Human askGovernor(Console console) throws AskBreak {
        try {
            console.print("governor birthday: ");
            LocalDate birthday;
            while (true) {
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (line.equals("")) {
                    birthday = null;
                    break;
                }
                try {
                    birthday = LocalDate.from(LocalDateTime.parse(line, DateTimeFormatter.ISO_DATE_TIME));
                    break;
                } catch (
                        DateTimeParseException e) {
                }
                try {
                    birthday = LocalDate.from(LocalDate.parse(line + "T00:00:00.0000", DateTimeFormatter.ISO_DATE_TIME).atStartOfDay());
                    break;
                } catch (DateTimeParseException e) {
                }
                console.print("governor.birthday: ");
            }
            return new Human(birthday);


        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }


    }

    /**
     * Запрашивает у пользователя данные для создания объекта Government
     * @param console Консоль для ввода/вывода
     * @return объект City с введенными данными
     * @throws AskBreak Исключение, выбрасываемое при некорректном вводе
     */


    public static Government askGovernment(Console console) throws AskBreak {
        try {
            console.print("Government (" + Government.names() + "): ");
            Government government;
            while (true) {
                var line = console.readln().trim();
                if (line.equals("")) {
                    return null;
                }

                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        government = Government.valueOf(line);
                        break;
                    } catch (NullPointerException | IllegalArgumentException e) {
                    }
                }
                console.print("Значение должно быть из списка повторите ввод\nGovernment (" + Government.names() + "): ");
            }
            return government;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }


    }

    /**
     * Запрашивает у пользователя данные для ввода agglomeration
     * @param console Консоль для ввода/вывода
     * @return объект City с введенными данными
     * @throws AskBreak Исключение, выбрасываемое при некорректном вводе
     */

    public static long askAgglomeretion(Console console) throws AskBreak {
        try {
            console.print("agglomeration: ");
            long agglomeretion;
            while (true) {
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        agglomeretion = Long.parseLong(line);
                        if (agglomeretion > 0) break;

                    } catch (NumberFormatException e) {

                    }
                }
                console.print("Значение должно быть целочисленным и больше 0\nповторите ввод agglomeration: ");

            }
            return agglomeretion;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return 0;
        }
    }

    /**
     * Запрашивает у пользователя данные для ввода telephoneCod
     * @param console Консоль для ввода/вывода
     * @return объект City с введенными данными
     * @throws AskBreak Исключение, выбрасываемое при некорректном вводе
     */

    public static Long askTelephoneCode(Console console) throws AskBreak {
        try {
            console.print("telephone code: ");
            Long telephoneCode;
            while (true) {
                var line = console.readln().trim();
                if (line.isEmpty()) {
                    return null;
                }
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        telephoneCode = Long.parseLong(line);
                        if (telephoneCode > 0 && telephoneCode < 100000) break;
                    } catch (NumberFormatException e) {

                    }
                }
                console.print("Ошибка значение должно быть либо целочисленным больше 0 и меньше 100000, либо пустым\nповторите ввод telephone code: ");
            }
            return telephoneCode;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    /**
     * Запрашивает у пользователя данные для ввода metersAboveSeaLevel
     * @param console Консоль для ввода/вывода
     * @return объект City с введенными данными
     * @throws AskBreak Исключение, выбрасываемое при некорректном вводе
     */

    public static Integer askMetersAboveSeaLevel(Console console) throws AskBreak {
        try {
            console.print("meters above sea level: ");
            Integer metersAboveSeaLevel;
            while (true) {
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        metersAboveSeaLevel = Integer.parseInt(line);
                        if (metersAboveSeaLevel > 0) break;
                    } catch (NumberFormatException e) {

                    }
                }
                console.print("Ошибка значение должно быть целочисленным и больше 0\nповторите ввод meters above sea level: ");
            }
            return metersAboveSeaLevel;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    /**
     * Запрашивает у пользователя данные для ввода area
     * @param console Консоль для ввода/вывода
     * @return объект City с введенными данными
     * @throws AskBreak Исключение, выбрасываемое при некорректном вводе
     */


    public static Long askArea(Console console) throws AskBreak {
        try {
            console.print("area: ");
            Long area;
            while (true) {
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {

                    try {
                        area = Long.parseLong(line);
                        if (area > 0) break;
                    } catch (NumberFormatException e) {

                    }
                }
                console.print("Ошибка значение должно быть целочисленным и больше 0\nповторите ввод area: ");
            }
            return area;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }

    }

    /**
     * Запрашивает у пользователя данные для ввода population
     * @param console Консоль для ввода/вывода
     * @return объект City с введенными данными
     * @throws AskBreak Исключение, выбрасываемое при некорректном вводе
     */

    public static int askPopulation(Console console) throws AskBreak {
        try {

            console.print("population: ");
            int population;
            while (true) {
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {

                    try {
                        population = Integer.parseInt(line);
                        if (population > 0) break;
                    } catch (NumberFormatException e) {

                    }
                }
                console.print("Ошибка значение должно быть целочисленным и больше 0\nповторите ввод population: ");
            }
            return population;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return 0;
        }
    }

    /**
     * Запрашивает у пользователя данные для запроса объекта Coordinates
     * @param console Консоль для ввода/вывода
     * @return объект City с введенными данными
     * @throws AskBreak Исключение, выбрасываемое при некорректном вводе
     */

    public static Coordinates askCoordinates(Console console) throws AskBreak {
        try {
            // Float x; //Поле не может быть null
            // Float y; //Максимальное значение поля: 986
            console.print("coordinates.x: ");
            Float x;
            while (true) {
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        x = Float.parseFloat(line);
                        if (x != null) break;
                    } catch (NumberFormatException e) {
                    }
                }
                console.print("Ошибка значение должно быть числом\nповторите ввод coordinates.x: ");
            }
            console.print("coordinates.y: ");
            float y;
            while (true) {
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {
                        y = Float.parseFloat(line);
                        if (y <= 986) break;
                    } catch (NumberFormatException e) {
                    }
                }
                console.print("Ошибка значение должно быть число меньшее или равное 986\nповторите ввод coordinates.y: ");
            }

            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }


}
