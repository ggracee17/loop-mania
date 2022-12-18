package unsw.loopmania.card;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.building.Building;

/**
 * a Card in the world
 * which doesn't move
 */
public abstract class Card extends StaticEntity {
    public Card(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public abstract boolean isDroppedLocationValid(LoopManiaWorld world, int buildingNodeX, int buildingNodeY);

    public abstract Building getBuilding(LoopManiaWorld world, int buildingNodeX, int buildingNodeY);
}
