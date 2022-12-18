package unsw.loopmania.card;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.Spawner;
import unsw.loopmania.building.VampireCastleBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class VampireCastleCard extends Card {
    public VampireCastleCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public boolean isDroppedLocationValid(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        return world.isAdjacentToPath(buildingNodeX, buildingNodeY);
    }

    @Override
    public Building getBuilding(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        Building newVampireBuilding = new VampireCastleBuilding(new SimpleIntegerProperty(buildingNodeX),
                new SimpleIntegerProperty(buildingNodeY), world.getAdjacentPath(buildingNodeX, buildingNodeY));
        world.addEntitySpawner((Spawner) newVampireBuilding);
        return newVampireBuilding;
    }
}
