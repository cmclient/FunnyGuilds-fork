package net.dzikoysk.funnyguilds.listener.region;

import java.util.EnumSet;
import java.util.Set;
import net.dzikoysk.funnyguilds.listener.AbstractFunnyListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityProtect extends AbstractFunnyListener {

    private static final Set<EntityType> IS_NOT_MOB = EnumSet.of(EntityType.ARMOR_STAND, EntityType.PLAYER);

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!this.config.explodeShouldAffectOnlyGuild) {
            return;
        }

        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity && !IS_NOT_MOB.contains(entity.getType()))) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            return;
        }

        event.setCancelled(true);
    }

}
