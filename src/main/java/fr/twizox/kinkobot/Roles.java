package fr.twizox.kinkobot;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public enum Roles {
    CIVIL("civilRole"),
    PIRATE("pirateRole"),
    MARINE("marineRole"),
    REBELLE("rebelleRole");

    private final String configKey;

    Roles(String configKey) {
        this.configKey = configKey;
    }

    public String getId(JsonObject jsonObject) {
        if (!jsonObject.has(configKey)) throw new RuntimeException("Missing config key: " + configKey);
        return jsonObject.get(configKey).getAsString();
    }

    public Role getRole(Guild guild) {
        return guild.getRoleById(getId(KinkoBot.getConfig()));
    }
}
