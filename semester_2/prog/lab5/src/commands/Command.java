package commands;

/**
 * Абстрактная команда с именем и описанием
 */
public abstract class Command implements Describable, Executable {
    private final String name;
    private final String description;

    /**
     * Конструктор команды
     * @param name - имя команды
     * @param description - описание команды
     */

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return Название и использование команды.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Описание команды.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Сравнение двух команд
     * @param obj - объект сравнение
     * @return резултат сравнение двух команд, true если имя и описание совпадают, false в противном случае
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Command command = (Command) obj;
        return name.equals(command.name) && description.equals(command.description);
    }

    /**
     * Получение хэш кода команды
     * @return хэш код команды
     */

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    /**
     * Строковое представление команды
     * @return строковое представление команды
     */

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}