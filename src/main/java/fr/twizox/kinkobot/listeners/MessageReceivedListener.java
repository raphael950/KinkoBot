package fr.twizox.kinkobot.listeners;

import com.google.gson.JsonObject;
import fr.twizox.kinkobot.Channels;
import fr.twizox.kinkobot.captcha.CaptchaManager;
import fr.twizox.kinkobot.utils.NiceColors;
import fr.twizox.kinkobot.utils.DiscordUtils;
import fr.twizox.kinkobot.utils.FileUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceivedListener extends ListenerAdapter {

    private final CaptchaManager captchaManager;
    private final JsonObject data;

    public MessageReceivedListener(CaptchaManager captchaManager, JsonObject data) {
        this.captchaManager = captchaManager;
        this.data = data;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!ChannelType.TEXT.equals(event.getChannelType())) return;

        User user = event.getAuthor();
        if (user.isBot()) return;

        String channelId = event.getGuildChannel().getId();
        if (channelId.equals(Channels.SUGGESTION.getId())) {
            suggestion(event);
        } else if (channelId.equals(Channels.VERIFICATION.getId())) {
            event.getMessage().delete().queue();
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(NiceColors.RED.getColor())
                    .setTitle("Vérification \uD83D\uDD12")
                    .setDescription("Vous n'avez pas de captcha à résoudre !\nVeuillez effectuer la commande `/captcha` dans le channel " + event.getChannel().getAsMention())
                    .setFooter("KinkoMC", event.getJDA().getSelfUser().getAvatarUrl());

            if (captchaManager.hasCaptcha(event.getMember())) {
                embedBuilder.setDescription(("> `/captcha code` dans le channel " + event.getChannel().getAsMention() + " pour valider votre captcha !"));
            }

            DiscordUtils.sendEmbed(30, user, embedBuilder);
        }

    }


    private void suggestion(MessageReceivedEvent event) {

        User user = event.getAuthor();
        MessageChannel channel = event.getChannel();

        event.getMessage().delete().queue();
        int count = data.get("suggestionCount").getAsInt() + 1;
        data.addProperty("suggestionCount", count);

        MessageEmbed messageEmbed = new EmbedBuilder()
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl()).setTitle("Suggestion #" + count)
                .setDescription(event.getMessage().getContentRaw())
                .setColor(NiceColors.getRandomColor()).build();

        channel.sendMessageEmbeds(messageEmbed).queue((message) -> {
            message.addReaction(Emoji.fromUnicode("⬆️")).and(message.addReaction(Emoji.fromUnicode("⬇️"))).queue();
            message.createThreadChannel("Suggestion de " + user.getName()).queue((threadChannel) -> {
                threadChannel.addThreadMember(user).queue();
                threadChannel.leave().queue();
                FileUtils.saveJSONFile("data.json", data);
            });
        });
    }

}

