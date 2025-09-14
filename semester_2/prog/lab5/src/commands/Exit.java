package commands;

import utility.Console;
import utility.ExecutionResponse;

/**
 * Команда 'exit'. Завершает выполнение.
 *
 * @author artyom_yarukov
 */
public class Exit extends Command {
    private final Console console;

    /**
     * Конструктор команды
     * @param console - консоль для ввода/вывода
     */

    public Exit(Console console) {
        super("exit", "завершить программу (без сохранения в файл)");
        this.console = console;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse apply(String[] arguments) {
        if (!arguments[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");

        return new ExecutionResponse("exit"); //"Завершение выполнения...");
    }
}