package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.loopmania.Character;
import unsw.loopmania.BattleEntity.Zombie;

public class ZombieTest {
    @Test
    public void simpleAttackTest() {
        Zombie zombie = new Zombie(null);
        Character character = new Character(null);
        int characterHealth = character.getHealth();

        zombie.attack(character);
        assertEquals(characterHealth - 14, character.getHealth());
    }

    @Test
    public void simpleDefenseTest() {
        Zombie zombie = new Zombie(null);
        int originalHealth = zombie.getHealth();
        Character character = new Character(null);

        zombie.defense(character, 18);
        assertEquals(originalHealth - 18, zombie.getHealth());
    }
}
