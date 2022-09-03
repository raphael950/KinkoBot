package fr.twizox.kinkobot.commands;

import fr.twizox.kinkobot.KinkoBot;
import fr.twizox.kinkobot.utils.Logger;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.List;

public class CommandManager {

    private final HashMap<String, AbstractCommand> commands = new HashMap<>();

    public void registerCommands(List<AbstractCommand> commandList) {
        for (AbstractCommand command : commandList) {
            commands.put(command.getName(), command);
        }
        KinkoBot.instance.getApi().updateCommands().addCommands(commandList).queue((success) -> {
            Logger.info(getClass(), "Successfully registered " + success.size() + " commands.");
        }, (failure) -> {
            Logger.error(getClass(), "Failed to register commands:" + failure.getMessage());
        });
    }

    public AbstractCommand getCommand(String name) {
        return commands.get(name);
    }

    public boolean getAndExecuteCommand(SlashCommandInteractionEvent event) {
        AbstractCommand command = commands.get(event.getName());
        if (command != null) {
            command.execute(event);
            return true;
        }
        return false;
    }

}
