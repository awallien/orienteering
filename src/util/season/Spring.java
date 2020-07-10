package util.season;

import util.point.State;
import util.terrain.ElevationGrid;
import util.point.Point;
import util.terrain.PixelColor;

import java.util.*;

/**
 * "Picture yourself in a boat on a river
 * With tangerine trees and marmalade skies
 * Somebody calls you, you answer quite slowly
 * A girl with kaleidoscope eyes
 * <p>
 * Cellophane flowers of yellow and green
 * Towering over your head
 * Look for the girl with the sun in her eyes
 * And she's gone"
 * ~ Lucy in the Sky by The Beatles (1967)
 *
 * @author Alex Wall
 */
public class Spring {

    /**
     * The max length that a water terrain can reach to transform land into mud
     */
    private final static int MAX_MUD_LENGTH = 15;

    /**
     * Private constructor
     */
    private Spring() {
    }

    /**
     * Uses BFS algorithm to transform adjacent water into mud terrain pixels during
     * the spring season
     *
     * @param map        grid of PixelColor per pixel on terrain
     * @param waterEdges a list of water edges with all state value to 1 to note the starting pixel
     * @return a set of visited points that would be submerged underwater
     */
    public static HashSet<Point> mudTerrainSet(PixelColor[][] map, ElevationGrid elevationGrid, HashSet<Point> waterEdges) {
        HashSet<Point> visited = new HashSet<>();
        Queue<State<State<Point, Integer>, Double>> bfsQueue = new LinkedList<>();

        // initially put land terrains into the BFS queue
        for (Point w : waterEdges) {
            double wEl = elevationGrid.get(w.getY(), w.getX());
            for (State<Point, Double> land : getLandPixels(w, wEl, elevationGrid, map)) {
                Point landPoint = land.getData();
                if (!visited.contains(landPoint)) {
                    bfsQueue.add(
                            new State<>(
                                    new State<>(landPoint, 2),
                                    land.getValue())
                    );
                    visited.add(landPoint);
                }
            }
        }

        // do the BFS, Break-dance Fire Slide
        while(!bfsQueue.isEmpty()) {
            State<State<Point, Integer>, Double> current = bfsQueue.poll();
            Point currentPoint = current.getData().getData();
            Double currentEl = current.getValue();
            Integer currentDepth = current.getData().getValue();
            if(current.getData().getValue() <= MAX_MUD_LENGTH) {
                for(State<Point, Double> next: getLandPixels(currentPoint, currentEl, elevationGrid, map)) {
                    if(!visited.contains(next.getData())) {
                        bfsQueue.add(new State<>(new State<>(next.getData(), currentDepth+1), currentEl));
                        visited.add(next.getData());
                    }
                }
            }
        }
        return visited;
    }


    /**
     * get adjacent land pixels to be converted into mud
     *
     * @param p      the point that it is currently at in the BFS queue
     * @param initEl the initial elevation for the water pixel
     * @param eg     the elevation grid
     * @param map    the pixel map
     * @return list of pixels that will be submerged in water
     */
    private static List<State<Point, Double>> getLandPixels(Point p, double initEl, ElevationGrid eg, PixelColor[][] map) {
        List<State<Point, Double>> mud = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                try {
                    PixelColor pc = map[p.getX() + x][p.getY() + y];
                    Double el = eg.get(p.getY() + y, p.getX() + x);
                    if (pc != PixelColor.LAKE_SWAMP_MARSH && pc != PixelColor.OUT_OF_BOUNDS && initEl + 1 >= el) {
                        mud.add(new State<>(new Point(p.getX() + x, p.getY() + y), initEl));
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }
        return mud;
    }
}
