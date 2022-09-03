package fr.twizox.kinkobot.commands;

import fr.twizox.kinkobot.utils.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;

public class CaptchaSendCommand extends AbstractCommand {

    public CaptchaSendCommand() {
        super("captchasend", "Générer l'embed du captcha");
        super.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Vérification \uD83D\uDD12")
                .setDescription("Veuillez effectuer la commande `/captcha` afin de générer un captcha à résoudre.")
                .setColor(Colors.NICE_ORANGE)
                .setFooter("KinkoMC - 2022", event.getJDA().getSelfUser().getAvatarUrl())
                .setTimestamp(event.getTimeCreated().toInstant());
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        event.reply("Le captcha a été envoyé.").setEphemeral(true).queue();
    }



}
