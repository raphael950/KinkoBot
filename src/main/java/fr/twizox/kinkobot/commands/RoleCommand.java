package fr.twizox.kinkobot.commands;

import fr.twizox.kinkobot.utils.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class RoleCommand extends AbstractCommand {

    public RoleCommand() {
        super("role", "Give a role to a user");
        super.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        Button pirate = Button.primary("pirate", Emoji.fromCustom("Pirate", 1015405399672901714l, false));
        Button rebelle = Button.primary("rebelle", Emoji.fromCustom("Rebelle", 1015405411295301773l, false));
        Button marine = Button.primary("marine", Emoji.fromCustom("Marine", 1015405389765955584l, false));

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder()
                .setActionRow(pirate, rebelle, marine)
                .setEmbeds(new EmbedBuilder()
                        .setTitle("Choisi ton Camp")
                        .setColor(Colors.NICE_BLUE)
                        .setDescription("\n\n <:pirate:997397421103517718> **Pirate**: hors la loi\n\n <:marine:995254664318689332> **Marine**: justice suprême\n\n <:rebelle:997397423104200824> **Rebelle**: renverser la Marine !\n\n> *Clique sur le bouton correspondant à ton camp pour obtenir le rôle associé !*")
                        .setThumbnail(event.getGuild().getIconUrl()).build());

        event.getChannel().sendMessage(messageCreateBuilder.build()).queue();
        event.reply("C'est bon").setEphemeral(true).queue();
    }

}
