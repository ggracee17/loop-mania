package unsw.loopmania.BattleEntity;

import java.util.Random;


import unsw.loopmania.Battleable;
import unsw.loopmania.PathPosition;

/**
 * a basic form of enemy in the world
 */
public class Slug extends Enemy {

    public Slug(PathPosition position) {
        super(position, 20, 8, 0, 1, 1, 27);
    }


    @Override
    public void attack(Battleable opponent) {
        opponent.defense(this, this.getAttackValue());
    }


    @Override
    public void move() {
        // this basic enemy moves in a random direction... 25% chance up or down, 50% chance not at all...
        double randDouble = new Random().nextDouble();
        if (randDouble < 0.25){
            moveUpPath();
        } else if (randDouble >= 0.25 && randDouble < 0.5) {
            moveDownPath();
        }
        
        notifyObservers();
    }     
}
