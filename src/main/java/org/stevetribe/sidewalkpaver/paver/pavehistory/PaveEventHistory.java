package org.stevetribe.sidewalkpaver.paver.pavehistory;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import javax.annotation.Nullable;
import java.util.Iterator;
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

    public void addBlockHistory(BlockState oldBlock, BlockState newBlock) {
        this.eventHistories.addLast(new PaveBlockHistory(oldBlock, newBlock));
    }

    public void addBlockHistory(PaveBlockHistory blockHistory) {
        this.eventHistories.addLast(new PaveBlockHistory(blockHistory.getOldBlock().getState(), blockHistory.getNewBlock().getState()));
    }

    private LinkedList<PaveBlockHistory> getEventHistories() {
        return eventHistories;
    }

    public void appendAnotherEventHistory(PaveEventHistory history){
        eventHistories.addAll(history.getEventHistories());
    }

    public void undo() {
        for (PaveBlockHistory eventHistory : eventHistories) {
            eventHistory.undo();
        }
        this.isEventCancelled = true;
    }

    public void redo() {
        for (PaveBlockHistory eventHistory : eventHistories) {
            eventHistory.redo();
        }
        this.isEventCancelled = true;
    }
}
