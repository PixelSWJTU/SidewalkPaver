package org.stevetribe.sidewalkpaver.paver.paveblock;

import org.bukkit.Material;

public class ConstantPaveBlock extends PaveBlock{
    Material material;
    int Length;

    ConstantPaveBlock(String name) {
        super(name);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
