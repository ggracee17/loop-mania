package unsw.loopmania.BattleEntity;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.BattleEntityState.AlliedState;
import unsw.loopmania.BattleEntityState.BattleState;
import unsw.loopmania.Battleable;
import unsw.loopmania.StaticEntity;

import java.util.List;

public class AlliedSoldier extends StaticEntity implements Battleable {
    private int health;
    private final int attackValue;
    private final int defenceValue;

    private BattleState battleState;

    public AlliedSoldier(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        health = 30;
        attackValue = 10;
        defenceValue = 0;

        battleState = new AlliedState();
    }


    /**
     * @return The current health of this Entity
     */
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    /**
     * Let the subject attack the opponent passed in
     *
     * @param opponent The opponent to attack
     */
    @Override
    public void attack(Battleable opponent) {
        opponent.defense(this, attackValue);
    }

    /**
     * Let the subject take damage from an attack
     *
     * @param opponent The opponent who performed the attack
     * @param damage   The damage made to the subject as calculated by the opponent
     */
    @Override
    public void defense(Battleable opponent, int damage) {
        setHealth(getHealth() - (damage - defenceValue));
    }
   

    /**
     * @param x The x coordinate of the character
     * @param y The y coordinate of the character
     * @return whether the character is within the battle radius of this enemy
     */
    @Override
    public boolean isWithinBattleRadius(int x, int y) {
        return false;
    }

    /**
     * @param x The x coordinate of the character
     * @param y The y coordinate of the character
     * @return whether the character is within the support radius of this enemy
     */
    @Override
    public boolean isWithinSupportRadius(int x, int y) {
        return false;
    }

    /**
     * @return Whether this entity is on the allied side
     */
    @Override
    public boolean isAllied() {
        return battleState instanceof AlliedState;
    }

    /**
     * @return Whether this entity is on the enemy side
     */
    @Override
    public boolean isEnemy() {
        return !isAllied();
    }

    /**
     * @param battleEntities List of all battleable entities in a battle
     * @return A list containing all the current entity's opponent
     */
    @Override
    public List<Battleable> getEntityToAttack(List<Battleable> battleEntities) {
        return battleState.getEntityToAttack(battleEntities);
    }

    /**
     * Set this entity's battle state
     *
     * @param battleState
     */
    @Override
    public void setBattleState(BattleState battleState) {
        this.battleState = battleState;
    }
}
