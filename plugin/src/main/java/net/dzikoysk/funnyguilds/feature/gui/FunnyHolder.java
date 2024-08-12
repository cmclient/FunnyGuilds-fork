package net.dzikoysk.funnyguilds.feature.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FunnyHolder implements InventoryHolder {

    private final GuiWindow guiWindow;
    private final Map<Integer, Consumer<InventoryClickEvent>> actions;
    private Consumer<InventoryCloseEvent> actionOnClose;
    private Inventory inventory;
    private boolean allowClick;

    public FunnyHolder(GuiWindow guiWindow) {
        this.guiWindow = guiWindow;
        this.actions = new HashMap<>();
    }

    public void handleClick(InventoryClickEvent event) {
        this.actions.getOrDefault(event.getRawSlot(), this.allowClick ? e -> {} : e -> e.setCancelled(true)).accept(event);
    }

    public void handleClose(InventoryCloseEvent event) {
        if (this.actionOnClose != null) {
            this.actionOnClose.accept(event);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public GuiWindow getGuiWindow() {
        return this.guiWindow;
    }

    public void setActionOnSlot(Integer slot, Consumer<InventoryClickEvent> consumer) {
        this.actions.put(slot, consumer != null ? consumer : event -> {});
    }

    public void setActionOnClose(Consumer<InventoryCloseEvent> actionOnClose) {
        this.actionOnClose = actionOnClose;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public boolean isAllowClick() {
        return this.allowClick;
    }

    public void setAllowClick(boolean allowClick) {
        this.allowClick = allowClick;
    }

}
