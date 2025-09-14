package commands;

import collection.Ask;
import managers.CollectionManager;
import utility.Console;
import utility.ExecutionResponse;
import collection.City;

/**
 * Команда 'remove_greater {element}'. Удаляет из коллекции все элементы, превышающие заданный.
 */
public class RemoveGreater extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды
     * @param console - консоль для ввода/вывода
     * @param collectionManager - объект collectionManager для работы с коллекцией
     */

    public RemoveGreater(Console console, CollectionManager collectionManager) {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        try {
            // Проверка на наличие аргументов
            if (!arguments[1].isEmpty())
                return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");


            // Запрашиваем данные для города, с которым будем сравнивать
            console.println("Введите данные для города, с которым будем сравнивать:");
            City comparisonCity = Ask.askCity(console, collectionManager.getFreeId());

            // Удаляем все элементы, превышающие comparisonCity
            int initialSize = collectionManager.getCollection().size();
            collectionManager.getCollection().removeIf(city -> city.compareTo(comparisonCity) > 0);
            int removedCount = initialSize - collectionManager.getCollection().size();

            return new ExecutionResponse(true, "Удалено элементов: " + removedCount);
        } catch (Ask.AskBreak e) {
            return new ExecutionResponse(false, "Отмена операции.");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}