package net.dzikoysk.funnyguilds.listener;

import net.dzikoysk.funnyguilds.feature.ban.BanUtils;
import net.dzikoysk.funnyguilds.shared.FunnyValidator;
import net.dzikoysk.funnyguilds.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class PlayerLogin extends AbstractFunnyListener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult() != Result.ALLOWED) {
            return;
        }

        Player player = event.getPlayer();
        String name = player.getName();
        switch (FunnyValidator.validateUsername(this.config, name)) {
            case TOO_SHORT:
                event.disallow(Result.KICK_OTHER, this.messageService.get(player, config -> config.loginNickTooShort));
                break;
            case TOO_LONG:
                event.disallow(Result.KICK_OTHER, this.messageService.get(player, config -> config.loginNickTooLong));
                break;
            case INVALID:
                event.disallow(Result.KICK_OTHER, this.messageService.get(player, config -> config.loginNickInvalid));
                break;
            case VALID:
                break;
        }

        this.userManager.findByPlayer(player)
                .peek(BanUtils::checkIfBanShouldExpire)
                .filter(User::isBanned)
                .peek(user -> event.disallow(Result.KICK_BANNED, BanUtils.getBanMessage(user)));
    }

}
