package fr.twizox.kinkobot.listeners;

import fr.twizox.kinkobot.captcha.CaptchaManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberRemoveListener extends ListenerAdapter {

    private final CaptchaManager captchaManager;

    public GuildMemberRemoveListener(CaptchaManager captchaManager) {
        this.captchaManager = captchaManager;
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        Member member = event.getMember();
        if (member == null || event.getUser().isBot()) return;
        if (captchaManager.hasCaptcha(member)) {
            captchaManager.removeCaptcha(member);
        }

    }
}
