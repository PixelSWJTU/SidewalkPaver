package org.stevetribe.sidewalkpaver.paver.pavehistory;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class PaveUserHistory {
    private final LinkedList<PaveEventHistory> histories = new LinkedList<>();

    public void addEventHistory(PaveEventHistory paveEventHistory) {
        this.histories.addLast(paveEventHistory);
    }

    @Nullable
    public PaveEventHistory getTheLastUncancelledPaveHistory() {
        for (int i = this.histories.size() - 1; i >= 0; i--) {
            if (!this.histories.get(i).isEventCancelled()) {
                return this.histories.get(i);
            }
        }
        return null;
    }

    @Nullable
    public PaveEventHistory getTheFirstCancelledPaveHistory() {
        for (int i = 0; i < this.histories.size(); i++) {
            if (this.histories.get(i).isEventCancelled()) {
                return this.histories.get(i);
            }
        }
        return null;
    }
}
