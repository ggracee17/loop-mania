package unsw.loopmania.BattleEntity;

import unsw.loopmania.Battleable;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.PathPosition;

import unsw.loopmania.BattleEntityState.BattleState;
import unsw.loopmania.BattleEntityState.EnemyState;
import unsw.loopmania.Observer.EnemyLocationPublisher;
import unsw.loopmania.Observer.EnemyObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends MovingEntity implements Battleable, EnemyLocationPublisher {
    private int health;
    private final int attackValue;
    private final int defenceValue;
    private final int battleRadius;
    private final int supportRadius;

    private final int goldReward;

    private BattleState battleState;

    private List<EnemyObserver> observerList;

    public Enemy(PathPosition position, int health, int attackValue, int defenceValue, int battleRadius, int supportRadius, int goldReward) {
        super(position);
        this.health = health;
        this.attackValue = attackValue;
        this.defenceValue = defenceValue;
        this.battleRadius = battleRadius;
        this.supportRadius = supportRadius;
        this.goldReward = goldReward;

        battleState = new EnemyState();

        observerList = new ArrayList<>();
    }

    /* Trivial Getters and Setters */
    public int getAttackValue() {
        return attackValue;
    }

    public int getDefenceValue() {
        return defenceValue;
    }

    public int getBattleRadius() {
        return battleRadius;
    }

    public int getSupportRadius() {
        return supportRadius;
    }

    public int getHealth() {
        return health;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * move the enemy
     */
    public abstract void move();

    
    /**
     * Attack opponent in battle.
     * @param opponent opponent attacked by this enemy.
     */
    @Override
    public abstract void attack(Battleable opponent);


    /**
     * defence when this enemy is attacked.
     * @param opponent opponent that attacks this enemy
     * @param damage damage caused by opponent
     */
    @Override
    public void defense(Battleable opponent, int damage) {
        setHealth(getHealth() - damage);
    }

    /**
     * @param x The x coordinate of the character
     * @param y The y coordinate of the character
     * @return whether the character is within the battle radius of this enemy
     */
    @Override
    public boolean isWithinBattleRadius(int x, int y) {
        return Math.pow((getX() - x), 2) + Math.pow((getY() - y), 2) <= battleRadius;
    }

    /**
     * @param x The x coordinate of the character
     * @param y The y coordinate of the character
     * @return whether the character is within the support radius of this enemy
     */
    @Override
    public boolean isWithinSupportRadius(int x, int y) {
        return Math.pow((getX() - x), 2) + Math.pow((getY() - y), 2) <= supportRadius;
    }

    /**
     * @return Whether this entity is on the allied side
     */
    @Override
    public boolean isAllied() {
        return !isEnemy();
    }

    /**
     * @return Whether this entity is on the enemy side
     */
    @Override
    public boolean isEnemy() {
        return battleState instanceof EnemyState;
    }

    /**
     * @param battleState The battleState to be set on this enemy
     */
    @Override
    public void setBattleState(BattleState battleState) {
        this.battleState = battleState;
    }

    /**
     * @param battleEntities List of all battleable entities in a battle
     * @return A list containing all the current entity's opponent
     */
    @Override
    public List<Battleable> getEntityToAttack(List<Battleable> battleEntities) {
        return battleState.getEntityToAttack(battleEntities);
    }

    @Override
    public void subscribe(EnemyObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void unsubscribe(EnemyObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observerList.forEach(o -> o.update(this));
    }
    
}
