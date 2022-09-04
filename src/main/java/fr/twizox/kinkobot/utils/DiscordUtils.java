package fr.twizox.kinkobot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.time.Duration;

public class DiscordUtils {

    // Send message to user and delete it 30 seconds later, handles blocked messages in context channel.
    public static void sendEmbed(int timeBeforeDelete, User user, EmbedBuilder embedBuilder) {
        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()))
                .delay(Duration.ofSeconds(timeBeforeDelete))
                .flatMap(Message::delete) // delete after 30 seconds
                .queue(null, new ErrorHandler()
                        .ignore(ErrorResponse.UNKNOWN_MESSAGE) // if delete fails that's fine
                        .handle(
                                ErrorResponse.CANNOT_SEND_TO_USER,  // Fallback handling for blocked messages
                                (exception) -> {
                                    // Do what you want when message cannot be sent to user
                                }));
    }

}
