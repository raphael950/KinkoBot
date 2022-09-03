package fr.twizox.kinkobot.listeners;

import com.google.gson.JsonObject;
import fr.twizox.kinkobot.Roles;
import fr.twizox.kinkobot.utils.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class ButtonClickListener extends ListenerAdapter {

    private final JsonObject config;
    private final List<Roles> roles = List.of(Roles.PIRATE, Roles.MARINE, Roles.REBELLE);

    public ButtonClickListener(JsonObject config) {
        this.config = config;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        Member member = event.getMember();
        Guild guild = event.getGuild();
        if (member == null || guild == null) return;

        for (Roles roleEnum : roles) {
            Role role = roleEnum.getRole(guild, config);
            if (!member.getRoles().contains(role)) continue;
            guild.removeRoleFromMember(member, role).queue();
            if (roleEnum == getRoleEnum(event)) { // Role demandé
                event.replyEmbeds(new EmbedBuilder()
                        .setColor(Colors.NICE_GREEN)
                        .setDescription("Vous êtes de nouveau Citoyen !")
                        .setFooter("KinkoMC - 2022", event.getJDA().getSelfUser().getAvatarUrl())
                        .build()).setEphemeral(true).queue();
                return;
            }
        }

        Role roleToAdd = getRole(event);
        guild.addRoleToMember(member, roleToAdd).submit().thenRun(() -> {
            event.replyEmbeds(new EmbedBuilder().setColor(Colors.NICE_GREEN)
                            .setDescription("Vous avez rejoint les " + roleToAdd.getName() + "s !")
                            .setFooter("KinkoMC - 2022", event.getJDA().getSelfUser().getAvatarUrl()).build())
                    .setEphemeral(true).queue();
        });

    }

    private Roles getRoleEnum(ButtonInteractionEvent event) {
        return Roles.valueOf(event.getComponentId().toUpperCase());
    }

    private Role getRole(ButtonInteractionEvent event) {
        return getRoleEnum(event).getRole(event.getGuild(), config);
    }

}
