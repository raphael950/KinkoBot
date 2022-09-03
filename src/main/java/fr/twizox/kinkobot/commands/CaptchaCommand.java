package fr.twizox.kinkobot.commands;

import com.google.gson.JsonObject;
import fr.twizox.kinkobot.KinkoBot;
import fr.twizox.kinkobot.Roles;
import fr.twizox.kinkobot.captcha.CaptchaManager;
import fr.twizox.kinkobot.utils.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.FileUpload;
import net.logicsquad.nanocaptcha.content.NumbersContentProducer;
import net.logicsquad.nanocaptcha.image.ImageCaptcha;
import net.logicsquad.nanocaptcha.image.renderer.DefaultWordRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CaptchaCommand extends AbstractCommand {

    private final JsonObject config;
    private final CaptchaManager captchaManager;

    public CaptchaCommand(JsonObject config, CaptchaManager captchaManager) {
        super("captcha", "Générer un captcha à résoudre");
        this.captchaManager = captchaManager;
        this.config = config;

        addOption(OptionType.STRING, "captcha", "Le code envoyé par le bot", false);
        super.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.NICKNAME_CHANGE));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (event.getMember().getRoles().size() != 0) {
            event.reply("Vous êtes déjà vérifié").setEphemeral(true).queue();
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setFooter("KinkoMC - 2022", KinkoBot.instance.getApi().getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());

        if (captchaManager.hasCaptcha(event.getMember())) {
            if (event.getOption("captcha") == null) {
                event.reply("Vous avez déjà un captcha en cours, veuillez le résoudre en faisant /captcha <code>.").setEphemeral(true).queue();
                return;
            }
            Member member = event.getMember();

            if (captchaManager.checkCaptcha(member, event.getOption("captcha").getAsString())) {
                embedBuilder.setTitle("Captcha validé");
                embedBuilder.setDescription("Vous allez être redirigé vers le serveur.");
                embedBuilder.setColor(Colors.NICE_GREEN);
                event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                event.getGuild().addRoleToMember(member, Roles.CIVIL.getRole(event.getGuild(), config)).queue();
                return;
            } else if (captchaManager.hasCaptcha(member)) {
                embedBuilder.setFooter((3 - captchaManager.getCaptcha(member).getTries()) + " tentatives restantes", KinkoBot.instance.getApi().getSelfUser().getAvatarUrl());
            } else {
                embedBuilder.setTitle("Captcha invalide");
                embedBuilder.setDescription("Vous allez être expulsé du serveur après 3 tentatives incorrectes.");
                embedBuilder.setColor(Colors.NICE_RED);
                event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                event.getMember().kick("Trois tentatives de captcha infructueuses.").queueAfter(5L, TimeUnit.SECONDS);
                return;
            }
        }

        sendCaptcha(embedBuilder, event);
    }

    private void sendCaptcha(EmbedBuilder embedBuilder, SlashCommandInteractionEvent event) {

        ImageCaptcha captcha = generateCaptcha(List.of(Colors.NICE_BLUE));
        MessageChannel channel = event.getMessageChannel();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            ImageIO.write(captcha.getImage(), "png", bytes);
        } catch (Exception e) {
            e.printStackTrace();
            event.reply("Une erreur est survenue lors de la génération de votre Captcha. Veuillez contacter le Staff.").queue();
            return;
        }

        if (captchaManager.hasCaptcha(event.getMember())) {
            captchaManager.getCaptcha(event.getMember()).setContent(captcha.getContent());
        } else {
            captchaManager.addCaptcha(event.getMember(), captcha);
        }

        embedBuilder.setImage("attachment://img.png")
                .setTitle("Vérification Anti-Robot \uD83E\uDD16")
                .setDescription("Veuillez entrer la commande `/captcha` suivi du code ci-dessous.")
                .setColor(Colors.NICE_BLUE);

        event.replyEmbeds(embedBuilder.build()).addFiles(FileUpload.fromData(bytes.toByteArray(), "img.png")).setEphemeral(true).queue();
        captcha = null;
    }

    private ImageCaptcha generateCaptcha(List<Color> colors) {
        return new ImageCaptcha.Builder(200, 50)
                .addFilter()
                .addContent(new NumbersContentProducer(6), new DefaultWordRenderer(colors, List.of(new Font("Arial", Font.BOLD, 40))))
                .build();
    }


}
