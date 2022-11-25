package org.stevetribe.sidewalkpaver.paver.paveblock;

public class OrganisedPaveBlock extends PaveBlock {
    private String templateName;                // the building template to place.
    private int intervalLength;                 // the frequency of command execute.

    OrganisedPaveBlock(String name) {
        super(name);
    }
}
