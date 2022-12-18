package unsw.loopmania.item.Helmets;

import javafx.beans.property.SimpleIntegerProperty;

public class BasicHelmet extends Helmet {
    public BasicHelmet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, 350);
    }

    @Override
    public int attackEffect(int overallAttack) {
        // damage inflicted by the Character against enemies is reduced
        return overallAttack - 5;
    }

    @Override
    public int defenseEffect(int overallDamage) {
        // enemy attacks are reduced by a scalar value
        return overallDamage - 5;
    }   
}
