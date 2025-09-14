import collection.*;
import commands.Help;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;
import utility.Console;
import utility.Runner;
import utility.StandartConsole;
import commands.*;


/**
 * Главный класс программы
 * Вариант 6172
 */

public class Main {
    public static void main(String[] args) throws Ask.AskBreak {

        // Создаем консоль для ввода/вывода
        Console console = new StandartConsole();

        // Проверяем, передан ли аргумент с именем файла
        if (args.length == 0) {
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1); // Завершаем программу с кодом ошибки
        }

        // Создаем менеджер для работы с файлами
        DumpManager dumpManager = new DumpManager(args[0], console);

        // Создаем менеджер коллекции
        CollectionManager collectionManager = new CollectionManager(dumpManager, args[0]);

        // Загружаем коллекцию из файла
        if (!collectionManager.init()) {
            console.println("Ошибка при загрузке коллекции из файла.");
            System.exit(1); // Завершаем программу с кодом ошибки
        }

        // Создаем менеджер команд и регистрируем все команды
        CommandManager commandManager = new CommandManager() {{
            register("help", new Help(console, this));
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("add", new Add(console, collectionManager));
            register("update", new Update(console, collectionManager));
            register("remove_by_id", new RemoveById(console, collectionManager));
            register("clear", new Clear(console, collectionManager));
            register("save", new Save(console, collectionManager));
            register("execute_script", new ExecuteScript(console));
            register("exit", new Exit(console));
            register("add_if_max", new AddIfMax(console, collectionManager));
            register("remove_greater", new RemoveGreater(console, collectionManager));
            register("history", new History(console, this));
            register("sum_of_meters_above_sea_level", new SumOfMeters(console, collectionManager));
            register("group_counting_by_population", new GroupCounting(console, collectionManager));
            register("print_descending", new PrintDescending(console, collectionManager));
        }};

        // Запускаем интерактивный режим
        new Runner(console, commandManager).interactiveMode();


    }
}



