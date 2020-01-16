package dogs.red.nine.oracle.data.tables;

import java.util.Comparator;

public class TableComparator implements Comparator<TableEntry> {
    @Override
    public int compare(TableEntry thisEntry, TableEntry otherEntry) {
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal

        if (thisEntry.getPoints() < otherEntry.getPoints()) {
            return -1;
        } else if (thisEntry.getPoints() > otherEntry.getPoints()) {
            return 1;
        } else {
            if (thisEntry.getGoalDifference() < otherEntry.getGoalDifference()) {
                return -1;
            } else if (thisEntry.getGoalDifference() > otherEntry.getGoalDifference()) {
                return 1;
            } else {
                if (thisEntry.getGoalsFor() < otherEntry.getGoalsFor()) {
                    return -1;
                } else if (thisEntry.getGoalDifference() > otherEntry.getGoalDifference()) {
                    return 1;
                }
            }
            // must be the same
            return 0;
        }
    }
}
