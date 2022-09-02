package fr.twizox.kinkobot.listeners;

import com.google.gson.JsonObject;
import fr.twizox.kinkobot.utils.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceivedListener extends ListenerAdapter {

    private final JsonObject data;

    public MessageReceivedListener(JsonObject data) {
        this.data = data;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!ChannelType.TEXT.equals(event.getChannelType())) return;

        User user = event.getAuthor();

        if (user.isBot()) return;
        event.getMessage().delete().queue();
        int count = data.get("suggestionCount").getAsInt() + 1;
        data.addProperty("suggestionCount", count);

        GuildMessageChannelUnion channel = event.getGuildChannel();

        MessageHistory.getHistoryFromBeginning(channel).submit().whenComplete((messages, throwable) -> {
            if (throwable != null) throwable.printStackTrace();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl());
            embedBuilder.setTitle("Suggestion #" + count);
            embedBuilder.setDescription(event.getMessage().getContentRaw());
            embedBuilder.setColor(Colors.getRandomColor());
            MessageEmbed messageEmbed = embedBuilder.build();

            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue((message) -> {
                message.createThreadChannel("Suggestion de " + user.getName()).queue((threadChannel) -> {
                    threadChannel.addThreadMember(user).queue();
                    threadChannel.leave().queue();
                });
            });
        }).join().size();

    }

}
