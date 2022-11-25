package org.stevetribe.sidewalkpaver.paver.pavehistory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PaveHistories {
    static private final Map<String, PaveUserHistory> userHistories = new HashMap<>();

    @Nonnull
    static public PaveUserHistory getUserHistoriesByUserName(String userName) {
        if(userHistories.get(userName) == null) {
            userHistories.put(userName, new PaveUserHistory());
        }
        return userHistories.get(userName);
    }
}
