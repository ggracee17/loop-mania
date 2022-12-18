package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * The moving entity
 */
public abstract class MovingEntity extends Entity {

    /**
     * object holding position in the path
     */
    private PathPosition position;

    /**
     * Create a moving entity which moves up and down the path in position
     * @param position represents the current position in the path
     */
    public MovingEntity(PathPosition position) {
        super();
        this.position = position;
    }

    /**
     * move clockwise through the path
     */
    public void moveDownPath() {
        position.moveDownPath();
    }

    /**
     * move anticlockwise through the path
     */
    public void moveUpPath() {
        position.moveUpPath();
    }

    @Override
    public SimpleIntegerProperty x() {
        return position.getX();
    }

    @Override
    public SimpleIntegerProperty y() {
        return position.getY();
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
