package test;

import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.Test;
import unsw.loopmania.BattleEntity.AlliedSoldier;
import unsw.loopmania.BattleEntity.Slug;

import static org.junit.jupiter.api.Assertions.*;

public class AlliedSoldierTest {
    @Test
    public void simpleAttackTest() {
        AlliedSoldier alliedSoldier = new AlliedSoldier(new SimpleIntegerProperty(0),
                new SimpleIntegerProperty(0));
        Slug slug = new Slug(null);
        int slugHealth = slug.getHealth();

        alliedSoldier.attack(slug);
        assertEquals(slugHealth - 10, slug.getHealth());
    }

    @Test
    public void simpleDefenseTest() {
        AlliedSoldier alliedSoldier = new AlliedSoldier(new SimpleIntegerProperty(0),
                new SimpleIntegerProperty(0));
        int originalHealth = alliedSoldier.getHealth();
        Slug slug = new Slug(null);

        alliedSoldier.defense(slug, 8);
        assertEquals(originalHealth - 8, alliedSoldier.getHealth());
    }

    @Test
    public void isAlliedEnemyTest() {
        AlliedSoldier alliedSoldier = new AlliedSoldier(new SimpleIntegerProperty(0),
                new SimpleIntegerProperty(0));

        assertTrue(alliedSoldier.isAllied());
        assertFalse(alliedSoldier.isEnemy());
    }
}
