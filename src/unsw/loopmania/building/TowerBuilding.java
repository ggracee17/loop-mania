package unsw.loopmania.building;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Battleable;
import unsw.loopmania.BattleEntityState.AlliedState;
import unsw.loopmania.BattleEntityState.BattleState;

public class TowerBuilding extends Building implements Battleable {
    private final int attackValue;
    private final int supportRadius;

    private final BattleState battleState;

    public TowerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        attackValue = 25;
        supportRadius = 3;
        battleState = new AlliedState();
    }

    /**
     * Let Tower attack the opponent passed in.
     * @param opponent Teh opponent to attack.
     */
    @Override
    public void attack(Battleable opponent) {
        opponent.defense(this, attackValue);
    }


    /**
     * Tower will not take any damage.
     * @param opponent
     * @param damage
     */
    @Override
    public void defense(Battleable opponent, int damage) {
        return;
    }

    /**
     * Tower will never be defeated.
     */
    @Override
    public int getHealth() {
        return 1;
    }

    /**
     * @param x The x coordinate of the character
     * @param y The y coordinate of the character
     * @return whether the character is within the battle radius of this Tower
     */
    @Override
    public boolean isWithinBattleRadius(int x, int y) {
        return false;
    }

    /**
     * @param x The x coordinate of the character
     * @param y The y coordinate of the character
     * @return whether the character is within the support radius of this Tower
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
        return true;
    }


    @Override
    public boolean isEnemy() {
        return false;
    }


    @Override
    public List<Battleable> getEntityToAttack(List<Battleable> battleEntities) {
        return battleState.getEntityToAttack(battleEntities);
    } 


    @Override
    public void setBattleState(BattleState battleState) {
        return;
    }

}
