package fr.twizox.kinkobot.listeners;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberJoinListener extends ListenerAdapter {

    private final JsonObject config;

    public GuildMemberJoinListener(JsonObject config) {
        this.config = config;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

    }

}
