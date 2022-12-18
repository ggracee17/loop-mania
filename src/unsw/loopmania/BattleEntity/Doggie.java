package unsw.loopmania.BattleEntity;

import unsw.loopmania.Battleable;
import unsw.loopmania.PathPosition;

public class Doggie extends Enemy {
    private int defenceCounter;

    /**
     * Create a moving entity which moves up and down the path in position
     *
     * @param position represents the current position in the path
     */
    public Doggie(PathPosition position) {
        super(position, 150, 18, 0, 1, 1, 0);
        defenceCounter = 0;
    }

    @Override
    public void attack(Battleable opponent) {
        opponent.defense(this, this.getAttackValue());
    }


    @Override
    public void defense(Battleable opponent, int damage) {
        if (defenceCounter != 4) {
            setHealth(getHealth() - damage);
            defenceCounter++;
        } else {
            defenceCounter = 0;
        }
    }

    @Override
    public void move() {
        moveUpPath();
        notifyObservers();
    }
}
