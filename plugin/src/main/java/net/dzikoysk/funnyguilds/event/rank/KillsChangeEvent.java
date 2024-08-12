package net.dzikoysk.funnyguilds.event.rank;

import net.dzikoysk.funnyguilds.user.User;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class KillsChangeEvent extends AbstractRankEvent {

    private static final HandlerList handlers = new HandlerList();
    private int killsChange;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public KillsChangeEvent(EventCause eventCause, User doer, User affected, int killsChange) {
        super(eventCause, doer, affected);
        this.killsChange = killsChange;
    }

    public int getKillsChange() {
        return this.killsChange;
    }

    public void setKillsChange(int killsChange) {
        this.killsChange = killsChange;
    }

    @Override
    public String getDefaultCancelMessage() {
        return "[FunnyGuilds] Kills change has been cancelled by the server!";
    }

}
