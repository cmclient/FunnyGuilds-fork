package net.dzikoysk.funnyguilds.event.guild.member;

import net.dzikoysk.funnyguilds.guild.Guild;
import net.dzikoysk.funnyguilds.user.User;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildMemberKickEvent extends GuildMemberEvent {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GuildMemberKickEvent(EventCause eventCause, User doer, Guild guild, User member) {
        super(eventCause, doer, guild, member);
    }

    @Override
    public String getDefaultCancelMessage() {
        return "[FunnyGuilds] Member kick has been cancelled by the server!";
    }

}
