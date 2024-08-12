package net.dzikoysk.funnyguilds.shared.bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import panda.std.function.QuadFunction;

public final class SpaceUtils {

    private SpaceUtils() {
    }

    public static boolean chance(double chance) {
        return chance >= 100 || chance > ThreadLocalRandom.current().nextDouble(0, 100);
    }

    public static List<Location> sphere(Location sphereCenter, int radius, int height, boolean hollow, boolean sphere, int plusY) {
        return mapSphereCoordinates(sphereCenter, radius, height, plusY, hollow, sphere, ArrayList::new, Location::new);
    }

    public static List<Block> sphereBlocks(Location sphereLocation, int radius, int height, int plusY, boolean hollow, boolean sphere) {
        return mapSphereCoordinates(sphereLocation, radius, height, plusY, hollow, sphere, ArrayList::new, World::getBlockAt);
    }

    private static <T, C extends Collection<T>> C mapSphereCoordinates(Location sphereCenter, int radius, int height, int plusY,
                                                                       boolean hollow, boolean sphere, Supplier<C> collectionSupplier,
                                                                       QuadFunction<World, Integer, Integer, Integer, T> coordinateMapper) {

        C result = collectionSupplier.get();

        World world = sphereCenter.getWorld();
        int centerX = sphereCenter.getBlockX();
        int centerY = sphereCenter.getBlockY();
        int centerZ = sphereCenter.getBlockZ();

        double radiusSquared = Math.pow(radius, 2.0D);
        double hollowRadiusSquared = Math.pow(radius - 1, 2.0D);

        for (int x = centerX - radius; x <= centerX + radius; x++) {
            double xDistSquared = Math.pow(centerX - x, 2.0D);

            for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                double zDistSquared = Math.pow(centerZ - z, 2.0D);

                for (int y = (sphere ? centerY - radius : centerY); y < (sphere ? centerY + radius : centerY + height); y++) {
                    double yDistSquared = sphere ? Math.pow(centerY - y, 2.0D) : 0.0D;
                    double distSquared = xDistSquared + zDistSquared + yDistSquared;

                    if (distSquared < radiusSquared && !(hollow && distSquared < hollowRadiusSquared)) {
                        result.add(coordinateMapper.apply(world, x, y + plusY, z));
                    }
                }
            }
        }

        return result;
    }

}
