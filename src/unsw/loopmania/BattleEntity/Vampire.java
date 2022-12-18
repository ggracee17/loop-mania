package unsw.loopmania.BattleEntity;

import java.util.Random;

import unsw.loopmania.Battleable;
import unsw.loopmania.PathPosition;

public class Vampire extends Enemy {
    private int remainedCriticalAttacks;
    private int extraDamage;

    public Vampire(PathPosition position) {
        super(position, 90, 20, 0, 2, 3, 80);

        remainedCriticalAttacks = 0;
        extraDamage = 0;
    }

    @Override
    public void attack(Battleable opponent) {
        Random random = new Random(2);

        // critical mode trigger
        if (remainedCriticalAttacks == 0 && random.nextDouble() > 0.85) {
            remainedCriticalAttacks = 1 + random.nextInt(2);
            extraDamage = 5 + random.nextInt(13);
        }

        // attack or critical attack
        if (remainedCriticalAttacks > 0) {
            opponent.defense(this, this.getAttackValue()+extraDamage);
            remainedCriticalAttacks--;
        } else {
            opponent.defense(this, this.getAttackValue());
        }
    }

    @Override
    public void move() {
        moveUpPath();
        notifyObservers();
    }
}
