package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;
import collection.City;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Команда 'group_counting_by_population'. Группирует элементы коллекции по значению поля population и выводит количество элементов в каждой группе.
 */
public class GroupCounting extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды
     * @param console - консоль для ввода/вывода
     * @param collectionManager - объект collectionManager для работы с коллекцией
     */

    public GroupCounting(Console console, CollectionManager collectionManager) {
        super("group_counting_by_population", "группировать элементы коллекции по значению поля population и вывести количество элементов в каждой группе");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        try {
            // Проверка на наличие аргументов
            if (!arguments[1].isEmpty())
                return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");

            // Группируем элементы по population и считаем количество элементов в каждой группе
            Map<Integer, Long> populationGroups = collectionManager.getCollection().stream()
                    .collect(Collectors.groupingBy(
                            City::getPopulation, // Ключ группировки — значение population
                            Collectors.counting() // Подсчет количества элементов в каждой группе
                    ));

            // Формируем строку для вывода
            if (populationGroups.isEmpty()) {
                return new ExecutionResponse(true, "Коллекция пуста.");
            } else {
                String result = populationGroups.entrySet().stream()
                        .map(entry -> "Population: " + entry.getKey() + " -> Количество городов: " + entry.getValue())
                        .collect(Collectors.joining("\n"));
                return new ExecutionResponse(true, result);
            }
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}