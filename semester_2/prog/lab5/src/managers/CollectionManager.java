package managers;

import collection.City;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс для работы с коллекцией
 */

public class CollectionManager {
    private int currentId = 0; // Счётчик для генерации уникальных ID
    private LinkedHashSet<City> collection = new LinkedHashSet<>(); // Основная коллекция
    private LocalDateTime lastInitTime; // Время последней инициализации
    private LocalDateTime lastSaveTime; // Время последнего сохранения
    private final DumpManager dumpManager; // Менеджер для работы с файлами
    private final String filename; // Имя файла для сохранения/загрузки

    public CollectionManager(DumpManager dumpManager, String filename) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;
        this.filename = filename; // Сохраняем имя файла
    }

    /**
     * @return Последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Последнее время сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return Коллекция городов.
     */
    public LinkedHashSet<City> getCollection() {
        return collection;
    }

    /**
     * Получить город по ID.
     *
     * @param id ID города.
     * @return Город или null, если город не найден.
     */
    public City byId(int id) {
        for (City city : collection) {
            if (city.getId() == id) {
                return city;
            }
        }
        return null;
    }

    /**
     * Проверяет, содержится ли город в коллекции.
     *
     * @param city Город для проверки.
     * @return true, если город содержится в коллекции, иначе false.
     */
    public boolean isContain(City city) {
        return city == null || byId(city.getId()) != null;
    }

    /**
     * Генерирует свободный ID для нового города.
     *
     * @return Свободный ID.
     */
    public int getFreeId() {
        while (byId(++currentId) != null) ; // Ищем первый свободный ID
        return currentId;
    }

    /**
     * Добавляет город в коллекцию.
     *
     * @param city Город для добавления.
     * @return true, если город успешно добавлен, иначе false.
     */
    public boolean add(City city) {
        if (isContain(city)) return false; // Если город уже есть, не добавляем
        collection.add(city);
        update();
        return true;
    }

    /**
     * Обновляет город в коллекции.
     *
     * @param city Город для обновления.
     * @return true, если город успешно обновлён, иначе false.
     */
    public boolean update(City city) {
        if (!isContain(city)) return false; // Если города нет, не обновляем
        collection.remove(byId(city.getId())); // Удаляем старую версию
        collection.add(city); // Добавляем новую версию
        update();
        return true;
    }

    /**
     * Удаляет город из коллекции по ID.
     *
     * @param id ID города.
     * @return true, если город успешно удалён, иначе false.
     */
    public boolean remove(int id) {
        City city = byId(id);
        if (city == null) return false; // Если города нет, не удаляем
        collection.remove(city);
        update();
        return true;
    }

    /**
     * Сортирует коллекцию и обновляет время изменения.
     */
    public void update() {
        // LinkedHashSet сохраняет порядок вставки, поэтому сортировка не требуется.
        // Если нужна сортировка, можно временно преобразовать в List, отсортировать и снова добавить в LinkedHashSet.
        List<City> sortedList = new ArrayList<>(collection);
        Collections.sort(sortedList);
        collection.clear();
        collection.addAll(sortedList);
    }

    /**
     * Инициализирует коллекцию из файла.
     * @return true, если инициализация прошла успешно, иначе false.
     */
    public boolean init() {

        collection.clear();
        try {
            collection = dumpManager.loadFromFile(); // Читаем коллекцию из файла
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке коллекции: " + e.getMessage());
            return false;
        }
        lastInitTime = LocalDateTime.now();

        // Проверяем на дубликаты и обновляем currentId
        for (City city : collection) {
            if (city.getId() > currentId) currentId = city.getId(); // Обновляем currentId
        }

        update(); // Сортируем коллекцию
        return true;
    }

    /**
     * Сохраняет коллекцию в файл.
     */
    public void saveCollection() {
        try {
            dumpManager.saveToFile(collection); // Сохраняем коллекцию в файл
            lastSaveTime = LocalDateTime.now();
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении коллекции: " + e.getMessage());
        }
    }

    /**
     * Строковое представление коллекции
     * @return строковое представление коллекции
     */

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (City city : collection) {
            info.append(city).append("\n\n");
        }
        return info.toString().trim();
    }
}





















