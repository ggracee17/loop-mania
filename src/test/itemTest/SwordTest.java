package test.itemTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import unsw.loopmania.Battleable;
import unsw.loopmania.BattleEntity.Zombie;
import unsw.loopmania.item.Weapons.Sword;
import unsw.loopmania.Character;

public class SwordTest {
    @Test
    public void simpleSwordTest() {
        Battleable zombie = new Zombie(null);
        Sword sword = new Sword(null,null);
        assertEquals(sword.attack(zombie, 0), 8);

    }
    
    @Test
    public void SwordAttackTest() {
        //without sword
        Sword sword = new Sword(null,null);
        Character character = new Character(null);
        Battleable zombie1 = new Zombie(null);
        character.attack(zombie1);
        assertEquals(zombie1.getHealth(), 12);

        //with sword
        character.setItem(sword);
        Battleable zombie2 = new Zombie(null);
        character.attack(zombie2);
        assertEquals(zombie2.getHealth(), 4);

    }

    @Test
    public void SwordDefenseTest() {
        //without sword
        Sword sword = new Sword(null,null);
        Character character = new Character(null);
        Battleable zombie = new Zombie(null);
        zombie.attack(character);
        assertEquals(character.getHealth(), 306);

        //with sword
        character.setItem(sword);
        zombie.attack(character);
        assertEquals(character.getHealth(), 292);


    }


}
