package net.dzikoysk.funnyguilds.feature.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GuiWindow {

    private final Inventory inv;
    private final FunnyHolder holder;

    public GuiWindow(String name, int rows) {
        this.holder = new FunnyHolder(this);
        this.inv = Bukkit.createInventory(this.holder, rows > 6 ? 6 * 9 : rows * 9, name);
        this.holder.setInventory(this.inv);
    }

    public GuiWindow(String name, int rows, Consumer<InventoryCloseEvent> actionOnClose) {
        this(name, rows);
        this.holder.setActionOnClose(actionOnClose);
        this.holder.setAllowClick(true);
    }

    public void setContents(ItemStack... contents) {
        this.inv.setContents(contents);
    }

    public void setItem(int slot, ItemStack item) {
        this.inv.setItem(slot, item);
    }

    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> consumer) {
        this.holder.setActionOnSlot(slot, consumer);
        this.inv.setItem(slot, item);
    }

    public void setToNextFree(ItemStack item) {
        this.setToNextFree(item, 0);
    }

    public void setToNextFree(ItemStack item, int start) {
        for (int slot = start; slot < this.inv.getSize(); slot++) {
            if (this.inv.getItem(slot) == null) {
                this.inv.setItem(slot, item);
                break;
            }
        }
    }

    public void open(HumanEntity entity) {
        entity.openInventory(this.inv);
    }

    //TODO: Use this method in the future. (Add ItemStack to configuration for fill inventory)
    public void fillEmpty(ItemStack itemStack) {
        for (int slot = 0; slot < this.inv.getSize(); slot++) {
            if (this.inv.getItem(slot) == null) {
                this.inv.setItem(slot, itemStack);
            }
        }
    }

}
