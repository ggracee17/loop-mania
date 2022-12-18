package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.Observer.CharacterObserver;

public class VillageBuilding extends Building implements CharacterObserver {

    public VillageBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * If the character steps on the village, he can regain health
     * @param character The character that steps on the village
     */
    @Override
    public void update(Character character) {
        if (character.getX() == getX() && character.getY() == getY()) {
            character.setHealth((int) Math.min(character.getHealth() * 1.1,
                                                character.getHealthLimit())
            );
        }
    }
}
