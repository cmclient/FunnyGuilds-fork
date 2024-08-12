package net.dzikoysk.funnyguilds.nms.v1_8R3.packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.lang.reflect.Field;
import net.dzikoysk.funnyguilds.nms.api.entity.FakeEntity;
import net.dzikoysk.funnyguilds.nms.api.packet.FunnyGuildsOutboundChannelHandler;
import net.dzikoysk.funnyguilds.nms.api.packet.PacketSuppliersRegistry;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunkBulk;

public class V1_8R3FunnyGuildsOutboundChannelHandler extends ChannelOutboundHandlerAdapter implements FunnyGuildsOutboundChannelHandler {

    private static final Field CHUNK_X_FIELD;
    private static final Field CHUNK_Z_FIELD;
    private static final Field CHUNK_X_BULK_FIELD;
    private static final Field CHUNK_Z_BULK_FIELD;

    static {
        try {
            CHUNK_X_FIELD = PacketPlayOutMapChunk.class.getDeclaredField("a");
            CHUNK_X_FIELD.setAccessible(true);
            CHUNK_Z_FIELD = PacketPlayOutMapChunk.class.getDeclaredField("b");
            CHUNK_Z_FIELD.setAccessible(true);
            CHUNK_X_BULK_FIELD = PacketPlayOutMapChunkBulk.class.getDeclaredField("a");
            CHUNK_X_BULK_FIELD.setAccessible(true);
            CHUNK_Z_BULK_FIELD = PacketPlayOutMapChunkBulk.class.getDeclaredField("b");
            CHUNK_Z_BULK_FIELD.setAccessible(true);
        }
        catch (NoSuchFieldException exception) {
            throw new RuntimeException("Failed to initialise V1_8R3FunnyGuildsOutboundChannelHandler", exception);
        }
    }

    private final PacketSuppliersRegistry packetSuppliersRegistry = new PacketSuppliersRegistry();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof PacketPlayOutMapChunk) {
            PacketPlayOutMapChunk chunkPacket = (PacketPlayOutMapChunk) msg;

            int chunkX = (int) CHUNK_X_FIELD.get(chunkPacket);
            int chunkZ = (int) CHUNK_Z_FIELD.get(chunkPacket);

            for (FakeEntity fakeEntity : this.packetSuppliersRegistry.supplyFakeEntities(chunkX, chunkZ)) {
                ctx.write(fakeEntity.getSpawnPacket());
            }
        }
        else if (msg instanceof PacketPlayOutMapChunkBulk) {
            PacketPlayOutMapChunkBulk chunkPacket = (PacketPlayOutMapChunkBulk) msg;

            int[] chunksX = (int[]) CHUNK_X_BULK_FIELD.get(chunkPacket);
            int[] chunksZ = (int[]) CHUNK_Z_BULK_FIELD.get(chunkPacket);

            for (int i = 0; i < chunksX.length; i++) {
                for (FakeEntity fakeEntity : this.packetSuppliersRegistry.supplyFakeEntities(chunksX[i], chunksZ[i])) {
                    ctx.write(fakeEntity.getSpawnPacket());
                }
            }
        }

        super.write(ctx, msg, promise);
    }

    @Override
    public PacketSuppliersRegistry getPacketSuppliersRegistry() {
        return this.packetSuppliersRegistry;
    }

}
