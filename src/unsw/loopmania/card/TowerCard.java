package unsw.loopmania.card;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.TowerBuilding;

public class TowerCard extends Card {
    public TowerCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public boolean isDroppedLocationValid(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        return world.isAdjacentToPath(buildingNodeX, buildingNodeY);
    }

    @Override
    public Building getBuilding(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        Building newTowerBuilding = new TowerBuilding(new SimpleIntegerProperty(buildingNodeX),
                new SimpleIntegerProperty(buildingNodeY));
        world.addTower((TowerBuilding) newTowerBuilding);
        return newTowerBuilding;
    }
}
