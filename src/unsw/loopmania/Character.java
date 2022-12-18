package unsw.loopmania;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.BattleEntityState.AlliedState;
import unsw.loopmania.BattleEntityState.BattleState;
import unsw.loopmania.Observer.CharacterObserver;
import unsw.loopmania.Observer.CharacterLocationPublisher;
import unsw.loopmania.item.Weapons.Weapon;
import unsw.loopmania.item.Armours.Armour;
import unsw.loopmania.item.Helmets.Helmet;
import unsw.loopmania.item.Shields.Shield;

import java.util.ArrayList;
import java.util.List;

/**
 * represents the main character in the backend of the game world
 */
public class Character extends MovingEntity implements Battleable, CharacterLocationPublisher {
    private IntegerProperty health;
    private int healthLimit;
    private int attackDamage;

    private final BattleState battleState;

    private List<CharacterObserver> observerList;

    // equipment slots
    private Weapon weapon;
    private Armour armour;
    private Shield shield;
    private Helmet helmet;

    // TODO = potentially implement relationships between this class and other classes
    public Character(PathPosition position) {
        super(position);
        health = new SimpleIntegerProperty(320);
        attackDamage = 18;
        healthLimit = 320;
        battleState = new AlliedState();

        observerList = new ArrayList<>();

        weapon = null;
        armour = null;
        shield = null;
        helmet = null;
    }

    /**
     * Setters for the equipment
     */
    public void setItem(Weapon weapon) {
        this.weapon = weapon;
    }
    public void setItem(Armour armour) {
        this.armour = armour;
    }
    public void setItem(Shield shield) {
        this.shield = shield;
    }
    public void setItem(Helmet helmet) {
        this.helmet = helmet;
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    public int getHealth() {
        return health.get();
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int damage) {
        attackDamage = damage;
    }

    public void setHealth(int health) {
        this.health.set(health);
    }

    public int getHealthLimit() {
        return healthLimit;
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
     * Character can only be in Allied state
     */
    @Override
    public void setBattleState(BattleState battleState) {
        return;
    }

    /**
     * Let the subject attack the opponent passed in
     *
     * @param opponent The opponent to attack
     */
    @Override
    public void attack(Battleable opponent) {
        int overallDamage = attackDamage;

        if (weapon != null) {
            overallDamage = weapon.attack(opponent, overallDamage);
        }

        if (overallDamage != -1 && helmet != null) {
            overallDamage = helmet.attackEffect(overallDamage);
        }

        if (overallDamage != -1) {
            opponent.defense(this, overallDamage);
        }
    }

    /**
     * Let the subject take damage from an attack
     *
     * @param opponent The opponent who performed the attack
     * @param damage   The damage made to the subject as calculated by the opponent
     */
    @Override
    public void defense(Battleable opponent, int damage) {
        int overallDamage = damage;
        if (armour != null) {
            overallDamage = armour.defenseEffect(overallDamage);
        }
        if (shield != null) {
            overallDamage = shield.defenseEffect(overallDamage);
        }
        if (helmet != null) {
            overallDamage = helmet.defenseEffect(overallDamage);
        }

        health.set(health.get() - overallDamage);
    }

    @Override
    public void moveDownPath() {
        super.moveDownPath();

        // Update the character's observers
        notifyObservers();
    }


    // 
    @Override
    public void subscribe(CharacterObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void unsubscribe(CharacterObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observerList.forEach(o -> o.update(this));
    }
}
