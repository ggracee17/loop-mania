package unsw.loopmania.item.Helmets;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.StaticEntity;

public abstract class Helmet extends StaticEntity {
    protected int price;
    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y, int price) {
        super(x, y);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    /**
     * @param overallAttack The damage will deal to this entity's opponent
     * @return The actual attackDamage after applying the effect of this item
     */
    public abstract int attackEffect(int overallAttack);

    /**
     * @param overallDamage The damage will deal to this entity
     * @return The actual damage dealt after applying the effect of this item
     */
    public abstract int defenseEffect(int overallDamage);
}
