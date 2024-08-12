package net.dzikoysk.funnyguilds.shared;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerializeUtils {

    private SerializeUtils() {
    }

    @Nullable
    public static String serialize(@NotNull ItemStack[] items) {
        if (items == null)
            return null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException ex) {
            FunnyGuilds.getPluginLogger().error("Failed to serialize vault contents!", ex);
            return null;
        }
    }

    @Nullable
    public static ItemStack[] deserialize(@Nullable String data) {
        if (FunnyStringUtils.isEmpty(data))
            return null;

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return items;
        } catch (IOException | ClassNotFoundException ex) {
            FunnyGuilds.getPluginLogger().error("Failed to deserialize vault contents!", ex);
            return null;
        }
    }

}
