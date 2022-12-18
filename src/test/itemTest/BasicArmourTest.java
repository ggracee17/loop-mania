package test.itemTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import unsw.loopmania.BattleEntity.Zombie;
import unsw.loopmania.item.Armours.BasicArmour;
import unsw.loopmania.Character;

public class BasicArmourTest {
    @Test
    public void armourAttackTest() {
        //without armour
        Character character = new Character(null);
        BasicArmour armour = new BasicArmour(null, null);
        Zombie zombie1 = new Zombie(null);
        character.attack(zombie1);
        assertEquals(zombie1.getHealth(), 12);
        //assertEquals(armour.defenseEffect(slug.getAttackValue()), 4);

        //with armour
        character.setItem(armour);
        Zombie zombie2 = new Zombie(null);
        character.attack(zombie2);
        assertEquals(zombie2.getHealth(), 12);

    }
    @Test
    public void armourDefenseTest() {
        //without armour
        Character character = new Character(null);
        BasicArmour armour = new BasicArmour(null, null);
        Zombie zombie = new Zombie(null);
        zombie.attack(character);
        assertEquals(character.getHealth(), 306);
        //assertEquals(armour.defenseEffect(slug.getAttackValue()), 4);

        //with armour
        character.setItem(armour);
        zombie.attack(character);
        assertEquals(character.getHealth(), 299);

    }

    
}
