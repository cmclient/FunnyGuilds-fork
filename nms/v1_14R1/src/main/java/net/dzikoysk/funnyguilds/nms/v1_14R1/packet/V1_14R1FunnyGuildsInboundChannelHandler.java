package net.dzikoysk.funnyguilds.nms.v1_14R1.packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.Field;
import net.dzikoysk.funnyguilds.nms.api.packet.FunnyGuildsInboundChannelHandler;
import net.dzikoysk.funnyguilds.nms.api.packet.PacketCallbacksRegistry;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.PacketPlayInUseEntity;

public class V1_14R1FunnyGuildsInboundChannelHandler extends ChannelInboundHandlerAdapter implements FunnyGuildsInboundChannelHandler {

    private static final Field ENTITY_ID;

    static {
        try {
            ENTITY_ID = PacketPlayInUseEntity.class.getDeclaredField("a");
            ENTITY_ID.setAccessible(true);

        }
        catch (NoSuchFieldException exception) {
            throw new RuntimeException("Failed to initialise V1_14R1FunnyGuildsChannelHandler", exception);
        }
    }

    private final PacketCallbacksRegistry packetCallbacksRegistry = new PacketCallbacksRegistry();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) msg;
            int entityId = (int) ENTITY_ID.get(packetPlayInUseEntity);

            PacketPlayInUseEntity.EnumEntityUseAction action = packetPlayInUseEntity.b();
            if (action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                this.packetCallbacksRegistry.handleAttackEntity(entityId, true);
            }
            else if (action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
                boolean isMainHand = packetPlayInUseEntity.c() == EnumHand.MAIN_HAND;
                this.packetCallbacksRegistry.handleRightClickEntity(entityId, isMainHand);
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public PacketCallbacksRegistry getPacketCallbacksRegistry() {
        return this.packetCallbacksRegistry;
    }

}
