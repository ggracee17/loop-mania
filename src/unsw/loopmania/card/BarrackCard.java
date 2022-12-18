package unsw.loopmania.card;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Observer.CharacterObserver;
import unsw.loopmania.building.BarrackBuilding;
import unsw.loopmania.building.Building;

public class BarrackCard extends Card {
    public BarrackCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public boolean isDroppedLocationValid(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        return world.isOnPath(buildingNodeX, buildingNodeY);
    }

    @Override
    public Building getBuilding(LoopManiaWorld world, int buildingNodeX, int buildingNodeY) {
        Building newBarrackBuilding = new BarrackBuilding(new SimpleIntegerProperty(buildingNodeX),
                new SimpleIntegerProperty(buildingNodeY), world);
        world.getCharacter().subscribe((CharacterObserver) newBarrackBuilding);
        return newBarrackBuilding;
    }
}
