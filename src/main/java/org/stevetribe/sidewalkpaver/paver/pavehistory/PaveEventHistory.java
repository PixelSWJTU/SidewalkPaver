package org.stevetribe.sidewalkpaver.paver.pavehistory;

import org.bukkit.Location;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class PaveEventHistory {
    private LinkedList<PaveBlockHistory> eventHistories = new LinkedList<>();
    private boolean isEventCancelled = false;

    @Nullable
    public PaveBlockHistory getLastBlockHistory() {
        return eventHistories.getLast();
    }

    public boolean isEventCancelled() {
        return isEventCancelled;
    }

    public void removeLastBlockHistory() {
        eventHistories.removeLast();
    }

    public void addBlockHistory(Location location, Material oldMaterial, Material newMaterial) {
        eventHistories.addLast(new PaveBlockHistory(location, oldMaterial, newMaterial));
    }

    public void addBlockHistory(PaveBlockHistory blockHistory) {
        eventHistories.addLast(new PaveBlockHistory(blockHistory.getLocation(), blockHistory.getOldMaterial(), blockHistory.getNewMaterial()));
    }

    public void undo() {
        for(int i = eventHistories.size() - 1; i >=0; i--) {
            eventHistories.get(i).undo();
        }
        isEventCancelled = true;
    }

    public void redo() {
        for(int i = eventHistories.size() - 1; i >=0; i--) {
            eventHistories.get(i).redo();
        }
        isEventCancelled = false;
    }
}
