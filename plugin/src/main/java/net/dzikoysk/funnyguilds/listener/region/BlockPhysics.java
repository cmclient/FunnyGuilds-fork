package net.dzikoysk.funnyguilds.listener.region;

import net.dzikoysk.funnyguilds.listener.AbstractFunnyListener;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockPhysics extends AbstractFunnyListener {

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {
        if (this.regionManager.isGuildHeart(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFall(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK && this.regionManager.isGuildHeart(event.getBlock())) {
            event.setCancelled(true);
        }
    }

}
