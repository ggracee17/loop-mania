package unsw.loopmania.card;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.VillageBuilding;

/**
 * represents a village card in the backend game world
 */
public class VillageCard extends Card {
    public VillageCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public boolean isDroppedLocationValid(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        return world.isOnPath(buildingNodeX, buildingNodeY);
    }

    @Override
    public Building getBuilding(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        Building newVillageBuilding = new VillageBuilding(new SimpleIntegerProperty(buildingNodeX),
                new SimpleIntegerProperty(buildingNodeY));
        world.getCharacter().subscribe((VillageBuilding) newVillageBuilding);
        return newVillageBuilding;
    }
}
