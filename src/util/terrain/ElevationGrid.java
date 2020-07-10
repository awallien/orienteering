package util.terrain;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * class representing the elevation grid for a given terrain map
 *
 * @author Alex Wall (asw8675)
 */
public class ElevationGrid {

    /** the data structure to store the elevation grid */
    private Double[][] elGrid;

    public ElevationGrid(BufferedReader reader, int width, int height) throws IOException {
        elGrid = new Double[height][width];
        for (int row = 0; row < height; row++) {
            String line = reader.readLine().strip().replaceAll("\\s+", " ");
            String[] fields = line.split(" ");
            for (int col = 0; col < width; col++)
                elGrid[row][col] = Double.valueOf(fields[col]);
        }
    }

    public Double get(int x, int y) {
        return elGrid[y][x];
    }

    /**
     * @return string representation of the elevation grid
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(
                String.format("ElevationGrid: width=%d height=%d\n", elGrid[0].length, elGrid.length));
        for (Double[] doubles : elGrid) {
            for (int j = 0; j < elGrid[0].length; j++)
                sb.append(String.format("[%f]", doubles[j]));
            sb.append("\n");
        }
        return sb.toString();
    }
}
