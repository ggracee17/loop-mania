package unsw.loopmania;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Battle Sequence:
 */
public class Battle {
    private Character character;
    private final List<Battleable> battleEntities;
    private final List<Battleable> defeatedEntities;

    public Battle(Character character) {
        this.character = character;
        battleEntities = new ArrayList<>();
        defeatedEntities = new ArrayList<>();

        battleEntities.add(character);
    }

    /**
     * @return All entities in the battle
     */
    public List<Battleable> getBattleEntities() {
        return battleEntities;
    }

    public List<Battleable> getDefeatedEntities() {
        return defeatedEntities;
    }

    public void addBattleEntity(Battleable battleEntity) {
        battleEntities.add(battleEntity);
    }

    /**
     * Battle order: Entities attack their opponent in the order they are added to the world
     */
    public void runBattle() {
        // While character is not defeated and there is enemy state entity in the battle
        while (battleEntities.contains(character) && battleEntities.stream().anyMatch(Battleable::isEnemy)) {
            for (Battleable e : battleEntities) {
                // Attack all opponents
                List<Battleable> entitiesToAttack = e.getEntityToAttack(battleEntities);
                entitiesToAttack.forEach(e::attack);
            }

            // After every entity has their turn, note down the defeated entities
            List<Battleable> deadEntities = battleEntities.stream()
                    .filter(e -> e.getHealth() <= 0)
                    .collect(Collectors.toList());

            // Remove them from the entities that can be in the battle
            battleEntities.removeAll(deadEntities);

            defeatedEntities.addAll(deadEntities);
        }
    }
}
