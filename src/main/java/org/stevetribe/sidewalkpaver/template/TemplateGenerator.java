package org.stevetribe.sidewalkpaver.template;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.LinkedList;

public class TemplateGenerator {
    public static Template generateTemplateByTheBottomBlock(Location location) {
        LinkedList<Location> bfs = new LinkedList<>();
        Template template = new Template();

        bfs.add(location.clone());

        while (bfs.size() != 0) {
            // maintain the linked list
            Location curLoc = bfs.getFirst();
            bfs.removeFirst();

            double x = curLoc.getX();
            double y = curLoc.getY();
            double z = curLoc.getZ();
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    for (int yOffset = -1; yOffset <= 1; yOffset++) {
                        Location potentialLocation = new Location(location.getWorld(), x + xOffset, y + yOffset, z + zOffset);
                        if (potentialLocation.getY() < location.getY()) {
                            continue;
                        }

                        Location relativeLocation = new Location(location.getWorld(),
                                potentialLocation.getX() - location.getX(),
                                potentialLocation.getY() - location.getY(),
                                potentialLocation.getZ() - location.getZ());

                        if (!potentialLocation.getBlock().getType().equals(Material.AIR) &&
                                !template.isLocationExist(relativeLocation)) {
                            if (Math.abs(potentialLocation.getX() - location.getX()) > 10 &&
                                    Math.abs(potentialLocation.getY() - location.getY()) > 50 &&
                                    Math.abs(potentialLocation.getZ() - location.getZ()) > 10) {
                                return template;
                            }
                            if (Math.abs(potentialLocation.getX() - location.getX()) > 10 ||
                                    Math.abs(potentialLocation.getY() - location.getY()) > 50 ||
                                    Math.abs(potentialLocation.getZ() - location.getZ()) > 10) {
                                continue;
                            }

                            template.addBlock(relativeLocation.clone(), potentialLocation.getBlock());
                            bfs.addLast(potentialLocation.clone());
                        }
                    }
                }
            }
        }
        return template;
    }
}
