package unsw.loopmania.item.Shields;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.StaticEntity;

public abstract class Shield extends StaticEntity {
    protected int price;

    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y, int price) {
        super(x, y);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    /**
     * @param damage The damage will deal to this entity
     * @return The actual damage dealt after applying the effect of this item
     */
    public abstract int defenseEffect(int damage);
}
