package util.point;

/**
 * Representation of a control for an orienteer, which is basically just
 * a point on the terrain that the orienteer needs to visit
 *
 * @author Alex Wall (asw8675)
 */
public class Control extends Point {
    public Control(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Control){
            Control c = (Control) obj;
            return c.getX() == getX() && c.getY() == getY();
        }
        return false;
    }
}
