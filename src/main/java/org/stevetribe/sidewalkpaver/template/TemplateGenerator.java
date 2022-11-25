package org.stevetribe.sidewalkpaver.template;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.LinkedList;

public class TemplateGenerator {
    public static Template generateTemplateByTheBottomBlock(Location location) {
        LinkedList<Location> bfs = new LinkedList<>();
        Template template = new Template();

        bfs.add(location.clone());
        template.addBlockToTemplate(location.clone(), location.getBlock().getType());

        while(bfs.size() != 0) {
            // maintain the linked list
            Location curLoc = bfs.getFirst();
            bfs.removeFirst();

            double x = curLoc.getX();
            double y = curLoc.getY();
            double z = curLoc.getZ();
            for(int xOffset = -1; xOffset <= 1; xOffset++) {    // go up
                for(int zOffset = -1; zOffset <= 1; zOffset++) {
                    for(int yOffset = 0; yOffset <= 1; yOffset++) {    // height first
                        Location potentialLocation = new Location(location.getWorld(), x + xOffset, y + yOffset, z + zOffset);
                        Location relativeLocation = new Location(location.getWorld(),
                                potentialLocation.getX() - location.getX(),
                                potentialLocation.getY() - location.getY(),
                                potentialLocation.getZ() - location.getZ());

                        if(!potentialLocation.getBlock().getType().equals(Material.AIR) &&
                                !template.isLocationExist(relativeLocation)) {
                            if(Math.abs(potentialLocation.getX() - location.getX()) > 10 ||
                                    Math.abs(potentialLocation.getY() - location.getY()) > 50 ||
                                    Math.abs(potentialLocation.getZ() - location.getZ()) > 10) {
                                return template;
                            }

                            template.addBlockToTemplate(relativeLocation.clone(), potentialLocation.getBlock().getType());
                            bfs.addLast(potentialLocation.clone());
                        }
                    }
                }
            }
        }
        return template;
    }
}
