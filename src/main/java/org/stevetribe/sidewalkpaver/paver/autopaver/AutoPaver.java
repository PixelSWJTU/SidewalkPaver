package org.stevetribe.sidewalkpaver.paver.autopaver;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.stevetribe.sidewalkpaver.paver.pavehistory.PaveEventHistory;
import org.stevetribe.sidewalkpaver.paver.pavehistory.PaveHistories;
import org.stevetribe.sidewalkpaver.template.Template;
import org.stevetribe.sidewalkpaver.template.Templates;

import java.util.Iterator;
import java.util.LinkedList;

public class AutoPaver {
    Player player;

    public AutoPaver(Player player) {
        this.player = player;
    }

    public boolean autoPave(Location locationA, Location locationB, Location direction) {
        locationA = locationA.clone();
        int count = 0;
        LinkedList<Location> sidewalkPath = new LinkedList<>();
        Location preLoc = null;

        while (locationA.getX() != locationB.getX() || locationA.getZ() != locationB.getZ()) {
            double x = locationA.getX();
            double z = locationA.getZ();

            // search for the next sidewalk block, 优化计划：优先向direction反方向搜索
            for(int xOffset = -1; xOffset <= 1; xOffset++) {
                for(int zOffset = -1; zOffset <= 1; zOffset++) {
                    if(count++ > 1000) {        // 避免死循环，在执行超过1000次后强制退出
                        System.out.println("Process finished with exit code 1");
                        return false;
                    }
                    // skip the current block.
                    if(xOffset == 0 && zOffset == 0) {
                        continue;
                    }
                    if(isBlockOnTheSideWalk(locationA, xOffset, zOffset)) {
                        // skip the previous block
                        if(preLoc != null) {
                            if(locationA.getX() + xOffset == preLoc.getX() && locationA.getZ() + zOffset == preLoc.getZ()) {
                                continue;
                            }
                        }
                        preLoc = locationA.clone();
                        locationA.setX(x + xOffset);
                        locationA.setZ(z + zOffset);
                        // jump out the nested for loop, break, break
                        xOffset = 2;
                        zOffset = 2;
                    }
                }
            }
            // check whether there is no result
            if(!isBlockOnTheSideWalk(locationA)) {
                return false;
            }

            // add to the list
            if(preLoc != null) {
                sidewalkPath.add(preLoc.clone());
            }
        }
        sidewalkPath.add(locationB.clone());

        // pave
        Iterator<Location> sidewalkBlock = sidewalkPath.descendingIterator();
        while(sidewalkBlock.hasNext()) {
            this.paveSidewalk(sidewalkBlock.next(), direction);
        }

        return true;
    }

//    static private boolean isBlockAlreadyVisited(LinkedList<Location> locations, Location locationA, int xOffset, int zOffset) {
//        if(locations != null) {
//            Iterator<Location> loc = locations.descendingIterator();
//            Location loc1 = new Location(locationA.getWorld(), locationA.getX() + xOffset, locationA.getY(), locationA.getZ() + zOffset);
//
//            System.out.println("dup check " + "x: " + loc1.getX() + "; z: " + loc1.getZ() + ";" + loc1.getBlock().getType().equals(Material.SMOOTH_STONE_SLAB));
//
//            while(loc.hasNext()) {
//                if(loc.next().equals(loc1)) {
//                    return true;
//                };
//            }
//        }
//        return false;
//    }

    static private boolean isBlockOnTheSideWalk(Location location, double xOffset, double zOffset) {
        Location loc = new Location(location.getWorld(), location.getX() + xOffset, location.getY(),location.getZ() + zOffset);
        return loc.getBlock().getType().equals(Material.SMOOTH_STONE_SLAB);
    }

    static private boolean isBlockOnTheSideWalk(Location loc) {
        return loc.getBlock().getType().equals(Material.SMOOTH_STONE_SLAB);
    }

    private void paveSidewalk(Location loc, Location direction) {
        int xIncrement;
        int zIncrement;
        Location templateLocation = null;
        PaveEventHistory paveEventHistory = new PaveEventHistory();
        PaveEventHistory templateHistory = new PaveEventHistory();

        if(direction.getYaw() > -45 && direction.getYaw() <= 45) {              // positive z
            xIncrement = 0;
            zIncrement = 1;
        } else if(direction.getYaw() > 45 && direction.getYaw() <= 135) {       // negative x
            xIncrement = -1;
            zIncrement = 0;
        } else if(direction.getYaw() > 135 || direction.getYaw() <= -135) {     // negative z
            xIncrement = 0;
            zIncrement = -1;
        } else {                                                                // positive x
            xIncrement = 1;
            zIncrement = 0;
        }

        for(int i = 0; i < 10; i++) {
            loc.setX(loc.getX() + xIncrement);
            loc.setZ(loc.getZ() + zIncrement);
            Material oldMaterial = loc.getBlock().getType();
            Material newMaterial;

            if(loc.getBlock().getType().equals(Material.GRASS_BLOCK)) {
                templateLocation = loc.clone();
                newMaterial = loc.getBlock().getType();
            } else {
                loc.getBlock().setType(Material.SMOOTH_STONE_SLAB);
                newMaterial = Material.SMOOTH_STONE_SLAB;
            }
            paveEventHistory.addBlockHistory(loc.clone(), oldMaterial, newMaterial);
        }

        if(templateLocation != null) {
            Template template = Templates.getTemplateByName(this.player.getName());
            if(template != null) {
                templateHistory = template.placeSingleBaseTemplate(templateLocation);
//                while(templateHistory.getLastBlockHistory() != null) {
//                    paveEventHistory.addBlockHistory(templateHistory.getLastBlockHistory());
//                    paveEventHistory.removeLastBlockHistory();
//                }
            }
        }

        PaveHistories.getUserHistoriesByUserName(player.getName()).addEventHistory(paveEventHistory);
    }
}
