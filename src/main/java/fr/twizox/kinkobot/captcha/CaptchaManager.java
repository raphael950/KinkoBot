package fr.twizox.kinkobot.captcha;

import fr.twizox.kinkobot.utils.NiceColors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.logicsquad.nanocaptcha.content.NumbersContentProducer;
import net.logicsquad.nanocaptcha.image.ImageCaptcha;
import net.logicsquad.nanocaptcha.image.renderer.DefaultWordRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.WeakHashMap;

public class CaptchaManager {

    private final WeakHashMap<Member, Captcha> captchas = new WeakHashMap<>();

    public void addCaptcha(Member member, ImageCaptcha imageCaptcha) {
        captchas.put(member, new Captcha(imageCaptcha));
    }

    public void removeCaptcha(Member member) {
        if (hasCaptcha(member)) {
            captchas.get(member).setImageCaptcha(null);
        }
        captchas.remove(member);
    }

    public Captcha getCaptcha(Member member) {
        return captchas.get(member);
    }

    public boolean hasCaptcha(Member member) {
        return captchas.containsKey(member);
    }

    public boolean checkCaptcha(Member member, int code) {
        if (!hasCaptcha(member)) return false;
        Captcha captcha = captchas.get(member);

        if (captcha.getImageCaptcha().getContent().equals(Integer.toString(code))) {
            removeCaptcha(member);
            return true;
        }
        if (captcha.getTries() > 1) {
            removeCaptcha(member);
        }
        captcha.addTry();
        return false;
    }

    private ReplyCallbackAction getCallBackWithImage(byte[] bytes, EmbedBuilder embedBuilder, SlashCommandInteractionEvent event) {
        return event.replyEmbeds(embedBuilder.setImage("attachment://captcha.png").build())
                .addFiles(FileUpload.fromData(bytes, "captcha.png"))
                .setEphemeral(true);
    }

    private byte[] getOrGenerateCaptcha(Member member, boolean override) {
        if (!hasCaptcha(member)) {
            addCaptcha(member, generateCaptcha());
        } else if (override) {
            getCaptcha(member).setImageCaptcha(generateCaptcha());
        }
        ImageCaptcha imageCaptcha = getCaptcha(member).getImageCaptcha();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            ImageIO.write(imageCaptcha.getImage(), "png", bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes.toByteArray();
    }

    public void sendCaptcha(boolean overridePreviousCaptcha, EmbedBuilder embedBuilder, SlashCommandInteractionEvent event) {


        MessageChannel channel = event.getMessageChannel();
        Member member = event.getMember();

        embedBuilder.setImage("attachment://img.png")
                .setTitle("Vérification Anti-Robot \uD83E\uDD16")
                .setDescription("Veuillez entrer la commande `/captcha` **en spécifiant le code ci-dessous**.")
                .setColor(NiceColors.BLUE.getColor());

        getCallBackWithImage(getOrGenerateCaptcha(member, overridePreviousCaptcha), embedBuilder, event).queue();
    }

    private ImageCaptcha generateCaptcha() {
        return new ImageCaptcha.Builder(200, 50)
                .addContent(new NumbersContentProducer(6), new DefaultWordRenderer(List.of(NiceColors.BLUE.getColor()), List.of(new Font("Arial", Font.BOLD, 40))))
                .build();
    }

}
