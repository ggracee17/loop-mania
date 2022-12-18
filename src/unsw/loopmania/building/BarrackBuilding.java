package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BattleEntity.AlliedSoldier;
import unsw.loopmania.Observer.CharacterObserver;

public class BarrackBuilding extends Building implements CharacterObserver {
    private final LoopManiaWorld  world;
    
    public BarrackBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, LoopManiaWorld world) {
        super(x, y);
        this.world = world;
    }

    /**
     * If the character steps on the barrack, he can get a allied soldier
     * @param character The character that steps on the barrack
     */
    @Override
    public void update(Character character) {
        if (character.getX() == getX() && character.getY() == getY()) {
            world.addAlliedSoldier(new AlliedSoldier(null, null));
        }
    }
}
