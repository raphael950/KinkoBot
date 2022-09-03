package fr.twizox.kinkobot;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public enum Roles {
    CIVIL("civilRole");

    private final String configKey;

    Roles(String configKey) {
        this.configKey = configKey;
    }

    public String getId(JsonObject jsonObject) {
        if (!jsonObject.has(configKey)) throw new RuntimeException("Missing config key: " + configKey);
        return jsonObject.get(configKey).getAsString();
    }

    public Role getRole(Guild guild, JsonObject jsonObject) {
        return guild.getRoleById(getId(jsonObject));
    }
}
