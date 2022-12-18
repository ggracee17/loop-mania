package unsw.loopmania.BattleEntity;

import unsw.loopmania.Battleable;
import unsw.loopmania.PathPosition;

public class ElanMuske extends Enemy {
    /**
     * Create a moving entity which moves up and down the path in position
     *
     * @param position represents the current position in the path
     */
    public ElanMuske(PathPosition position) {
        super(position, 500, 51, 0, 1, 1, 0);
    }

    @Override
    public void attack(Battleable opponent) {
        opponent.defense(this, this.getAttackValue());
    }
    

    @Override
    public void move() {
        moveUpPath();
        notifyObservers();
    }

}
