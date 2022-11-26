package org.stevetribe.sidewalkpaver.paver.pavehistory;

import org.bukkit.Location;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class PaveEventHistory {
    private final LinkedList<PaveBlockHistory> eventHistories = new LinkedList<>();
    private boolean isEventCancelled = false;

    @Nullable
    public PaveBlockHistory getLastBlockHistory() {
        return this.eventHistories.getLast();
    }

    public int getSize() {
        return this.eventHistories.size();
    }

    public boolean isEventCancelled() {
        return this.isEventCancelled;
    }

    public void removeLastBlockHistory() {
        this.eventHistories.removeLast();
    }

    public void addBlockHistory(Location location, Material oldMaterial, Material newMaterial) {
        this.eventHistories.addLast(new PaveBlockHistory(location, oldMaterial, newMaterial));
    }

    public void addBlockHistory(PaveBlockHistory blockHistory) {
        this.eventHistories.addLast(new PaveBlockHistory(blockHistory.getLocation(), blockHistory.getOldMaterial(), blockHistory.getNewMaterial()));
    }

    private LinkedList<PaveBlockHistory> getEventHistories() {
        return eventHistories;
    }

    public void appendAnotherEventHistory(PaveEventHistory history){
        eventHistories.addAll(history.getEventHistories());
    }

    public void undo() {
        for(int i = eventHistories.size() - 1; i >=0; i--) {
            eventHistories.get(i).undo();
        }
        this.isEventCancelled = true;
    }

    public void redo() {
        for(int i = eventHistories.size() - 1; i >=0; i--) {
            eventHistories.get(i).redo();
        }
        this.isEventCancelled = false;
    }
}
