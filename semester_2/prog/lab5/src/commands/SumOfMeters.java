package commands;

import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;
import collection.City;

/**
 * Команда 'sum_of_meters_above_sea_level'. Выводит сумму значений поля metersAboveSeaLevel для всех элементов коллекции.
 */
public class SumOfMeters extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды
     * @param console - консоль для ввода/вывода
     * @param collectionManager - объект collectionManager для работы с коллекцией
     */

    public SumOfMeters(Console console, CollectionManager collectionManager) {
        super("sum_of_meters_above_sea_level", "вывести сумму значений поля metersAboveSeaLevel для всех элементов коллекции");
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

            // Вычисляем сумму значений metersAboveSeaLevel
            int sum = collectionManager.getCollection().stream()
                    .mapToInt(City::getMetersAboveSeaLevel)
                    .sum();

            return new ExecutionResponse(true, "Сумма значений metersAboveSeaLevel: " + sum);
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}