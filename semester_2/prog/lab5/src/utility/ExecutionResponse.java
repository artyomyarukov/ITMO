package utility;

/**
 * Класс для возврата результатов выполнения команд
 */

public class ExecutionResponse {
    private boolean exitCode;
    private String massage;

    /**
     * Конструктор с указанием статуса и сообщения.
     * @param code статус выполнения (true - успех, false - ошибка)
     * @param s текстовое сообщение с результатом или описанием ошибки
     */


    public ExecutionResponse(boolean code, String s) {
        exitCode = code;
        massage = s;
    }
    /**
     * Конструктор для успешных операций.
     * Автоматически устанавливает exitCode = true.
     * @param s текстовое сообщение о результате выполнения
     */

    public ExecutionResponse(String s) {
        this(true, s);
    }

    /**
     * Возвращает текстовое сообщение с результатом выполнения.
     * @return сообщение о результате или ошибке
     */

    public boolean getExitCode() {
        return exitCode;
    }

    /**
     * Возвращает текстовое сообщение с результатом выполнения
     * @return сообщение о результате или ошибке
     */

    public String getMassage() {
        return massage;
    }

    /**
     * Возвращает строковое представление результата выполнения
     * @return строковое представление результата выполнения
     */

    public String toString() {
        return String.valueOf(exitCode) + ";" + massage;
    }
}