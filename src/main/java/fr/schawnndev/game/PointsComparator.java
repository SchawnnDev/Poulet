package fr.schawnndev.game;

import java.util.Comparator;

/**
 * Created by SchawnnDev on 09/04/2015.
 */

public class PointsComparator implements Comparator<Points> {
    @Override
    public int compare(Points a, Points b) {
        return a.getPoints() < b.getPoints() ? -1 : a.getPoints() == b.getPoints() ? 0 : 1;
    }
}