package fr.twizox.kinkobot.listeners;

import fr.twizox.kinkobot.utils.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class ButtonClickListener extends ListenerAdapter {

    HashMap<String, String> roles = new HashMap<>();

    public ButtonClickListener() {
        roles.put("pirate", "1015414701489934356");
        roles.put("rebelle", "1015414857945849897");
        roles.put("marine", "1015414810915127396");
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        User user = event.getUser();
        Member member = event.getMember();
        Guild guild = event.getGuild();
        if (member == null || guild == null) return;

        for (String roleId : roles.values()) {
            Role role = guild.getRoleById(roleId);
            if (member.getRoles().contains(role)) {
                if (role.getId().equals(roles.get(event.getComponentId()))) { // Role demandé
                    event.reply("Tu as déjà ce rôle !").setEphemeral(true).queue();
                    return;
                } // Role non demandé mais que le joueur possède
                guild.removeRoleFromMember(member, role).queue();
            }
        }

        Role roleToAdd = getRole(event);
        guild.addRoleToMember(member, roleToAdd).submit().thenRun(() -> {
            event.replyEmbeds(new EmbedBuilder().setColor(Colors.DEFAULT).setDescription("Vous avez rejoins les " + roleToAdd.getName()).build())
                    .setEphemeral(true).queue();
        });

    }

    private Role getRole(ButtonInteractionEvent event) {
        return event.getGuild().getRoleById(roles.get(event.getComponentId()));
    }

}
