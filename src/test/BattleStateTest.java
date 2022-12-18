package test;

import org.junit.jupiter.api.Test;
import unsw.loopmania.*;
import unsw.loopmania.BattleEntityState.AlliedState;
import unsw.loopmania.BattleEntityState.EnemyState;
import unsw.loopmania.Character;
import unsw.loopmania.BattleEntity.Slug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleStateTest {
    @Test
    public void getEntityToAttackAlliedTest() {
        Character character1 = new Character(null);
        Character character2 = new Character(null);
        Slug slug1 = new Slug(null);
        Slug slug2 = new Slug(null);
        List<Battleable> battleEntities = new ArrayList<>(Arrays.asList(character1, slug1, slug2, character2));

        assertEquals(Arrays.asList(slug1, slug2), new AlliedState().getEntityToAttack(battleEntities));
        assertEquals(Arrays.asList(character1, character2), new EnemyState().getEntityToAttack(battleEntities));
    }
}
