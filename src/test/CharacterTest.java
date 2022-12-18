package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.loopmania.Character;
import unsw.loopmania.BattleEntity.Slug;

public class CharacterTest {
    @Test
    public void simpleAttackTest() {
        Character character = new Character(null);
        Slug slug = new Slug(null);
        int slugHealth = slug.getHealth();

        character.attack(slug);
        assertEquals(slugHealth - 18, slug.getHealth());
    }

    @Test
    public void simpleDefenseTest() {
        Character character = new Character(null);
        int originalHealth = character.getHealth();
        Slug slug = new Slug(null);

        character.defense(slug, 8);
        assertEquals(originalHealth - 8, character.getHealth());
    }
}
