package fr.twizox.kinkobot.listeners;

import fr.twizox.kinkobot.commands.AbstractCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class SlashCommandListener extends ListenerAdapter {

    HashMap<String, AbstractCommand> commands = new HashMap<>();

    public SlashCommandListener(List<AbstractCommand> commandList) {
        for (AbstractCommand command : commandList) {
            commands.put(command.getName(), command);
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        AbstractCommand command = commands.get(event.getName());
        if (command != null) command.execute(event);
    }
}
