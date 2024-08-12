package net.dzikoysk.funnyguilds.event.guild;

import net.dzikoysk.funnyguilds.guild.Guild;
import net.dzikoysk.funnyguilds.user.User;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildLivesChangeEvent extends GuildEvent {

    private static final HandlerList handlers = new HandlerList();
    private final int newLives;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GuildLivesChangeEvent(EventCause eventCause, User doer, Guild guild, int newLives) {
        super(eventCause, doer, guild);
        this.newLives = newLives;
    }

    public int getNewLives() {
        return this.newLives;
    }

    @Override
    public String getDefaultCancelMessage() {
        return "[FunnyGuilds] Guild lives change has been cancelled by the server!";
    }

}