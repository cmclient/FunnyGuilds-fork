package net.dzikoysk.funnyguilds.feature.command.admin;

import net.dzikoysk.funnycommands.stereotypes.FunnyCommand;
import net.dzikoysk.funnyguilds.event.SimpleEventHandler;
import net.dzikoysk.funnyguilds.event.guild.member.GuildMemberJoinEvent;
import net.dzikoysk.funnyguilds.feature.command.AbstractFunnyCommand;
import net.dzikoysk.funnyguilds.feature.command.GuildValidation;
import net.dzikoysk.funnyguilds.feature.command.UserValidation;
import net.dzikoysk.funnyguilds.feature.scoreboard.ScoreboardGlobalUpdateUserSyncTask;
import net.dzikoysk.funnyguilds.guild.Guild;
import net.dzikoysk.funnyguilds.shared.FunnyFormatter;
import net.dzikoysk.funnyguilds.user.User;
import org.bukkit.command.CommandSender;

import static net.dzikoysk.funnyguilds.feature.command.DefaultValidation.when;

public final class AddCommand extends AbstractFunnyCommand {

    @FunnyCommand(
            name = "${admin.add.name}",
            permission = "funnyguilds.admin",
            completer = "guilds:3 online-players:3",
            acceptsExceeded = true,
            playerOnly = true
    )
    public void execute(CommandSender sender, String[] args) {
        when(args.length < 1, config -> config.generalNoTagGiven);
        when(!this.guildManager.tagExists(args[0]), config -> config.generalNoGuildFound);
        when(args.length < 2, config -> config.generalNoNickGiven);

        User userToAdd = UserValidation.requireUserByName(args[1]);
        when(userToAdd.hasGuild(), config -> config.generalUserHasGuild);

        Guild guild = GuildValidation.requireGuildByTag(args[0]);
        User admin = AdminUtils.getAdminUser(sender);

        if (!SimpleEventHandler.handle(new GuildMemberJoinEvent(AdminUtils.getCause(admin), admin, guild, userToAdd))) {
            return;
        }

        guild.addMember(userToAdd);
        userToAdd.setGuild(guild);
        this.plugin.getIndividualNameTagManager()
                .map(manager -> new ScoreboardGlobalUpdateUserSyncTask(manager, userToAdd))
                .peek(this.plugin::scheduleFunnyTasks);

        FunnyFormatter formatter = new FunnyFormatter()
                .register("{GUILD}", guild.getName())
                .register("{TAG}", guild.getTag())
                .register("{PLAYER}", userToAdd.getName());

        this.messageService.getMessage(config -> config.joinToMember)
                .receiver(userToAdd)
                .with(formatter)
                .send();
        this.messageService.getMessage(config -> config.joinToOwner)
                .receiver(guild.getOwner())
                .with(formatter)
                .send();
        this.messageService.getMessage(config -> config.broadcastJoin)
                .broadcast()
                .with(formatter)
                .send();
    }

}
