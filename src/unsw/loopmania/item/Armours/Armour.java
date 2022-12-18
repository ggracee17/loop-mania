package unsw.loopmania.item.Armours;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.StaticEntity;

public abstract class Armour extends StaticEntity {
    protected int price;
    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y, int price) {
        super(x, y);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public abstract int defenseEffect(int overallDamage);
}
