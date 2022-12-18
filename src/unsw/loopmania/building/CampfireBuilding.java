package unsw.loopmania.building;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Battleable;
import unsw.loopmania.BattleEntityState.BattleState;

public class CampfireBuilding extends Building implements Battleable {
    private final int battleRadius;

    public CampfireBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        battleRadius = 3;
    }

    @Override
    public void attack(Battleable opponent) {
        return;
    }

    @Override
    public void defense(Battleable opponent, int damage) {
        return;
    }

    @Override
    public int getHealth() {
        return 1;
    }

    @Override
    public boolean isWithinBattleRadius(int x, int y) {
        return Math.pow((getX() - x), 2) + Math.pow((getY() - y), 2) <= battleRadius;
    }

    @Override
    public boolean isWithinSupportRadius(int x, int y) {
        return false;
    }

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
        return new ArrayList<>();
    }

    @Override
    public void setBattleState(BattleState battleState) {
        return;
    }
}
