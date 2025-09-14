package ru.lenok.server.commands;

import ru.lenok.common.CommandRequest;
import ru.lenok.common.CommandResponse;
import ru.lenok.common.commands.AbstractCommand;
import ru.lenok.server.collection.LabWorkService;

import java.io.IOException;

import static ru.lenok.server.commands.CommandName.filter_starts_with_name;


public class FilterStartsWithNameCommand extends AbstractCommand {
    LabWorkService labWorkService;

    public FilterStartsWithNameCommand(LabWorkService labWorkService) {
        super(filter_starts_with_name.getBehavior(), "Аргумент - name. Вывести элементы, значение поля name которых начинается с заданной подстроки");
        this.labWorkService = labWorkService;
    }

    private CommandResponse execute(String arg) {
        return new CommandResponse(labWorkService.filterWithName(arg));
    }

    @Override
    public CommandResponse execute(CommandRequest req) throws IOException {
        return execute(req.getCommandWithArgument().getArgument());
    }
}
