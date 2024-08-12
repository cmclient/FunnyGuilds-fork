package net.dzikoysk.funnyguilds.feature.command.admin;

import net.dzikoysk.funnycommands.stereotypes.FunnyCommand;
import net.dzikoysk.funnyguilds.event.SimpleEventHandler;
import net.dzikoysk.funnyguilds.event.guild.GuildLivesChangeEvent;
import net.dzikoysk.funnyguilds.feature.command.AbstractFunnyCommand;
import net.dzikoysk.funnyguilds.feature.command.GuildValidation;
import net.dzikoysk.funnyguilds.feature.command.InternalValidationException;
import net.dzikoysk.funnyguilds.guild.Guild;
import net.dzikoysk.funnyguilds.shared.FunnyFormatter;
import net.dzikoysk.funnyguilds.user.User;
import org.bukkit.command.CommandSender;
import panda.std.Option;
import static net.dzikoysk.funnyguilds.feature.command.DefaultValidation.when;

public final class LivesCommand extends AbstractFunnyCommand {

    @FunnyCommand(
            name = "${admin.lives.name}",
            permission = "funnyguilds.admin",
            completer = "guilds:3",
            acceptsExceeded = true
    )
    public void execute(CommandSender sender, String[] args) {
        when(args.length < 1, config -> config.generalNoTagGiven);
        when(args.length < 2, config -> config.adminNoLivesGiven);

        Guild guild = GuildValidation.requireGuildByTag(args[0]);
        int lives = Option.attempt(NumberFormatException.class, () -> Integer.parseInt(args[1])).orThrow(() -> {
            return new InternalValidationException(config -> config.adminErrorInNumber, FunnyFormatter.of("{ERROR}", args[0]));
        });

        User admin = AdminUtils.getAdminUser(sender);
        if (!SimpleEventHandler.handle(new GuildLivesChangeEvent(AdminUtils.getCause(admin), admin, guild, lives))) {
            return;
        }

        guild.setLives(lives);

        FunnyFormatter formatter = new FunnyFormatter()
                .register("{GUILD}", guild.getTag())
                .register("{LIVES}", lives);

        this.messageService.getMessage(config -> config.adminLivesChanged)
                .with(formatter)
                .receiver(sender)
                .send();
    }

}
