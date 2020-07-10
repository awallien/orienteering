import util.point.State;
import util.terrain.ElevationGrid;
import util.Orienteering;
import util.PathList;
import util.season.Season;
import util.terrain.Terrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * The main class to run Lab1
 *
 * @author Alex Wall
 */
public class Lab1 {

    /**
     * Outputs the modified image into a PNG file
     *
     * @param img         the image
     * @param outFileName the name of the out file
     * @throws IOException when the image is unable to be written
     */
    private static void outImage(BufferedImage img, String outFileName) throws IOException {
        ImageIO.write(img, "png", new File(outFileName));
    }

    /**
     * Reads command line arguments, starts the algorithm, and outputs the resulting image
     *
     * @param args 0: the terrain image with pixel colors
     *             1: file where each pixel corresponds to an elevation
     *             2: file of controls (x,y) in the terrain map
     *             3: season: "summer", "fall", "winter", and "spring"
     *             4: name of output image file
     */
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Usage: java Lab1.java terrain-image elevation-file path-file <summer|fall|winter|spring> output-image");
        } else {
            BufferedReader reader;
            BufferedImage imageRead;
            try {
                // get the terrain image
                System.out.print("Plotting the terrain image...");
                imageRead = ImageIO.read(new File(args[0]));
                Terrain terrain = new Terrain(imageRead);
                System.out.println("\t\tdone.");

                // get the elevations per pixel
                System.out.print("Reading the elevation file...");
                reader = new BufferedReader(new FileReader(args[1]));
                terrain.setElevationGrid(new ElevationGrid(reader, terrain.width, terrain.height));
                System.out.println("\t\tdone.");

                // get the controls that an orienteer needs to visit
                System.out.print("Building the path list...");
                reader = new BufferedReader(new FileReader(args[2]));
                PathList path = new PathList(reader);
                System.out.println("\t\t\tdone.");

                // get the season
                System.out.print("Tis the season of...");
                Season season = Season.fromString(args[3]);
                terrain.modifyBySeason(season);
                System.out.println("\t\t\t\t" + season + ".");

                // run the algorithm
                System.out.print("Running the terrain...");
                State<PathList, Double> orientList = new Orienteering(terrain, path).start();
                System.out.println("\t\t\t\tdone.");

                // print the resulting image
                System.out.print("Printing the image...");
                outImage(terrain.putPath(orientList.getData()), args[4]);
                System.out.println("\t\t\t\tdone.");

                System.out.println("Minimum distance...\t\t\t\t" + orientList.getValue());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
