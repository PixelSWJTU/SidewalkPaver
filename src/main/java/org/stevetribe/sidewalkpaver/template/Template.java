package org.stevetribe.sidewalkpaver.template;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.stevetribe.sidewalkpaver.paver.pavehistory.PaveEventHistory;

import java.util.*;

public class Template {
    private final Map<Location, Block> template = new HashMap<>();

    public boolean isLocationExist(Location location) {
        return template.get(location) != null;
    }

    public void addBlock(Location location , Block block) {
        template.put(location, block);
    }

    public PaveEventHistory placeSingleBaseTemplate(Location location) {
        PaveEventHistory history = new PaveEventHistory();

        for(Map.Entry<Location, Block> entry : template.entrySet()) {
            Location desLoc = new Location(location.getWorld(),
                    entry.getKey().getX() + location.getX(),
                    entry.getKey().getY() + location.getY(),
                    entry.getKey().getZ() + location.getZ());
            BlockState oldBlock = desLoc.getBlock().getState();

            desLoc.getBlock().setBlockData(entry.getValue().getBlockData());
            history.addBlockHistory(oldBlock, desLoc.getBlock().getState());
        }

        return history;
    }
}
