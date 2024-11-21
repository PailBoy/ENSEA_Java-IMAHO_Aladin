import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TrapSprite extends SolidSprite {
    private int damage;

    public TrapSprite(double x, double y, Image image, double width, double height, int damage) {
        super(x, y, image, width, height);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public boolean intersect(Rectangle2D.Double hitBox) {
        return false;
    }


}
