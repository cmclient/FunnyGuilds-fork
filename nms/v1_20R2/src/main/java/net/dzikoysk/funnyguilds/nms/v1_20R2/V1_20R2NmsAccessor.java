package net.dzikoysk.funnyguilds.nms.v1_20R2;

import net.dzikoysk.funnyguilds.nms.api.NmsAccessor;
import net.dzikoysk.funnyguilds.nms.api.entity.EntityAccessor;
import net.dzikoysk.funnyguilds.nms.api.message.MessageAccessor;
import net.dzikoysk.funnyguilds.nms.api.packet.PacketAccessor;
import net.dzikoysk.funnyguilds.nms.api.playerlist.PlayerListAccessor;
import net.dzikoysk.funnyguilds.nms.api.statistics.StatisticsAccessor;
import net.dzikoysk.funnyguilds.nms.v1_20R2.entity.V1_20R2EntityAccessor;
import net.dzikoysk.funnyguilds.nms.v1_20R2.message.V1_20R2MessageAccessor;
import net.dzikoysk.funnyguilds.nms.v1_20R2.packet.V1_20R2PacketAccessor;
import net.dzikoysk.funnyguilds.nms.v1_20R2.playerlist.V1_20R2PlayerListAccessor;
import net.dzikoysk.funnyguilds.nms.v1_20R2.statistics.V1_20R2StatisticsAccessor;

public class V1_20R2NmsAccessor implements NmsAccessor {

    @Override
    public PacketAccessor getPacketAccessor() {
        return new V1_20R2PacketAccessor();
    }

    @Override
    public PlayerListAccessor getPlayerListAccessor() {
        return new V1_20R2PlayerListAccessor();
    }

    @Override
    public StatisticsAccessor getStatisticsAccessor() {
        return new V1_20R2StatisticsAccessor();
    }

    @Override
    public EntityAccessor getEntityAccessor() {
        return new V1_20R2EntityAccessor();
    }

    @Override
    public MessageAccessor getMessageAccessor() {
        return new V1_20R2MessageAccessor();
    }

}
