package net.dzikoysk.funnyguilds.nms.v1_19R3;

import net.dzikoysk.funnyguilds.nms.api.NmsAccessor;
import net.dzikoysk.funnyguilds.nms.api.entity.EntityAccessor;
import net.dzikoysk.funnyguilds.nms.api.message.MessageAccessor;
import net.dzikoysk.funnyguilds.nms.api.packet.PacketAccessor;
import net.dzikoysk.funnyguilds.nms.api.playerlist.PlayerListAccessor;
import net.dzikoysk.funnyguilds.nms.api.statistics.StatisticsAccessor;
import net.dzikoysk.funnyguilds.nms.v1_19R3.entity.V1_19R3EntityAccessor;
import net.dzikoysk.funnyguilds.nms.v1_19R3.message.V1_19R3MessageAccessor;
import net.dzikoysk.funnyguilds.nms.v1_19R3.packet.V1_19R3PacketAccessor;
import net.dzikoysk.funnyguilds.nms.v1_19R3.playerlist.V1_19R3PlayerListAccessor;
import net.dzikoysk.funnyguilds.nms.v1_19R3.statistics.V1_19R3StatisticsAccessor;

public class V1_19R3NmsAccessor implements NmsAccessor {

    @Override
    public PacketAccessor getPacketAccessor() {
        return new V1_19R3PacketAccessor();
    }

    @Override
    public PlayerListAccessor getPlayerListAccessor() {
        return new V1_19R3PlayerListAccessor();
    }

    @Override
    public StatisticsAccessor getStatisticsAccessor() {
        return new V1_19R3StatisticsAccessor();
    }

    @Override
    public EntityAccessor getEntityAccessor() {
        return new V1_19R3EntityAccessor();
    }

    @Override
    public MessageAccessor getMessageAccessor() {
        return new V1_19R3MessageAccessor();
    }

}
