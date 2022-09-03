package fr.twizox.kinkobot.captcha;

import java.time.OffsetDateTime;

public class Captcha {

    private String content;
    private final OffsetDateTime dateTime;
    private int tries;

    public Captcha(String content, OffsetDateTime dateTime) {
        this.content = content;
        this.dateTime = dateTime;
        this.tries = 0;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public int getTries() {
        return tries;
    }

    public void addTry() {
        tries++;
    }
}
