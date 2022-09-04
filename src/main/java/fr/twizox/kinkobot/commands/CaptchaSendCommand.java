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
                .setDescription("1) Effectuez la commande `/captcha` afin de générer un captcha à résoudre.\n" +
                        "2) Refaites la commande suivie du code que vous avez reçu: `/captcha code`\n\n" +
                        "*Votre code a disparu ? Pas de problème ! Refaites la commande pour en générer un à nouveau*")
                .setColor(Colors.NICE_ORANGE)
                .setFooter("KinkoMC - 2022", event.getJDA().getSelfUser().getAvatarUrl())
                .setTimestamp(event.getTimeCreated());
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        event.reply("Le captcha a été envoyé.").setEphemeral(true).queue();
    }



}
