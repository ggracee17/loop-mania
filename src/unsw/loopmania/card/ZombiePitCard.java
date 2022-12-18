package unsw.loopmania.card;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.Spawner;
import unsw.loopmania.building.ZombiePitBuilding;

/**
 * represents a vampire castle card in the backend game world
 */
public class ZombiePitCard extends Card {
    public ZombiePitCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }
    
    @Override
    public boolean isDroppedLocationValid(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        return world.isAdjacentToPath(buildingNodeX, buildingNodeY);
    }

    @Override
    public Building getBuilding(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        Building newZombieBuilding = new ZombiePitBuilding(new SimpleIntegerProperty(buildingNodeX),
                new SimpleIntegerProperty(buildingNodeY), world.getAdjacentPath(buildingNodeX, buildingNodeY));
        world.addEntitySpawner((Spawner) newZombieBuilding);
        return newZombieBuilding;
    }
}