package util.terrain;

import util.PathList;
import util.point.Point;
import util.season.Season;
import util.season.Spring;
import util.season.Winter;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.*;

/**
 * class implementation of the park/terrain map that the orienteer would travel on
 *
 * @author Alex Wall (asw8675)
 */
public class Terrain {

    /**
     * image of the terrain map
     */
    private BufferedImage terrainImage;

    /**
     * each pixel in an xy-coordinate map is a distinct PixelColor
     */
    private PixelColor[][] map;

    private HashSet<Point> waterEdges;
    private HashSet<Point> footPathNearForest;

    private ElevationGrid elevationGrid;

    /**
     * width and height of the terrain image
     */
    public final int width, height;

    /**
     * Constructor
     *
     * @param imageReader buffered image of the terrain
     */
    public Terrain(BufferedImage imageReader) {
        this.terrainImage = imageReader;
        this.width = imageReader.getWidth();
        this.height = imageReader.getHeight();

        this.waterEdges = new HashSet<>();
        this.footPathNearForest = new HashSet<>();
        this.map = new PixelColor[height][width];
        readImage();
    }

    /**
     * reads an image pixel by pixel; any special pixels that needs attention would be placed in a hashset
     */
    private void readImage() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                PixelColor terrainPixel = PixelColor.fromHex(terrainImage.getRGB(col, row) & PixelColor.RGB_BYTES);
                map[row][col] = terrainPixel;
                if (terrainPixel == PixelColor.LAKE_SWAMP_MARSH && isWaterEdge(row, col)) {
                    waterEdges.add(new Point(row, col));
                }
                if (terrainPixel == PixelColor.FOOTPATH && isNearForest(row, col)) {
                    footPathNearForest.add(new Point(row, col));
                }
            }
        }
    }

    /**
     * check if the adjacent pixels to a given coordinate is an EASY_MOVE_FOREST
     * @param row the y coordinate
     * @param col the x coordinate
     * @return true if the given coordinate pixel is adjacent to an EASY_MOVE_FOREST pixel; otherwise, false
     * @pre the given coordinate maps to a FOOTPATH coordinate
     */
    private boolean isNearForest(int row, int col) {
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                try {
                    PixelColor check = PixelColor.fromHex(terrainImage.getRGB(col + x, row + y) & PixelColor.RGB_BYTES);
                    if (check == PixelColor.EASY_MOVE_FOREST)
                        return true;
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }
        return false;
    }

    /**
     * checks all sides of a given row and column on the terrain, if any of the sides is
     * not a water terrain or frozen water, then this pixel is an edge
     *
     * @param row the y coordinate
     * @param col the x coordinate
     * @return true if pixel is a water edge; otherwise, false
     * @pre the pixel at (col,row) is a water terrain
     */
    private boolean isWaterEdge(int row, int col) {
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                try {
                    PixelColor check = PixelColor.fromHex(terrainImage.getRGB(col + x, row + y) & PixelColor.RGB_BYTES);
                    if (check != PixelColor.LAKE_SWAMP_MARSH && check != PixelColor.FROZEN_WATER)
                        return true;
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }
        return false;
    }

    public void setElevationGrid(ElevationGrid elevationGrid) {
        this.elevationGrid = elevationGrid;
    }

    /**
     * gets the pixel color on the terrain with the given coordinates
     *
     * @param x x coordinate for a point
     * @param y y coordinate for a point
     * @return the pixel color at a given coordinate
     */
    public PixelColor getPixel(int x, int y) {
        return map[y][x];
    }

    public Double getElevation(int x, int y) { return elevationGrid.get(x,y);}

    /**
     * puts a path of the orienteer onto this terrain
     * <p>
     * a list of controls that the orienteer has taken
     *
     * @return a modified image of this terrain with the path
     */
    public BufferedImage putPath(PathList orienteer) {
        ColorModel cm = terrainImage.getColorModel();
        WritableRaster wr = terrainImage.copyData(terrainImage.getRaster().createCompatibleWritableRaster());
        BufferedImage newImage =
                new BufferedImage(cm, wr, terrainImage.isAlphaPremultiplied(), null);

        while (orienteer.hasNext()) {
            Point c = orienteer.dequeue();
            newImage.setRGB(c.getX(), c.getY(), PixelColor.ME.getRGBFill());
        }
        return newImage;
    }

    /**
     * Depending on the season, parts of the terrain are likely to change
     *
     * @param season summer|fall|winter|spring
     */
    public void modifyBySeason(Season season) {
        switch (season) {
            case WINTER:
                for (Point p : Winter.freezeWaterSet(map, waterEdges)) {
                    terrainImage.setRGB(p.getY(), p.getX(), PixelColor.FROZEN_WATER.getRGBFill());
                    map[p.getX()][p.getY()] = PixelColor.FROZEN_WATER;
                }
                break;
            case SPRING:
                for(Point p: Spring.mudTerrainSet(map, elevationGrid, waterEdges)) {
                    terrainImage.setRGB(p.getY(), p.getX(), PixelColor.MUD.getRGBFill());
                    map[p.getX()][p.getY()] = PixelColor.MUD;
                }
                break;
            case FALL:
                for(Point p: footPathNearForest){
                    terrainImage.setRGB(p.getY(), p.getX(), PixelColor.FALL_FOOTPATH.getRGBFill());
                    map[p.getX()][p.getY()] = PixelColor.FALL_FOOTPATH;
                }
                break;
            default:
                break;
        }
    }

    /**
     * @return string representation of this object, a layout of terrain of uppercase letters where each letter
     * is the abbreviation of the PixelColor type
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("util.terrain.Terrain: width=%d, height=%d\n", width, height));
        for (PixelColor[] row : map) {
            for (PixelColor pc : row) {
                sb.append(pc.abbrev);
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
