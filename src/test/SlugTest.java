package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.loopmania.Character;
import unsw.loopmania.BattleEntity.Slug;

public class SlugTest {
    @Test
    public void simpleAttackTest() {
        Slug slug = new Slug(null);
        Character character = new Character(null);
        int characterHealth = character.getHealth();

        slug.attack(character);
        assertEquals(characterHealth - 8, character.getHealth());
    }

    @Test
    public void simpleDefenseTest() {
        Slug slug = new Slug(null);
        int originalHealth = slug.getHealth();
        Character character = new Character(null);

        slug.defense(character, 18);
        assertEquals(originalHealth - 18, slug.getHealth());
    }
}