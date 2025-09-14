package ru.lenok.client;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lenok.client.client_command.ExecuteScriptCommand;
import ru.lenok.client.client_command.ExitFromProgramCommand;
import ru.lenok.client.input.AbstractInput;
import ru.lenok.common.CommandRequest;
import ru.lenok.common.CommandResponse;
import ru.lenok.common.CommandWithArgument;
import ru.lenok.common.LabWorkItemAssembler;
import ru.lenok.common.commands.CommandDefinition;

import java.util.Collection;
import java.util.Stack;

import static ru.lenok.client.ClientApplication.CLIENT_ID;
import static ru.lenok.common.commands.ArgType.LONG;

@Data

public class ClientInputProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ClientInputProcessor.class);
    public static boolean debug = true;
    private final ClientConnector clientConnector;
    private Collection<CommandDefinition> commandDefinitions;
    private Stack<String> scriptExecutionContext;
    private ExecuteScriptCommand executeScriptCommand;
    private ExitFromProgramCommand exitCommand;

    public ClientInputProcessor(Collection<CommandDefinition> commandDefinitions, ClientConnector clientConnector) {
        this.scriptExecutionContext = new Stack<>();
        this.commandDefinitions = commandDefinitions;
        this.clientConnector = clientConnector;
        this.executeScriptCommand = new ExecuteScriptCommand(this);
        this.exitCommand = new ExitFromProgramCommand();
    }

    public void processInput(AbstractInput input, boolean interactive) throws Exception {
        String line;
        CommandWithArgument commandWithArgument = null;
        LabWorkItemAssembler labWorkItemAssembler = null;
        while ((line = input.readLine()) != null) {
            if (labWorkItemAssembler == null) {
                try {
                    commandWithArgument = parseLineAsCommand(line);
                } catch (Exception e) {
                    handleException(interactive, e);
                    continue;
                }
                if (commandWithArgument.getCommandDefinition().hasElement()) {
                    labWorkItemAssembler = new LabWorkItemAssembler(interactive);
                    continue;
                }
                sendAndProcessRequest(commandWithArgument, null);
                continue;
            }
            try {
                labWorkItemAssembler.addNextLine(line);
            } catch (Exception e) {
                handleException(interactive, e);
                continue;
            }
            if (labWorkItemAssembler.isFinished()) {
                sendAndProcessRequest(commandWithArgument, labWorkItemAssembler);
                labWorkItemAssembler = null;
            }
        }
        if (labWorkItemAssembler != null) {
            logger.error("Внимание! У вас есть невыполненная последняя команда - недостаточно полей введено, " + commandWithArgument);
        }
    }

    private void sendAndProcessRequest(CommandWithArgument commandWithArgument, LabWorkItemAssembler labWorkItemAssembler) throws Exception {
        CommandDefinition commandDefinition = commandWithArgument.getCommandDefinition();
        if(commandDefinition.hasElement()){
            if (labWorkItemAssembler == null){
                throw new IllegalArgumentException("Вы не передали элемент на команду, которой он необходим: " + commandWithArgument);
            }
        }
        else{
            if (labWorkItemAssembler != null){
                throw new IllegalArgumentException("Вы передали элемент на команду, которой он не нужен: " + commandWithArgument);
            }
        }
        CommandRequest commandRequest = new CommandRequest(commandWithArgument, labWorkItemAssembler == null ? null : labWorkItemAssembler.getLabWorkElement(), CLIENT_ID);
        if (commandDefinition == CommandDefinition.execute_script) {
            runExecuteScript(commandRequest);
        } else if (commandDefinition == CommandDefinition.exit) {
            exitCommand.execute();
        }
        CommandResponse commandResponse = clientConnector.sendCommand(commandRequest);
        processResponse(commandResponse);
    }

    private void runExecuteScript(CommandRequest commandRequest) throws Exception {
        try {
            executeScriptCommand.execute(commandRequest.getCommandWithArgument().getArgument());
        } catch (Exception e) {
            logger.error("Произошла ошибка при выполнении скрипта", e);
        }
    }

    private void handleException(boolean interactive, Exception e) throws Exception {
        if (!interactive) {
            throw e;
        }
        displayCommonError(e);
    }

    private void processResponse(CommandResponse commandResponse) {
        if (commandResponse.getError() == null) {
            logger.info(commandResponse.getOutput());
        } else {
            displayCommonError(commandResponse.getError());
        }

    }

    private CommandWithArgument parseLineAsCommand(String line) {
        String[] splittedLine = line.trim().split("\\s+");
        CommandWithArgument result = null;
        CommandDefinition commandDefinition;
        try {
            commandDefinition = CommandDefinition.valueOf(splittedLine[0]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Такой команды НЕТ: " + splittedLine[0], e);
        }
        if (commandDefinition == null) {
            throw new IllegalArgumentException("Такой команды НЕТ: " + splittedLine[0]);
        }
        if (!commandDefinition.hasArg() && splittedLine.length >= 2) {
            throw new IllegalArgumentException("Слишком много аргументов, ожидалось 0: " + line);
        } else if (commandDefinition.hasArg()) {
            if (splittedLine.length == 1 || splittedLine.length > 2) {
                throw new IllegalArgumentException("Неправильное количество аргументов, ожидался 1: " + line);
            }
            if (commandDefinition.getArgType() == LONG) {
                try {
                    Long.parseLong(splittedLine[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ожидался аргумент типа Long, пришло: " + line);
                }
            }
        }
        result = new CommandWithArgument(commandDefinition, splittedLine.length == 2 ? splittedLine[1] : null);
        return result;
    }

    public void setScriptExecutionContext(String path) {
        scriptExecutionContext.push(path);
    }

    public void exitContext() {
        scriptExecutionContext.pop();
    }

    public boolean checkContext(String currentFile) {
        return scriptExecutionContext.contains(currentFile);
    }

    private void displayCommonError(Exception e) {
        logger.error(e.getMessage());
    }

    /*
    все команды создаются при инициализации - только один раз, при этом в конструктор передается LabWorkService (сервисный слой, который работает со storage - моя коллекция + crud)
    view - InputProcessor должен сформировать CommandRequest, отправить на контроллер CommandController
    CommandRequest состоит из имени команды, ее аргумента и полностью собранного LabWork элемента
    контроллер должен вызвать метод команды Command.execute и передать туда аргумент и LabWork
    Результат работы команды оборачивается в CommandResponse - строка или ошибка, и возвращается в InputProcessor

     */
}
