package test.itemTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import unsw.loopmania.BattleEntity.Slug;
import unsw.loopmania.item.Helmets.BasicHelmet;
import unsw.loopmania.Character;

public class BasicHelmetTest {
    @Test
    public void helmetAttackTest() {
        // Without helmet
        Character character = new Character(null);
        Slug slug1 = new Slug(null);
        character.attack(slug1);
        assertEquals(slug1.getHealth(), 2);

        // With helmet
        character.setItem(new BasicHelmet(null, null));
        Slug slug2 = new Slug(null);
        character.attack(slug2);
        assertEquals(slug2.getHealth(), 7);
    }

    @Test
    public void helmetDefenceTest() {
        // Without helmet
        Character character = new Character(null);
        Slug slug = new Slug(null);
        slug.attack(character);
        assertEquals(character.getHealth(), 312);

        // With helmet
        character.setItem(new BasicHelmet(null, null));
        slug.attack(character);
        assertEquals(character.getHealth(), 309);
    }
}
