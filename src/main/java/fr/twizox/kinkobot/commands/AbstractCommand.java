package fr.twizox.kinkobot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand extends CommandDataImpl {


    public AbstractCommand(@NotNull String name, @NotNull String description) {
        super(name, description);
    }

    public abstract void execute(SlashCommandInteractionEvent event);

}
