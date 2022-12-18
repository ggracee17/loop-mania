package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BattleEntity.Enemy;
import unsw.loopmania.Observer.EnemyObserver;

public class TrapBuilding extends Building implements EnemyObserver {
    private final LoopManiaWorld world;
    
    public TrapBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, LoopManiaWorld world) {
        super(x, y);
        this.world = world;
    }

    @Override
    public void update(Enemy enemy) {
        if (enemy.getX() == getX() && enemy.getY() == getY()) {
            enemy.setHealth(enemy.getHealth()-20);

            // kill enemy if it is defeated.
            if (enemy.getHealth() <= 0) {
                world.addTrappedEnemies(enemy);
                world.killEnemy(enemy);
            }

            // destroy this trap
            world.getEnemies().forEach(e -> e.unsubscribe(this));
            world.killTrap(this);

        }
      
    }
}
