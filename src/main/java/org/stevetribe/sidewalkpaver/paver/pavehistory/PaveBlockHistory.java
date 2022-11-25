package org.stevetribe.sidewalkpaver.paver.pavehistory;

import org.bukkit.Location;
import org.bukkit.Material;

public class PaveBlockHistory {
    private final Location location;
    private final Material oldMaterial;
    private final Material newMaterial;

    PaveBlockHistory(Location location, Material oldMaterial, Material newMaterial) {
        this.location = location;
        this.oldMaterial = oldMaterial;
        this.newMaterial = newMaterial;
    }

    public Location getLocation() {
        return location;
    }

    public Material getOldMaterial() {
        return oldMaterial;
    }

    public Material getNewMaterial() {
        return newMaterial;
    }

    public void undo() {
        location.getBlock().setType(oldMaterial);
    }

    public void redo() {
        location.getBlock().setType(newMaterial);
    }
}
