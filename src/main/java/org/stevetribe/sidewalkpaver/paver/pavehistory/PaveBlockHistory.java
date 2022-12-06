package org.stevetribe.sidewalkpaver.paver.pavehistory;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class PaveBlockHistory {
    private final BlockState oldBlock;
    private final BlockState newBlock;

    PaveBlockHistory(BlockState oldBlock, BlockState newBlock) {
        this.oldBlock = oldBlock;
        this.newBlock = newBlock;
    }

    public Block getNewBlock() {
        return oldBlock.getBlock();
    }

    public Block getOldBlock() {
        return oldBlock.getBlock();
    }

    public void undo() {
        oldBlock.update(true);
    }

    public void redo() {
        newBlock.update(true);
    }
}
