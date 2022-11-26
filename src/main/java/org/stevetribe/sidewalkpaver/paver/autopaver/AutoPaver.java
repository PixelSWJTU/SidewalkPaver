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
    private final Player player;
    private Location locationA;
    private final Location locationB;
    private final Location direction;
    private final int width;
    private final Material road;
    private final Material marker;
    private final Material line;

    public AutoPaver(Player player, Location locationA, Location locationB, Location direction,
                     int width, Material road, Material marker, Material line) {
        this.player = player;
        this.locationA = locationA;
        this.locationB = locationB;
        this.direction = direction;
        this.width = width;
        this.road = road;
        this.marker = marker;
        this.line = line;
    }

    public boolean autoPave() {
        locationA = locationA.clone();
        int count = 0;
        LinkedList<Location> sidewalkPath = new LinkedList<>();
        Location preLoc = null;

        while (locationA.getX() != locationB.getX() || locationA.getZ() != locationB.getZ()) {
            double x = locationA.getX();
            double z = locationA.getZ();

            // search for the next sidewalk block, 优化计划：优先向direction反方向搜索
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    if (count++ > 1000) {        // 避免死循环，在执行超过1000次后强制退出
                        return false;
                    }
                    // skip the current block.
                    if (xOffset == 0 && zOffset == 0) {
                        continue;
                    }
                    if (isBlockOnTheSideWalk(locationA, xOffset, zOffset)) {
                        // skip the previous block
                        if (preLoc != null) {
                            if (locationA.getX() + xOffset == preLoc.getX() && locationA.getZ() + zOffset == preLoc.getZ()) {
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
            if (!isBlockOnTheSideWalk(locationA)) {
                return false;
            }

            // add to the list
            if (preLoc != null) {
                sidewalkPath.add(preLoc.clone());
            }
        }
        sidewalkPath.add(locationB.clone());

        // pave
        paveSidewalk(sidewalkPath, direction);

        return true;
    }

    private boolean isBlockOnTheSideWalk(Location location, double xOffset, double zOffset) {
        Location loc = new Location(location.getWorld(), location.getX() + xOffset, location.getY(), location.getZ() + zOffset);
        return loc.getBlock().getType().equals(this.line);
    }

    private boolean isBlockOnTheSideWalk(Location loc) {
        return loc.getBlock().getType().equals(this.line);
    }

    private void paveSidewalk(LinkedList<Location> sidewalkPath, Location direction) {
        Iterator<Location> sidewalkBlock = sidewalkPath.descendingIterator();
        PaveEventHistory paveEventHistory = new PaveEventHistory();
        LinkedList<Location> templateLocations = new LinkedList<>();
        Template template = Templates.getTemplateByName(this.player.getName());

        while (sidewalkBlock.hasNext()) {
            Location loc = sidewalkBlock.next();

            int xIncrement;
            int zIncrement;

            if (direction.getYaw() > -45 && direction.getYaw() <= 45) {              // positive z
                xIncrement = 0;
                zIncrement = 1;
            } else if (direction.getYaw() > 45 && direction.getYaw() <= 135) {       // negative x
                xIncrement = -1;
                zIncrement = 0;
            } else if (direction.getYaw() > 135 || direction.getYaw() <= -135) {     // negative z
                xIncrement = 0;
                zIncrement = -1;
            } else {                                                                 // positive x
                xIncrement = 1;
                zIncrement = 0;
            }

            // pave a single line of sidewalk
            for (int i = 0; i < width; i++) {
                loc.setX(loc.getX() + xIncrement);
                loc.setZ(loc.getZ() + zIncrement);
                Material oldMaterial = loc.getBlock().getType();
                Material newMaterial;

                if (loc.getBlock().getType().equals(marker)) {
                    // detect and record template location
                    templateLocations.addLast(loc.clone());
                    newMaterial = loc.getBlock().getType();
                } else {
                    loc.getBlock().setType(road);
                    newMaterial = road;
                }
                paveEventHistory.addBlockHistory(loc.clone(), oldMaterial, newMaterial);
            }
        }

        while (templateLocations.size() > 0 && template != null){
            Location templateLocation = templateLocations.getLast();
            templateLocations.removeLast();

            PaveEventHistory templateHistory = template.placeSingleBaseTemplate(templateLocation);
            paveEventHistory.appendAnotherEventHistory(templateHistory);
        }

        PaveHistories.getUserHistoriesByUserName(player.getName()).addEventHistory(paveEventHistory);
    }
}
