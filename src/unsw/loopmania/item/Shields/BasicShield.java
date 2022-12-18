package unsw.loopmania.item.Shields;

import javafx.beans.property.SimpleIntegerProperty;

public class BasicShield extends Shield {
    public BasicShield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, 450);
    }

    @Override
    public int defenseEffect(int damage) {
        return (int) (damage * 0.8);
    }
}
