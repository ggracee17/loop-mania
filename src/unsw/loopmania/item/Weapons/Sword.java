package unsw.loopmania.item.Weapons;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Battleable;

public class Sword extends Weapon {
    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, 8, 300);
    }

    @Override
    public int attack(Battleable opponent, int damage) {
        return damage + attackDamage;
    }
}
