package net.dzikoysk.funnyguilds.feature.gui;

import net.dzikoysk.funnyguilds.listener.AbstractFunnyListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiActionHandler extends AbstractFunnyListener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.CHEST) {
            return;
        }

        InventoryHolder inventoryHolder = inventory.getHolder();
        if (!(inventoryHolder instanceof FunnyHolder)) {
            return;
        }

        FunnyHolder funnyHolder = (FunnyHolder) inventoryHolder;
        funnyHolder.handleClick(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.CHEST) {
            return;
        }

        InventoryHolder inventoryHolder = inventory.getHolder();
        if (!(inventoryHolder instanceof FunnyHolder)) {
            return;
        }

        FunnyHolder funnyHolder = (FunnyHolder) inventoryHolder;
        funnyHolder.handleClose(event);
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.CHEST) {
            return;
        }

        InventoryHolder inventoryHolder = inventory.getHolder();
        if (!(inventoryHolder instanceof FunnyHolder)) {
            return;
        }

        FunnyHolder funnyHolder = (FunnyHolder) inventory;
        if (!funnyHolder.isAllowClick()) {
            event.setCancelled(true);
        }
    }

}
