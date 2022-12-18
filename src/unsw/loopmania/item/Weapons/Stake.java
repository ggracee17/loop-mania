package unsw.loopmania.item.Weapons;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Battleable;
import unsw.loopmania.BattleEntity.Vampire;

public class Stake extends Weapon {
    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x,y, 5, 400);
    }

    @Override
    public int attack(Battleable opponent, int damage) {
        if (opponent instanceof Vampire) {
            return (int)((damage + 5) * 1.4);
        } else {
            return damage + attackDamage;
        }
    }
}
