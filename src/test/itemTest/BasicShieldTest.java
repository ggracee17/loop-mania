package test.itemTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import unsw.loopmania.BattleEntity.Slug;
import unsw.loopmania.BattleEntity.Vampire;
import unsw.loopmania.item.Shields.BasicShield;
import unsw.loopmania.Character;

public class BasicShieldTest {
    @Test
    public void shieldAttackTest() {
        //without shield
        BasicShield shield = new BasicShield(null,null);
        Character character = new Character(null);
        Slug slug1 = new Slug(null);
        character.attack(slug1);
        assertEquals(slug1.getHealth(), 2);

        //with shield
        character.setItem(shield);
        Slug slug2 = new Slug(null);
        character.attack(slug2);
        assertEquals(slug1.getHealth(), 2);

    }

    @Test
    public void shieldDefenseTest() {
        //without shield
        BasicShield shield = new BasicShield(null,null);
        Character character = new Character(null);
        Slug slug = new Slug(null);
        slug.attack(character);
        assertEquals(character.getHealth(), 312);

        //with shield
        character.setItem(shield);
        slug.attack(character);
        assertEquals(character.getHealth(), 306);

    }
    

    @Test
    public void vampireShieldTest() {
        BasicShield shield = new BasicShield(null,null);
        Character character = new Character(null);
        Vampire vampire = new Vampire(null);
        vampire.attack(character);
        assertEquals(character.getHealth(), 300);
        character.setItem(shield);
        vampire.attack(character);
        assertEquals(character.getHealth(), 284);
        character.attack(vampire);
        assertEquals(vampire.getHealth(), 72);

    }
}
