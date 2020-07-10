package util.season;

import util.point.Point;
import util.point.State;
import util.terrain.PixelColor;

import java.util.*;

/**
 * "Wintertime winds blue and freezing
 * Coming from northern storms in the sea
 * Love has been lost, is that the reason?
 * Trying so desperately to be free
 * <p>
 * Come with me dance, my dear
 * Winter's so cold this year
 * And you are so warm
 * My wintertime love to be"
 * ~ Wintertime Love by The Doors (1968)
 *
 * @author Alex Wall (asw8675)
 */
public class Winter {

    /**
     * The max length that a water terrain can freeze from its standard position
     */
    private static final int MAX_FREEZE_LENGTH = 7;

    /**
     * Private constructor
     */
    private Winter() {
    }

    /**
     * Uses BFS algorithm to transform adjacent water into frozen water terrain pixels during
     * the winter season
     *
     * @param map        the PixelColor grid for easy lookup on terrain
     * @param waterEdges a list of water edges with all state value to 1 to note the starting pixel
     * @return a set of visited frozen points
     */
    public static HashSet<Point> freezeWaterSet(PixelColor[][] map, HashSet<Point> waterEdges) {
        Queue<State<Point, Integer>> bfsQueue = new LinkedList<>();
        HashSet<Point> visited = new HashSet<>(waterEdges);

        // take each water edge and put the nearest water pixels into the queue
        for (Point w : waterEdges) {
            bfsQueue.add(new State<>(w, 1));
            for (Point o : getWaterPixels(w, map)) {
                if (!waterEdges.contains(o) && !visited.contains(o)) {
                    bfsQueue.add(new State<>(o, 2));
                    visited.add(o);
                }
            }
        }

        // do BFS on the rest of the water pixels that were obtained from the water edges
        while (!bfsQueue.isEmpty()) {
            State<Point, Integer> current = bfsQueue.poll();
            if (current.getValue() < MAX_FREEZE_LENGTH) {
                for (Point o : getWaterPixels(current.getData(), map)) {
                    if (!visited.contains(o)) {
                        bfsQueue.add(new State<>(o, current.getValue() + 1));
                        visited.add(o);
                    }
                }
            }
        }

        return visited;
    }

    /**
     * get the adjacent water pixels from a given water pixel point
     *
     * @param p   the point on the grid that is a water pixel
     * @param map the pixel color map
     * @return list of adjacent water pixel points
     */
    private static List<Point> getWaterPixels(Point p, PixelColor[][] map) {
        List<Point> waters = new ArrayList<>();
        for (int x = -1; x <= 1; x ++) {
            for (int y = -1; y <= 1; y ++) {
                try {
                    PixelColor pc = map[p.getX() + x][p.getY() + y];
                    if (pc == PixelColor.LAKE_SWAMP_MARSH)
                        waters.add(new Point(p.getX() + x, p.getY() + y));
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }
        return waters;
    }

}
