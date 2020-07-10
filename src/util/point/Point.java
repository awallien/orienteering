package util.point;

/**
 * A grid coordinate point
 *
 * @author Alex Wall (asw8675)
 */
public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point other = (Point) obj;
            return other.x == this.x && other.y == this.y;
        }
        return false;
    }

    /**
     * using the bijective algorithm to uniquely set a hashcode for a Point
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int tmp = (y + ((x + 1) / 2));
        return x + (tmp * tmp);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
