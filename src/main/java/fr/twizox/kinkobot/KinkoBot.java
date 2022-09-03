package fr.twizox.kinkobot;

import com.google.gson.JsonObject;
import fr.twizox.kinkobot.captcha.CaptchaManager;
import fr.twizox.kinkobot.commands.*;
import fr.twizox.kinkobot.databases.H2Database;
import fr.twizox.kinkobot.listeners.ButtonClickListener;
import fr.twizox.kinkobot.listeners.MessageReceivedListener;
import fr.twizox.kinkobot.listeners.SlashCommandListener;
import fr.twizox.kinkobot.utils.FileUtils;
import fr.twizox.kinkobot.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class KinkoBot {

    public static final KinkoBot instance = new KinkoBot();
    private JDA api;
    private H2Database database;

    public static void main(String[] args) throws IOException {
        try {
            instance.start(args);
        } catch (LoginException | InterruptedException e) {
            Logger.error(KinkoBot.class, "Invalid token");
        }
    }

    public void start(String[] args) throws LoginException, InterruptedException {

        FileUtils.saveResource("config.json", false);
        FileUtils.saveResource("data.json", false);
        FileUtils.saveResource("tokens.json", false);

        JsonObject config;
        JsonObject data;
        JsonObject tokens;

        try {
            config = FileUtils.parseJSONFile("config.json");
            data = FileUtils.parseJSONFile("data.json");
            tokens = FileUtils.parseJSONFile("tokens.json");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error(getClass(), "Could not load JSONS");
            return;
        }

        api = JDABuilder.createDefault(tokens.get("discord").getAsString(),
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS)
                .setActivity(Activity.watching("kinkomc.fr"))
                .build();

        Logger.info(getClass(), "Bot started as " + api.getSelfUser().getAsTag() + ", loading on " + api.awaitReady().getGuilds().size() + " guilds.");

        CaptchaManager captchaManager = new CaptchaManager();
        api.updateCommands().submit();

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommands(getApi().getGuilds(),
                List.of(
                        new OpenCommand(),
                        new JoinCommand(),
                        new RoleCommand(),
                        new CaptchaCommand(config, captchaManager)
                ));

        api.addEventListener(new MessageReceivedListener(captchaManager, config, data),
                new SlashCommandListener(commandManager),
                new ButtonClickListener());

        Logger.info(getClass(), "Bot loaded successfully.");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("exit")) {
                stop(data);
            } else {
                Logger.warn(getClass(), "Unknown command");
                Logger.warn(getClass(), "Available commands: exit");
            }
        }
    }

    public void stop(JsonObject data) {
        Logger.info(getClass(), "Stopping KinkoBot...");

        FileUtils.saveJSONFile("data.json", data);
        Logger.info(getClass(), "Off");
        api.shutdown();

        System.exit(0);
    }

    public JDA getApi() {
        return api;
    }

}