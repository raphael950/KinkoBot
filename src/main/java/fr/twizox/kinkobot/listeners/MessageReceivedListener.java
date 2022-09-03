package fr.twizox.kinkobot.listeners;

import com.google.gson.JsonObject;
import fr.twizox.kinkobot.Channels;
import fr.twizox.kinkobot.captcha.CaptchaManager;
import fr.twizox.kinkobot.utils.Colors;
import fr.twizox.kinkobot.utils.FileUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceivedListener extends ListenerAdapter {

    private final CaptchaManager captchaManager;
    private final JsonObject config;
    private final JsonObject data;

    public MessageReceivedListener(CaptchaManager captchaManager, JsonObject config, JsonObject data) {
        this.captchaManager = captchaManager;
        this.config = config;
        this.data = data;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!ChannelType.TEXT.equals(event.getChannelType())) return;

        User user = event.getAuthor();
        if (user.isBot()) return;

        String channelId = event.getGuildChannel().getId();
        if (channelId.equals(Channels.SUGGESTION.getId(config))) {
            suggestion(event);
        } else if (channelId.equals(Channels.VERIFICATION.getId(config))) {
            event.getMessage().delete().queue();
        }

    }


    private void suggestion(MessageReceivedEvent event) {

        User user = event.getAuthor();
        GuildMessageChannelUnion channel = event.getGuildChannel();

        event.getMessage().delete().queue();
        int count = data.get("suggestionCount").getAsInt() + 1;
        data.addProperty("suggestionCount", count);

        MessageEmbed messageEmbed = new EmbedBuilder()
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl()).setTitle("Suggestion #" + count)
                .setDescription(event.getMessage().getContentRaw())
                .setColor(Colors.getRandomColor()).build();

        event.getChannel().sendMessageEmbeds(messageEmbed).queue((message) -> {
            message.createThreadChannel("Suggestion de " + user.getName()).queue((threadChannel) -> {
                threadChannel.addThreadMember(user).queue();
                threadChannel.leave().queue();
                FileUtils.saveJSONFile("data.json", data);
            });
        });
    }

}

