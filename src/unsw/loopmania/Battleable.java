package unsw.loopmania;

import unsw.loopmania.BattleEntityState.BattleState;

import java.util.List;

/**
 * This interface should be extend by all class that can attend a battle.
 * It provides methods that are necessary in a battle.
 */
public interface Battleable {
    /**
     * Let the subject attack the opponent passed in
     * @param opponent  The opponent to attack
     */
    void attack(Battleable opponent);

    /**
     * Let the subject take damage from an attack
     * @param opponent  The opponent who performed the attack
     * @param damage    The damage made to the subject as calculated by the opponent
     */
    void defense(Battleable opponent, int damage);

    /**
     * @return  The current health of this Entity
     */
    int getHealth();

    /**
     * @param x The x coordinate of the character
     * @param y The y coordinate of the character
     * @return whether the character is within the battle radius of this enemy
     */
    boolean isWithinBattleRadius(int x, int y);

    /**
     * @param x The x coordinate of the character
     * @param y The y coordinate of the character
     * @return whether the character is within the support radius of this enemy
     */
    boolean isWithinSupportRadius(int x, int y);

    /**
     * @return Whether this entity is on the allied side
     */
    boolean isAllied();

    /**
     * @return Whether this entity is on the enemy side
     */
    boolean isEnemy();

    /**
     * @param battleEntities List of all battleable entities in a battle
     * @return A list containing all the current entity's opponent
     */
    List<Battleable> getEntityToAttack(List<Battleable> battleEntities);

    /**
     * Set this entity's battle state
     */
    void setBattleState(BattleState battleState);
}
