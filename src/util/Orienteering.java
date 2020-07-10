package util;

import util.point.Control;
import util.point.Point;
import util.point.State;
import util.season.Season;
import util.terrain.Terrain;

import java.util.*;

/**
 * Class to perform the A* search and heuristics on finding the minimum distance
 * between points in the orienteer's path list
 *
 * @author Alex Wall (asw8675)
 */
public class Orienteering {

    /**
     * Multipliers when travelling to different pixels in any of the octal directions
     */
    private final double X_MULTIPLIER = 10.29;
    private final double Y_MULTIPLIER = 7.55;

    /**
     * Information regarding the terrain that the orienteer is performing on
     */
    private Terrain terrain;

    /**
     * the list of controls that the orienteer needs to visit
     */
    private PathList pathList;


    /**
     * Constructor
     */
    public Orienteering(Terrain terrain, PathList pathList) {
        this.terrain = terrain;
        this.pathList = pathList;
    }


    /**
     * takes a pair of sequence points from the path list passed into this class
     * and perform A* search algorithm
     *
     * @return a full list of all the points that the orienteer travels and their total distance
     */
    public State<PathList, Double> start() {
        Control current = pathList.dequeue();
        PathList fullList = new PathList();
        double fullDistance = 0.0;
        while (pathList.hasNext()) {
            Control next = pathList.dequeue();
            State<PathList, Double> subList = search(current, next);
            fullList.enqueueAll(subList.getData());
            fullDistance += subList.getValue();
            current = next;
        }
        return new State<>(fullList, fullDistance);
    }

    /**
     * calculates the displacement of an orienteer from point s to f
     *
     * @param s the starting point
     * @param f the ending point
     * @return the displacement between point s and f
     */
    public double displacement(Control s, Control f) {
        double pEl = terrain.getElevation(s.getX(), s.getY()), gEl = terrain.getElevation(f.getX(), f.getY());
        return Math.sqrt(
                X_MULTIPLIER * Math.pow(s.getX() - f.getX(), 2)
                        + Y_MULTIPLIER * Math.pow(s.getY() - f.getY(), 2)
                        + Math.pow(Math.abs(pEl - gEl), 2)
        );
    }

    /**
     * g(n) - the time it takes for an orienteer to go from one point to an adjacent point
     *
     * @param s the starting point
     * @param f the destination point
     * @return the cost for an orienteer to go from s to f
     */
    public double cost(Control s, Control f) {
        double distance = displacement(s, f);
        double speed = terrain.getPixel(f.getX(), f.getY()).speed;
        return distance / speed;
    }

    /**
     * h(n) - the distance from one point to the goal
     *
     * @param p    the point that the orienteer is at
     * @param goal the final point that the orienteer is travelling to
     * @return the heuristic cost from the point to another
     */
    public double heuristic(Control p, Control goal) {
        return displacement(p, goal) / terrain.getPixel(p.getX(), p.getY()).speed;
    }

    /**
     * performs the A* search algorithm
     *
     * @param start the starting point
     * @param goal  the goal point
     */
    private State<PathList, Double> search(Control start, Control goal) {
        HashMap<Control, Control> cameFrom = new HashMap<>();
        HashMap<Control, Double> gScore = new HashMap<>();
        HashMap<Control, Double> fScore = new HashMap<>();

        PriorityQueue<Control> openQueue = new PriorityQueue<>(
                Comparator.comparingInt(o -> fScore.getOrDefault(o, Double.POSITIVE_INFINITY).intValue()));

        openQueue.add(start);
        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));

        while (!openQueue.isEmpty()) {
            Control current = openQueue.poll();
            if (current.equals(goal)) {
                return getListAndDistance(cameFrom, start, goal);
            }
            for (Control neighbor : getSuccessors(current)) {
                if (neighbor.getX() >= terrain.width || neighbor.getY() >= terrain.height || neighbor.getX() < 0 || neighbor.getY() < 0) {
                    continue;
                }
                double temp = gScore.get(current) + cost(current, neighbor);

                if (temp < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, temp);
                    fScore.put(neighbor, temp + heuristic(neighbor, goal));
                    if (!openQueue.contains(neighbor)) {
                        openQueue.add(neighbor);
                    }
                }
            }
        }

        return new State<>(new PathList(), 0.0);

    }

    /**
     * get Pathlist and distance of the path
     *
     * @param cameFrom
     * @param start
     * @param goal
     * @return
     */
    private State<PathList, Double> getListAndDistance(HashMap<Control, Control> cameFrom, Control start, Control goal) {
        Stack<Control> path = new Stack<>();
        double distance = 0.0;
        path.push(goal);
        Control curr = cameFrom.get(goal);
        while (!curr.equals(start)) {
            path.push(curr);
            Control next = cameFrom.get(curr);
            distance += displacement(curr, next);
            curr = next;
        }
        path.push(start);
        return new State<>(new PathList(path), distance);
    }

    /**
     * The helper method you love and know throughout this whole project
     *
     * @param current the current Control
     * @return the neighbors of it
     */
    private HashSet<Control> getSuccessors(Control current) {
        HashSet<Control> neighbors = new HashSet<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int xx = current.getX() + x;
                int yy = current.getY() + y;
                neighbors.add(new Control(xx, yy));
            }
        }
        return neighbors;
    }

}
