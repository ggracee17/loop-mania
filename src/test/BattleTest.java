package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import unsw.loopmania.Battle;
import unsw.loopmania.Battleable;
import unsw.loopmania.Character;
import unsw.loopmania.BattleEntity.Slug;
import unsw.loopmania.BattleEntity.Vampire;
import unsw.loopmania.BattleEntity.Zombie;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.item.Weapons.Sword;
import unsw.loopmania.item.Weapons.Stake;
import unsw.loopmania.item.Armours.BasicArmour;
import unsw.loopmania.item.Helmets.BasicHelmet;
import unsw.loopmania.item.Shields.BasicShield;


public class BattleTest {
    @Test
    public void basicBattleTest() {
        // Set up variable
        Character character = new Character(null);
        int characterHealth = character.getHealth();
        Battle battle = new Battle(character);

        Slug slug1 = new Slug(null);
        Slug slug2 = new Slug(null);
        Slug slug3 = new Slug(null);

        battle.addBattleEntity(slug1);
        battle.addBattleEntity(slug2);
        battle.addBattleEntity(slug3);

        List<Battleable> enemies = new ArrayList<>(Arrays.asList(slug1, slug2, slug3));

        // Before the battle start
        // assertEquals(battle.getBattleEntities(), enemies);
        assert(battle.getDefeatedEntities().isEmpty());

        battle.runBattle();

        // After the battle finishes
        assertEquals(enemies, battle.getDefeatedEntities());
        assertEquals(characterHealth - 6 * slug1.getAttackValue(), character.getHealth());
        for (Battleable enemy: enemies) assertEquals(enemy.getHealth(), -16);
    }

    @Test
    public void swordBattleTest() {
        // Set up variable
        Character character = new Character(null);
        character.setItem(new Sword(null, null));
        int characterHealth = character.getHealth();
        Battle battle = new Battle(character);

        Slug slug1 = new Slug(null);
        Slug slug2 = new Slug(null);
        Slug slug3 = new Slug(null);

        battle.addBattleEntity(slug1);
        battle.addBattleEntity(slug2);
        battle.addBattleEntity(slug3);

        List<Battleable> enemies = new ArrayList<>(Arrays.asList(slug1, slug2, slug3));

        // Before the battle start
        // assertEquals(battle.getBattleEntities(), enemies);
        assert(battle.getDefeatedEntities().isEmpty());

        battle.runBattle();

        // After the battle finishes
        assertEquals(enemies, battle.getDefeatedEntities());
        assertEquals(characterHealth - 3 * slug1.getAttackValue(), character.getHealth());
        for (Battleable enemy: enemies) assertEquals(enemy.getHealth(), -6);
    }

    @Test
    public void stakeBattleTest() {
        // Set up variable
        Character character = new Character(null);
        character.setItem(new Stake(null, null));
        int characterHealth = character.getHealth();
        Battle battle = new Battle(character);

        Slug slug1 = new Slug(null);
        Vampire vampire1 = new Vampire(null);

        battle.addBattleEntity(slug1);
        battle.addBattleEntity(vampire1);

        List<Battleable> enemies = new ArrayList<>(Arrays.asList(slug1, vampire1));

        // Before the battle start
        // assertEquals(battle.getBattleEntities(), enemies);
        assert(battle.getDefeatedEntities().isEmpty());

        battle.runBattle();

        // After the battle finishes
        assertEquals(enemies, battle.getDefeatedEntities());
        assertEquals(characterHealth - 1 * slug1.getAttackValue() - 3 * vampire1.getAttackValue(), character.getHealth());
        assertEquals(vampire1.getHealth(), -6);
        assertEquals(slug1.getHealth(), -3);

    }

    @Test
    public void staffBattleTest() {

    }

    @Test
    public void armourBattleTest() {
        Character character = new Character(null);
        character.setItem(new BasicArmour(null, null));
        int characterHealth = character.getHealth();
        Battle battle = new Battle(character);

        Slug slug1 = new Slug(null);
        Vampire vampire1 = new Vampire(null);

        battle.addBattleEntity(slug1);
        battle.addBattleEntity(vampire1);

        List<Battleable> enemies = new ArrayList<>(Arrays.asList(slug1, vampire1));

        // Before the battle start
        // assertEquals(battle.getBattleEntities(), enemies);
        assert(battle.getDefeatedEntities().isEmpty());

        battle.runBattle();

        // After the battle finishes
        assertEquals(enemies, battle.getDefeatedEntities());
        assertEquals(characterHealth - 2 * slug1.getAttackValue()/2 - 5 * vampire1.getAttackValue()/2, character.getHealth());
        assertEquals(slug1.getHealth(), -16);
        assertEquals(vampire1.getHealth(), 0);

    }

    @Test
    public void shieldBattleTest() {
        // Set up variable
        Character character = new Character(null);
        character.setItem(new BasicShield(null, null));
        int characterHealth = character.getHealth();
        Battle battle = new Battle(character);

        Slug slug1 = new Slug(null);
        Zombie zombie1 = new Zombie(null);

        battle.addBattleEntity(slug1);
        battle.addBattleEntity(zombie1);

        List<Battleable> enemies = new ArrayList<>(Arrays.asList(slug1, zombie1));

        // Before the battle start
        // assertEquals(battle.getBattleEntities(), enemies);
        assert(battle.getDefeatedEntities().isEmpty());

        battle.runBattle();

        // After the battle finishes
        assertEquals(enemies, battle.getDefeatedEntities());
        assertEquals(characterHealth - 2 * (int)(0.8 * slug1.getAttackValue()) -2 * (int)(0.8 * zombie1.getAttackValue()), character.getHealth());
        assertEquals(slug1.getHealth(), -16);
        assertEquals(zombie1.getHealth(), -6);
    }

    @Test
    public void helmetBattleTest() {
        // Set up variable
        Character character = new Character(null);
        character.setItem(new BasicHelmet(null, null));
        int characterHealth = character.getHealth();
        Battle battle = new Battle(character);

        Slug slug1 = new Slug(null);
        Zombie zombie1 = new Zombie(null);
        Vampire vampire1 = new Vampire(null);

        battle.addBattleEntity(slug1);
        battle.addBattleEntity(zombie1);
        battle.addBattleEntity(vampire1);

        List<Battleable> enemies = new ArrayList<>(Arrays.asList(slug1, zombie1, vampire1));

        // Before the battle start
        // assertEquals(battle.getBattleEntities(), enemies);
        assert(battle.getDefeatedEntities().isEmpty());

        battle.runBattle();

        // After the battle finishes
        assertEquals(enemies, battle.getDefeatedEntities());
        assertEquals(characterHealth - 2 * (slug1.getAttackValue() - 5) - 3 * (zombie1.getAttackValue() - 5) - 7 * (vampire1.getAttackValue() - 5), character.getHealth());
        assertEquals(slug1.getHealth(), -6);
        assertEquals(zombie1.getHealth(), -9);
        assertEquals(vampire1.getHealth(), -1);
    }

    @Test
    public void swordHelmetBattleTest() {
        // Set up variable
        Character character = new Character(null);
        character.setItem(new Sword(null, null));
        character.setItem(new BasicHelmet(null, null));
        int characterHealth = character.getHealth();
        Battle battle = new Battle(character);

        Slug slug1 = new Slug(null);
        Zombie zombie1 = new Zombie(null);
        Vampire vampire1 = new Vampire(null);

        battle.addBattleEntity(slug1);
        battle.addBattleEntity(zombie1);
        battle.addBattleEntity(vampire1);

        List<Battleable> enemies = new ArrayList<>(Arrays.asList(slug1, zombie1, vampire1));

        // Before the battle start
        // assertEquals(battle.getBattleEntities(), enemies);
        assert(battle.getDefeatedEntities().isEmpty());

        battle.runBattle();

        // After the battle finishes
        assertEquals(enemies, battle.getDefeatedEntities());
        assertEquals(characterHealth - (slug1.getAttackValue() - 5) - 2 * (zombie1.getAttackValue() - 5) - 5 * (vampire1.getAttackValue() - 5), character.getHealth());
        assertEquals(slug1.getHealth(), -1);
        assertEquals(zombie1.getHealth(), -12);
        assertEquals(vampire1.getHealth(), -15);
    }


    @Test
    public void towerBattleTest() {
        // Set up variable
        Character character = new Character(null);
        int characterHealth = character.getHealth();
        Battle battle = new Battle(character);
        TowerBuilding tower = new TowerBuilding(null,null);
        battle.addBattleEntity(tower);

        Slug slug1 = new Slug(null);
        Zombie zombie1 = new Zombie(null);
        Vampire vampire1 = new Vampire(null);

        battle.addBattleEntity(slug1);
        battle.addBattleEntity(zombie1);
        battle.addBattleEntity(vampire1);

        List<Battleable> enemies = new ArrayList<>(Arrays.asList(slug1, zombie1, vampire1));

        // Before the battle start
        // assertEquals(battle.getBattleEntities(), enemies);
        assert(battle.getDefeatedEntities().isEmpty());

        battle.runBattle();

        // After the battle finishes
        assertEquals(enemies, battle.getDefeatedEntities());
        assertEquals(characterHealth - slug1.getAttackValue() -  zombie1.getAttackValue() - 3 * vampire1.getAttackValue(), character.getHealth());
        assertEquals(slug1.getHealth(), -23);
        assertEquals(zombie1.getHealth(), -13);
        assertEquals(vampire1.getHealth(), -39);
    }

}
