package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import unsw.loopmania.Battleable;
import unsw.loopmania.BattleEntity.Slug;
import unsw.loopmania.building.TowerBuilding;

public class TowerTest {
    @Test
    public void simpleTowerTest() {
        TowerBuilding tower = new TowerBuilding(null,null);
        Battleable slug = new Slug(null);
        int slugHealth = slug.getHealth();
        tower.attack(slug);
        assertEquals(slug.getHealth(), slugHealth-25);
    }
}
