package unsw.loopmania.Observer;

import unsw.loopmania.BattleEntity.Enemy;

public interface EnemyObserver {
    // This method is called whenever the enemy's position is changed.
    void update(Enemy enemy);
}
