package net.dzikoysk.funnyguilds.nms.v1_8R3.message;

import net.dzikoysk.funnyguilds.nms.api.message.MessageAccessor;
import net.dzikoysk.funnyguilds.nms.api.message.TitleMessage;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class V1_8R3MessageAccessor implements MessageAccessor {

    @Override
    public void sendTitleMessage(TitleMessage titleMessage, Player... players) {
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(
                EnumTitleAction.TITLE,
                CraftChatMessage.fromString(titleMessage.getText(), false)[0]
        );
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(
                EnumTitleAction.SUBTITLE,
                CraftChatMessage.fromString(titleMessage.getSubText(), false)[0]
        );
        PacketPlayOutTitle timesPacket = new PacketPlayOutTitle(
                titleMessage.getFadeInDuration(),
                titleMessage.getStayDuration(),
                titleMessage.getFadeOutDuration()
        );

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitlePacket);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(timesPacket);
        }
    }

    @Override
    public void sendActionBarMessage(String text, Player... players) {
        PacketPlayOutChat actionBarPacket = new PacketPlayOutChat(
                ChatSerializer.a("{\"text\":\"" + text + "\"}"),
                (byte) 2
        );

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionBarPacket);
        }
    }

}
