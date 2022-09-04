package fr.twizox.kinkobot.captcha;

import net.logicsquad.nanocaptcha.image.ImageCaptcha;

public class Captcha {

    private ImageCaptcha imageCaptcha;
    private int tries;

    public Captcha(ImageCaptcha imageCaptcha) {
        this.imageCaptcha = imageCaptcha;
        this.tries = 0;
    }

    public ImageCaptcha getImageCaptcha() {
        return imageCaptcha;
    }

    public void setImageCaptcha(ImageCaptcha imageCaptcha) {
        this.imageCaptcha = imageCaptcha;
    }

    public int getTries() {
        return tries;
    }

    public void addTry() {
        tries++;
    }
}
