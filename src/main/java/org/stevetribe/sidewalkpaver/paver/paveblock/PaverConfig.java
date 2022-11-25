package org.stevetribe.sidewalkpaver.paver.paveblock;

import org.stevetribe.sidewalkpaver.paver.paveblock.PaverBlockSet;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PaverConfig {
    final private static Map<String, PaverBlockSet> paverConfigs = new HashMap<String, PaverBlockSet>();

    static public void newConfig(String username, PaverBlockSet paveBlockSet) {
        paverConfigs.put(username, paveBlockSet);
    }

    @Nullable
    static public PaverBlockSet getPaverBlockSetByUserName(String username) {
        return paverConfigs.get(username);
    }
}
