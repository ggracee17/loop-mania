package unsw.loopmania.item.Weapons;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.BattleEntity.Enemy;
import unsw.loopmania.BattleEntityState.AlliedState;
import unsw.loopmania.Battleable;

import java.util.Random;

public class Staff extends Weapon {
    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x,y, 2, 450);
    }

    @Override
    public int attack(Battleable opponent, int damage) {
        if (!(opponent instanceof Enemy)) {
            return damage + attackDamage;
        }

        // 80% of the time, trance is not triggered
        if (new Random().nextDouble() > 0.15) {
            return damage + attackDamage;
        } else {
            // Trance is triggered 20% of the times
            opponent.setBattleState(new AlliedState());
            return -1;
        }
    }
}
