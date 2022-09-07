package fr.twizox.kinkobot.commands;

import fr.twizox.kinkobot.KinkoBot;
import fr.twizox.kinkobot.Roles;
import fr.twizox.kinkobot.captcha.CaptchaManager;
import fr.twizox.kinkobot.utils.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.concurrent.TimeUnit;

public class CaptchaCommand extends AbstractCommand {

    private final CaptchaManager captchaManager;

    public CaptchaCommand(CaptchaManager captchaManager) {
        super("captcha", "Générer un captcha à résoudre");
        this.captchaManager = captchaManager;
        super.addOption(OptionType.INTEGER, "code", "Nombre se trouvant sur l'image du captcha envoyé.", false);
        super.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.NICKNAME_CHANGE));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setFooter("KinkoMC - 2022", KinkoBot.instance.getApi().getSelfUser().getAvatarUrl())
                .setTimestamp(event.getTimeCreated());

        if (event.getMember().getRoles().size() != 0) {
            event.replyEmbeds(embedBuilder.setDescription("Vous êtes déjà vérifié !").setColor(Colors.NICE_RED).build()).setEphemeral(true).queue();
            return;
        }

        if (captchaManager.hasCaptcha(event.getMember())) {
            if (event.getOption("code") == null) {
                captchaManager.sendCaptcha(false, embedBuilder, event);
                return;
            }

            // Checking captcha content

            Member member = event.getMember();
            int submittedCode = event.getOption("code").getAsInt();

            if (captchaManager.checkCaptcha(member, submittedCode)) {
                event.replyEmbeds(embedBuilder
                        .setTitle("Vérification réussie")
                        .setDescription("Vous allez être redirigé vers le serveur...")
                        .setColor(Colors.NICE_GREEN)
                        .build()).setEphemeral(true).queue();
                event.getGuild().addRoleToMember(member, Roles.CIVIL.getRole(event.getGuild())).queue();
                return;
            }
            if (captchaManager.hasCaptcha(member)) {
                embedBuilder.setFooter((3 - captchaManager.getCaptcha(member).getTries()) + " tentatives restantes", KinkoBot.instance.getApi().getSelfUser().getAvatarUrl());
            } else {
                embedBuilder.setTitle("Captcha invalide");
                embedBuilder.setDescription("Vous avez été expulsé du serveur après 3 tentatives incorrectes.\n*Veuillez rejoindre le serveur à nouveau pour réessayer* \n\n> https://discord.gg/kinkomc");
                embedBuilder.setColor(Colors.NICE_RED);
                member.getUser().openPrivateChannel().queue((channel) -> {
                    member.kick("Trois tentatives de captcha infructueuses.").queueAfter(5L, TimeUnit.SECONDS);
                    channel.sendMessageEmbeds(embedBuilder.build()).queue();
                });
                return;
            }

        }

        // Overriding previous captcha because it could be a failed attempt.
        captchaManager.sendCaptcha(true, embedBuilder, event);
    }


}
