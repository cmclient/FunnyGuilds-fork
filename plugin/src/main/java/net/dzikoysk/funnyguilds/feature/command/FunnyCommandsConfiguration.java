package net.dzikoysk.funnyguilds.feature.command;

import net.dzikoysk.funnycommands.FunnyCommands;
import net.dzikoysk.funnycommands.resources.types.PlayerType;
import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.config.PluginConfiguration;
import net.dzikoysk.funnyguilds.config.sections.CommandsConfiguration;
import net.dzikoysk.funnyguilds.feature.command.admin.*;
import net.dzikoysk.funnyguilds.feature.command.user.*;
import net.dzikoysk.funnyguilds.guild.GuildManager;
import net.dzikoysk.funnyguilds.user.UserManager;
import org.bukkit.Server;
import panda.utilities.text.Joiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class FunnyCommandsConfiguration {

    private FunnyCommandsConfiguration() {
    }

    public static FunnyCommands createFunnyCommands(FunnyGuilds plugin) {
        Server server = plugin.getServer();

        PluginConfiguration config = plugin.getPluginConfiguration();

        CommandsConfiguration commands = config.commands;
        CommandsConfiguration.FunnyCommand enlargeCommand = commands.enlarge;
        enlargeCommand.enabled = enlargeCommand.enabled && !config.enlargeItems.isEmpty();

        UserManager userManager = plugin.getUserManager();
        GuildManager guildManager = plugin.getGuildManager();

        CommandComponents userCommands = new CommandComponents("user")
                .command("ally", commands.ally, AllyCommand.class)
                .command("base", commands.base, BaseCommand.class)
                .command("break", commands.break_, BreakCommand.class)
                .command("confirm", commands.confirm, ConfirmCommand.class)
                .command("create", commands.create, CreateCommand.class)
                .command("delete", commands.delete, DeleteCommand.class)
                .command("deputy", commands.deputy, DeputyCommand.class)
                .command("enlarge", enlargeCommand, EnlargeCommand.class)
                .command("escape", commands.escape, EscapeCommand.class)
                .command("funnyguilds", commands.funnyguilds, FunnyGuildsCommand.class)
                .command("guild", commands.guild, GuildCommand.class)
                .command("info", commands.info, InfoCommand.class)
                .command("invite", commands.invite, InviteCommand.class)
                .command("items", commands.items, ItemsCommand.class)
                .command("join", commands.join, JoinCommand.class)
                .command("kick", commands.kick, KickCommand.class)
                .command("leader", commands.leader, LeaderCommand.class)
                .command("leave", commands.leave, LeaveCommand.class)
                .command("player", commands.player, PlayerInfoCommand.class)
                .command("pvp", commands.pvp, PvPCommand.class)
                .command("ranking", commands.ranking, RankingCommand.class)
                .command("rank-reset", commands.rankReset, RankResetCommand.class)
                .command("stats-reset", commands.statsReset, StatsResetCommand.class)
                .command("set-base", commands.setbase, SetBaseCommand.class)
                .command("top", commands.top, TopCommand.class)
                .command("validity", commands.validity, ValidityCommand.class)
                .command("war", commands.war, WarCommand.class)
                .command("tnt", commands.tnt, TntCommand.class)
                .command("vault", commands.vault, VaultCommand.class);

        CommandComponents adminCommands = new CommandComponents("admin")
                .command("add", commands.admin.add, AddCommand.class)
                .command("assists", commands.admin.assists, AssistsCommand.class)
                .command("base", commands.admin.base, BaseAdminCommand.class)
                .command("ban", commands.admin.ban, BanCommand.class)
                .command("deaths", commands.admin.deaths, DeathsCommand.class)
                .command("delete", commands.admin.delete, DeleteAdminCommand.class)
                .command("deputy", commands.admin.deputy, DeputyAdminCommand.class)
                .command("guilds-enabled", commands.admin.enabled, GuildsEnabledCommand.class)
                .command("kick", commands.admin.kick, KickAdminCommand.class)
                .command("kills", commands.admin.kills, KillsCommand.class)
                .command("leader", commands.admin.leader, LeaderAdminCommand.class)
                .command("lives", commands.admin.lives, LivesCommand.class)
                .command("logouts", commands.admin.logouts, LogoutsCommand.class)
                .command("main", commands.admin.main, MainCommand.class)
                .command("move", commands.admin.move, MoveCommand.class)
                .command("name", commands.admin.name, NameCommand.class)
                .command("points", commands.admin.points, PointsCommand.class)
                .command("protection", commands.admin.protection, ProtectionCommand.class)
                .command("spy", commands.admin.spy, SpyCommand.class)
                .command("tag", commands.admin.tag, TagCommand.class)
                .command("teleport", commands.admin.teleport, TeleportCommand.class)
                .command("unban", commands.admin.unban, UnbanCommand.class)
                .command("validity", commands.admin.validity, ValidityAdminCommand.class);

        return FunnyCommands.configuration(() -> plugin)
                .registerDefaultComponents()
                .placeholders(userCommands.placeholders)
                .placeholders(adminCommands.placeholders)
                .injector(plugin.getInjector().fork(resources -> {}))
                .bind(new UserBind(userManager))
                .bind(new GuildBind(userManager))
                .type(new PlayerType(server))
                .completer(new MembersCompleter(userManager))
                .completer(new GuildsCompleter(guildManager))
                .completer(new AlliesCompleter(userManager))
                .completer(new GuildInvitationsCompleter(userManager, plugin.getGuildInvitationList()))
                .completer(new InvitePlayersCompleter(config, userManager))
                .validator(new MemberValidator())
                .validator(new ManageValidator())
                .validator(new OwnerValidator())
                .commands(userCommands.commands)
                .commands(adminCommands.commands)
                .exceptionHandler(new InternalValidationExceptionHandler(plugin.getMessageService()))
                .exceptionHandler(new FunnyGuildsExceptionHandler(FunnyGuilds.getPluginLogger()))
                .install();
    }

    private static final class CommandComponents {

        private final String group;
        private final Map<String, Function<String, String>> placeholders = new HashMap<>();
        private final List<Class<?>> commands = new ArrayList<>();

        private CommandComponents(String group) {
            this.group = group;
        }

        private CommandComponents command(String name, CommandsConfiguration.FunnyCommand configuration, Class<?> command) {
            if (configuration.enabled) {
                this.placeholders.put(this.group + "." + name + ".name", key -> configuration.name);
                this.placeholders.put(this.group + "." + name + ".aliases", key -> Joiner.on(", ").join(configuration.aliases).toString());
                this.placeholders.put(this.group + "." + name + ".description", key -> "");
                this.commands.add(command);
            }

            return this;
        }

        private CommandComponents command(String name, String alias, Class<?> command) {
            this.placeholders.put(this.group + "." + name + ".name", key -> alias);
            this.commands.add(command);
            return this;
        }

    }

}
