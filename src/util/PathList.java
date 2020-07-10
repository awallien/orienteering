package util;

import util.point.Control;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Alex Wall (asw8675)
 */
public class PathList {

    /**
     * list of controls that an orienteer has to go
     */
    private LinkedList<Control> controls;

    /**
     * Constructor with a BufferedReader
     *
     * @param pathReader reader containing info about points
     * @throws IOException - BufferedReader is not found
     */
    public PathList(BufferedReader pathReader) throws IOException {
        controls = new LinkedList<>();
        while (true) {
            String line = pathReader.readLine();
            if (line == null)
                break;
            String[] fields = line.split(" ");
            controls.add(new Control(Integer.parseInt(fields[0]), Integer.parseInt(fields[1])));
        }
    }

    /**
     * Constructor for an empty LinkedList, comes into play for
     * building the orienteer's path from control to control
     */
    public PathList() {
        this.controls = new LinkedList<>();
    }

    public PathList(Collection<Control> collection){
        this.controls = new LinkedList<>(collection);
    }

    /**
     * add some queue controls to this path list
    */
    public void enqueueAll(PathList pathList) {
        this.controls.addAll(pathList.controls);
    }

    /**
     * Retrieve the next control and remove from this
     *
     * @return a control
     */
    public Control dequeue() {
        return controls.poll();
    }

    /**
     * Are there any more controls in the list?
     *
     * @return True if there are more places to travel; otherwise, false
     */
    public boolean hasNext() {
        return !controls.isEmpty();
    }

    /**
     * @return string representation of a util.PathList
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(String.format("PathList(size=%d)[ ", controls.size()));
        for (Control c : controls) {
            sb.append(c);
        }
        sb.append("]");
        return sb.toString();
    }
}
