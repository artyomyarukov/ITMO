package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;
import collection.City;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'print_descending'. Выводит элементы коллекции в порядке убывания.
 */
public class PrintDescending extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды
     * @param console - консоль для ввода/вывода
     * @param collectionManager - объект collectionManager для работы с коллекцией
     */

    public PrintDescending(Console console, CollectionManager collectionManager) {
        super("print_descending", "вывести элементы коллекции в порядке убывания");
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

            // Получаем коллекцию и сортируем её в порядке убывания
            List<City> sortedCities = collectionManager.getCollection().stream()
                    .sorted(Comparator.reverseOrder()) // Сортировка в порядке убывания
                    .toList();

            // Формируем строку для вывода
            if (sortedCities.isEmpty()) {
                return new ExecutionResponse(true, "Коллекция пуста.");
            } else {
                String result = sortedCities.stream()
                        .map(City::toString)
                        .collect(Collectors.joining("\n"));
                return new ExecutionResponse(true, result);
            }
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}