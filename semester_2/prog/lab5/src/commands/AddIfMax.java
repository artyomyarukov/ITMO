package commands;

import collection.Ask;
import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;
import collection.City;

/**
 * Команда 'add_if_max'. Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.
 */
public class AddIfMax extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды
     * @param console - консоль для ввода/вывода
     * @param collectionManager - объект collectionManager для работы с коллекцией
     */

    public AddIfMax(Console console, CollectionManager collectionManager) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
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

            // Запрашиваем данные для нового города
            console.println("Введите данные для нового города:");
            City newCity = Ask.askCity(console, collectionManager.getFreeId());

            // Проверяем, что новый город больше максимального
            City maxCity = collectionManager.getCollection().stream()
                    .max(City::compareTo)
                    .orElse(null);

            if (maxCity == null || newCity.compareTo(maxCity) > 0) {
                collectionManager.add(newCity);
                return new ExecutionResponse(true, "Город успешно добавлен в коллекцию.");
            } else {
                return new ExecutionResponse(false, "Город не добавлен, так как его значение не превышает значение наибольшего элемента коллекции.");
            }
        } catch (Ask.AskBreak e) {
            return new ExecutionResponse(false, "Отмена операции.");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}