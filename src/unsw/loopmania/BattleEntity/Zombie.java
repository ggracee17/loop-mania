package unsw.loopmania.BattleEntity;

import unsw.loopmania.BattleEntityState.EnemyState;
import unsw.loopmania.Battleable;
import unsw.loopmania.PathPosition;

import java.util.Random;

/**
 * a basic form of zombie in the world
 */
public class Zombie extends Enemy {
    private boolean moveNextTick;

    public Zombie(PathPosition position) {
        super(position, 30, 14, 0, 1, 1, 51);
        moveNextTick = true;
    }

    @Override
    public void attack(Battleable opponent) {
        if (!(opponent instanceof AlliedSoldier)) {
            opponent.defense(this, this.getAttackValue());
        } else {
            // Critical attack can only apply to allied soldier
            Random random = new Random(2);

            if (random.nextDouble() > 0.1) {
                // 90% of the time normal attack
                opponent.defense(this, this.getAttackValue());
            } else {
                // Critical attack probability is 10%
                opponent.setBattleState(new EnemyState());
            }
        }
    }

    /**
     * Zombie moves one tile per two ticks
     */
    @Override
    public void move(){
        if (moveNextTick) {
            moveUpPath();
            moveNextTick = false;
        } else {
            moveNextTick = true;
        }

        notifyObservers();
    }
}

