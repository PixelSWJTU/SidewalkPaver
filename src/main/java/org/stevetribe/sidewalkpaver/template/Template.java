package org.stevetribe.sidewalkpaver.template;

import org.bukkit.Location;
import org.bukkit.Material;
import org.stevetribe.sidewalkpaver.paver.pavehistory.PaveEventHistory;

import java.util.*;

public class Template {
    private final Map<Location, Material> template = new HashMap<>();

    public Map<Location, Material> getTemplate() {
        return template;
    }

    public boolean isLocationExist(Location location) {
        return template.get(location) != null;
    }

    public void addBlockToTemplate(Location location ,Material material) {
        template.put(location, material);
    }

    public PaveEventHistory placeSingleBaseTemplate(Location location) {
        PaveEventHistory history = new PaveEventHistory();

        for(Map.Entry<Location, Material> entry : template.entrySet()) {
            Location desLoc = new Location(location.getWorld(),
                    entry.getKey().getX() + location.getX(),
                    entry.getKey().getY() + location.getY(),
                    entry.getKey().getZ() + location.getZ());

            history.addBlockHistory(desLoc, desLoc.getBlock().getType(), entry.getValue());
            desLoc.getBlock().setType(entry.getValue());
        }

        return history;
    }
}
