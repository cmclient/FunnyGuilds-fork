package net.dzikoysk.funnyguilds.feature.command.user;

import net.dzikoysk.funnycommands.stereotypes.FunnyCommand;
import net.dzikoysk.funnycommands.stereotypes.FunnyComponent;
import net.dzikoysk.funnyguilds.feature.command.AbstractFunnyCommand;
import net.dzikoysk.funnyguilds.feature.gui.GuiWindow;
import net.dzikoysk.funnyguilds.guild.Guild;
import net.dzikoysk.funnyguilds.shared.bukkit.ChatUtils;
import org.bukkit.entity.Player;

@FunnyComponent
public final class VaultCommand extends AbstractFunnyCommand {

    @FunnyCommand(
            name = "${user.vault.name}",
            description = "${user.vault.description}",
            aliases = "${user.vault.aliases}",
            permission = "funnyguilds.vault",
            acceptsExceeded = true,
            playerOnly = true
    )
    public void execute(Player player, Guild guild) {
        if (guild.isVaultOpened()) {
            player.sendMessage(ChatUtils.colored(" &8» &cSkarbiec moze byc otwarty tylko przez jedna osobe w tym samym czasie."));
            return;
        }

        GuiWindow gui = new GuiWindow(ChatUtils.colored("&8» &aSkarbiec gildii &8[&c" + guild.getTag() + "&8]"), 6, event -> {
            guild.setVault(event.getInventory().getContents());
            guild.setVaultOpened(false);
            guild.markChanged();
        });
        gui.setContents(guild.getVault());
        gui.open(player);
        guild.setVaultOpened(true);
    }

}
