package unsw.loopmania;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 * represents a non-moving entity
 * unlike the moving entities, this can be placed anywhere on the game map
 */
public abstract class StaticEntity extends Entity {
    /**
     * x and y coordinates represented by IntegerProperty, so ChangeListeners can be added
     */
    private IntegerProperty x, y;

    public StaticEntity(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public IntegerProperty x() {
        return x;
    }

    @Override
    public IntegerProperty y() {
        return y;
    }

    @Override
    public int getX() {
        return x().get();
    }

    @Override
    public int getY() {
        return y().get();
    }
}
