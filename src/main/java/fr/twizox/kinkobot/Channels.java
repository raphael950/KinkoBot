package fr.twizox.kinkobot;

import com.google.gson.JsonObject;

public enum Channels {

    SUGGESTION("suggestionChannel"),
    VERIFICATION("verificationChannel");

    private final String configKey;

    Channels(String configKey) {
        this.configKey = configKey;
    }

    public String getId() {
        JsonObject jsonObject = KinkoBot.getConfig();
        if (!jsonObject.has(configKey)) throw new RuntimeException("Missing config key: " + configKey);
        return jsonObject.get(configKey).getAsString();
    }

}
