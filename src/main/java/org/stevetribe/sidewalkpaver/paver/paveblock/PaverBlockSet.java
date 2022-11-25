package org.stevetribe.sidewalkpaver.paver.paveblock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PaverBlockSet {
    private final List<PaveBlock> list = new ArrayList<PaveBlock>();
    public UserConfigStatus status;

    // add an empty PaveBlock
    // false for duplicate, yes for success
    public boolean addNewPaveBlock(String name, PaveBlockType type) {
        if(list.stream().anyMatch(b -> b.getName().equals(name))){
            return false;
        }
        this.list.add(new PaveBlock(name));
        return true;
    }

    @Nullable
    public PaveBlock getUserPaveBlockByName(String name) {
        return list.stream().filter(b -> b.getName().equals(name)).findFirst().get();
    }
}
