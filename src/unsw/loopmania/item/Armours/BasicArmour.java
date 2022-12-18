package unsw.loopmania.item.Armours;

import javafx.beans.property.SimpleIntegerProperty;

public class BasicArmour extends Armour {
    public BasicArmour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, 600);
    }

    /**
     * @param overallDamage The damage will deal to this entity
     * @return The actual damage dealt after applying the effect of this item
     */
    @Override
    public int defenseEffect(int overallDamage) {
        return overallDamage/2;
    }
}
