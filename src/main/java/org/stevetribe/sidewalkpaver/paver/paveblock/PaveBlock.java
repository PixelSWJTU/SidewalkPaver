package org.stevetribe.sidewalkpaver.paver.paveblock;

public class PaveBlock {
    private String name;
    private PaveBlockType type;
    private UserConfigStatus status;

    PaveBlock(String name) {
        this.name = name;
        this.status = UserConfigStatus.CONFIGURING;
    }

    public String getName() {
        return name;
    }

}

