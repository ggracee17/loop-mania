package unsw.loopmania.item.Weapons;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Battleable;
import unsw.loopmania.StaticEntity;

public abstract class Weapon extends StaticEntity {
    protected int attackDamage;
    protected int price;

    public Weapon(SimpleIntegerProperty x, SimpleIntegerProperty y, int attackDamage, int price) {
        super(x, y);
        this.attackDamage = attackDamage;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    /**
     * This methods will return the damage it will deal to the opponent.
     *
     * If -1 is returned it means a special effect has been triggered
     * and there is no need to go on with the effect of other items.
     *
     * @param opponent The opponent to attack
     * @param damage   The damage will occur based on previous weapons or character's attack
     * @return The damage it will deal to the opponent
     */
    public abstract int attack(Battleable opponent, int damage);
}
