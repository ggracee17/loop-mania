package unsw.loopmania.BattleEntityState;

import unsw.loopmania.Battleable;

import java.util.List;
import java.util.stream.Collectors;

public class EnemyState implements BattleState {

    @Override
    public List<Battleable> getEntityToAttack(List<Battleable> battleEntities) {
        return battleEntities.stream().filter(Battleable::isAllied).collect(Collectors.toList());
    }
}
