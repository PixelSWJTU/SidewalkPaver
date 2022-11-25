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
            System.out.println(this.histories.get(i).isEventCancelled());
            if (!this.histories.get(i).isEventCancelled()) {
                System.out.println(this.histories.get(i).getLastBlockHistory().getLocation());
                return this.histories.getLast();
            }
        }
        return null;
    }

    @Nullable
    public PaveEventHistory getTheFirstCancelledPaveHistory() {
        for (int i = 0; i < this.histories.size() - 1; i++) {
            System.out.println(this.histories.get(i));
            System.out.println(this.histories.get(i).isEventCancelled());
            if (this.histories.get(i).isEventCancelled()) {
                return this.histories.getLast();
            }
        }
        return null;
    }
}
