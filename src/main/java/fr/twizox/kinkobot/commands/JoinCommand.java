package fr.twizox.kinkobot.commands;

import fr.twizox.kinkobot.utils.NiceColors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;

import java.util.Date;

public class JoinCommand extends AbstractCommand {

    public JoinCommand() {
        super("join", "Commande de test pour message de join");
        super.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        if (user.isBot()) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Bienvenue sur le serveur ").append(event.getGuild().getName()).append(" ").append(user.getAsMention()).append(" !\n")
                .append("Vous êtes le **").append(event.getGuild().getMemberCount()).append("**ème membre !\n")
                .append("> Notre site web : ").append("https://kinkomc.fr").append("\n")
                .append("> Notre boutique : ").append("https://kinkomc.fr/shop").append("\n");
        String description = sb.toString();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Bienvenue " + user.getAsTag())
                .setDescription(description)
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setTimestamp(new Date().toInstant())
                .setColor(NiceColors.DEFAULT.getColor())
                .setFooter("KinkoMC - 2022", event.getJDA().getSelfUser().getAvatarUrl());

        event.replyEmbeds(embedBuilder.build()).queue();
    }

}
