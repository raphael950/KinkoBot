package fr.twizox.kinkobot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ouverture")) {
            OptionMapping option = event.getOption("utilisateur");
            boolean isPrivate = option == null;

            String description =
                    "Le serveur est encore en cours de **développement** \n\n• L'ouverture est prévue fin septembre.";
            if (!isPrivate) {
                description =
                        "Bonjour " + option.getAsMentionable().getAsMention() + ", le serveur est encore en cours de **développement** \n\n• L'ouverture est prévue fin septembre.";
            }

            Color randomColor = Color.getHSBColor((float) Math.random(), 0.5f, 1.0f);

            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Date d'ouverture", "https://kinkomc.fr/")
                    .setDescription(description)
                    .setColor(randomColor)
                    .build();
            event.replyEmbeds(embed).setEphemeral(isPrivate).queue();
        }
    }
}
