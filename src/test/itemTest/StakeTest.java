package test.itemTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import unsw.loopmania.Battleable;
import unsw.loopmania.BattleEntity.Slug;
import unsw.loopmania.BattleEntity.Zombie;
import unsw.loopmania.BattleEntity.Vampire;
import unsw.loopmania.item.Weapons.Stake;
import unsw.loopmania.Character;

public class StakeTest {
    @Test
    public void normalStakeTest() {
        Stake stake = new Stake(null,null);
        Battleable slug = new Slug(null);
        assertEquals(stake.attack(slug, 0), 5);
    }

    @Test
    public void StakeAttackTest() {
        //without stake
        Stake stake = new Stake(null,null);
        Character character = new Character(null);
        Battleable zombie1 = new Zombie(null);
        character.attack(zombie1);
        assertEquals(zombie1.getHealth(), 12);

        //with stake
        character.setItem(stake);
        Battleable zombie2 = new Zombie(null);
        character.attack(zombie2);
        assertEquals(zombie2.getHealth(), 7);
    }

    @Test
    public void StakeDefenseTest() {
        //without stake
        Stake stake = new Stake(null,null);
        Character character = new Character(null);
        Battleable zombie = new Zombie(null);
        zombie.attack(character);
        assertEquals(character.getHealth(), 306);

        //with stake
        character.setItem(stake);
        zombie.attack(character);
        assertEquals(character.getHealth(), 292);
    }



    @Test
    public void vampireStakeTest() {
        Stake stake = new Stake(null,null);
        Battleable vampire = new Vampire(null);
        assertEquals(stake.attack(vampire, 0), 7);

        Character character = new Character(null);
        character.setItem(stake);
        vampire.attack(character);
        assertEquals(character.getHealth(), 300);
        
        character.attack(vampire);
        assertEquals(vampire.getHealth(), 58);

    }
}
