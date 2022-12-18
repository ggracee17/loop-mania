package unsw.loopmania.BattleEntityState;

import unsw.loopmania.Battleable;

import java.util.List;

/**
 * All battleable entities must contains one of the BattleState
 */
public interface BattleState {
    /**
     * @param battleableList List of all battleable entities in a battle
     * @return A list containing all the current entity's opponent
     */
    List<Battleable> getEntityToAttack(List<Battleable> battleableList);
}
