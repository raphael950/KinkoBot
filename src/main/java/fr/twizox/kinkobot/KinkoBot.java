package fr.twizox.kinkobot;

import com.google.gson.JsonObject;
import fr.twizox.kinkobot.databases.H2Database;
import fr.twizox.kinkobot.listeners.MessageReceivedListener;
import fr.twizox.kinkobot.listeners.SlashCommandListener;
import fr.twizox.kinkobot.utils.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Scanner;

public class KinkoBot {

    public static final KinkoBot instance = new KinkoBot();
    private JDA api;
    private H2Database database;

    public static void main(String[] args) throws IOException {
        try {
            instance.start(args);
        } catch (LoginException e) {
            System.out.println("Invalid token");
        }
    }

    public void start(String[] args) throws LoginException {

        FileUtils.saveResource("config.json", false);
        FileUtils.saveResource("data.json", false);

        JsonObject config;
        JsonObject data;

        try {
            config = FileUtils.parseJSONFile("config.json");
            data = FileUtils.parseJSONFile("data.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load config.json or data.json");
            return;
        }

        api = JDABuilder.createDefault(config.get("token").getAsString(),
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS)
                .setActivity(Activity.watching("kinkomc.fr"))
                .addEventListeners(new MessageReceivedListener(data), new SlashCommandListener())
                .build();
        api.updateCommands().addCommands(Commands.slash("ouverture", "Informations concernant la date d'ouverturee")
                .addOption(OptionType.MENTIONABLE, "utilisateur", "Afficher le message d'ouverture à un membre du serveur", false, false)).submit().thenRun(() -> {
            System.out.println("Commands registered!");
        });

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("exit")) {
                stop(data);
            } else {
                System.out.println("Unknown command: " + line);
                System.out.println("Available commands: exit");
            }
        }
    }

    public void stop(JsonObject data) {
        System.out.println("Exiting...");
        FileUtils.saveJSONFile("data.json", data);
        api.shutdown();
        System.exit(0);
    }

    public JDA getApi() {
        return api;
    }

}