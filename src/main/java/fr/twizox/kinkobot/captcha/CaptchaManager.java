package fr.twizox.kinkobot.captcha;

import net.dv8tion.jda.api.entities.Member;
import net.logicsquad.nanocaptcha.image.ImageCaptcha;

import java.time.OffsetDateTime;
import java.util.WeakHashMap;

public class CaptchaManager {

    private final WeakHashMap<Member, Captcha> captchas = new WeakHashMap<>();

    public void addCaptcha(Member member, ImageCaptcha imageCaptcha) {
        captchas.put(member, new Captcha(imageCaptcha.getContent(), imageCaptcha.getCreated()));
    }

    public void changeContent(Member member, String content) {
        captchas.get(member).setContent(content);
    }

    public void addCaptcha(Member member, Captcha captcha) {
        captchas.put(member, captcha);
    }

    public void removeCaptcha(Member member) {
        captchas.remove(member);
    }

    public Captcha getCaptcha(Member member) {
        return captchas.get(member);
    }

    public String getCaptchaContent(Member member) {
        return captchas.get(member).getContent();
    }

    public boolean hasCaptcha(Member member) {
        return captchas.containsKey(member);
    }

    public OffsetDateTime getTimeout(Member member) {
        return captchas.get(member).getDateTime();
    }

    public boolean checkCaptcha(Member member, String lowerCaseMessage) {
        if (!hasCaptcha(member)) return false;
        Captcha captcha = captchas.get(member);

        if (captcha.getContent().equals(lowerCaseMessage)) {
            removeCaptcha(member);
            return true;
        }
        if (captcha.getTries() > 1) {
            removeCaptcha(member);
        }
        captcha.addTry();
        return false;
    }

}
