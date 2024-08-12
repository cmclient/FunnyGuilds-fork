package net.dzikoysk.funnyguilds.feature.command.admin;

import net.dzikoysk.funnycommands.stereotypes.FunnyCommand;
import net.dzikoysk.funnyguilds.event.SimpleEventHandler;
import net.dzikoysk.funnyguilds.event.rank.LogoutsChangeEvent;
import net.dzikoysk.funnyguilds.feature.command.AbstractFunnyCommand;
import net.dzikoysk.funnyguilds.feature.command.InternalValidationException;
import net.dzikoysk.funnyguilds.feature.command.UserValidation;
import net.dzikoysk.funnyguilds.shared.FunnyFormatter;
import net.dzikoysk.funnyguilds.user.User;
import net.dzikoysk.funnyguilds.user.UserRank;
import org.bukkit.command.CommandSender;
import panda.std.Option;
import static net.dzikoysk.funnyguilds.feature.command.DefaultValidation.when;

public final class LogoutsCommand extends AbstractFunnyCommand {

    @FunnyCommand(
            name = "${admin.logouts.name}",
            permission = "funnyguilds.admin",
            completer = "guilds:3",
            acceptsExceeded = true
    )
    public void execute(CommandSender sender, String[] args) {
        when(args.length < 1, config -> config.generalNoNickGiven);
        when(args.length < 2, config -> config.adminNoLogoutsGiven);

        int logouts = Option.attempt(NumberFormatException.class, () -> Integer.parseInt(args[1])).orThrow(() -> {
            return new InternalValidationException(config -> config.adminErrorInNumber, FunnyFormatter.of("{ERROR}", args[0]));
        });

        User admin = AdminUtils.getAdminUser(sender);
        User user = UserValidation.requireUserByName(args[0]);

        UserRank userRank = user.getRank();
        int change = logouts - userRank.getLogouts();

        LogoutsChangeEvent logoutsChangeEvent = new LogoutsChangeEvent(AdminUtils.getCause(admin), admin, user, change);
        if (!SimpleEventHandler.handle(logoutsChangeEvent)) {
            return;
        }

        int finalLogouts = logouts + logoutsChangeEvent.getLogoutsChange();
        user.getRank().setLogouts(finalLogouts);

        FunnyFormatter formatter = new FunnyFormatter()
                .register("{PLAYER}", user.getName())
                .register("{LOGOUTS}", finalLogouts);

        this.messageService.getMessage(config -> config.adminLogoutsChanged)
                .receiver(sender)
                .with(formatter)
                .send();
    }

}
