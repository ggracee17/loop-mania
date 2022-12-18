package unsw.loopmania.building;

import org.javatuples.Pair;
import unsw.loopmania.Entity;

import java.util.List;

/**
 * A spawner for spawning entities on the map
 */
public interface Spawner {
    /**
     * Get the list of entity that this spawner want to spawn
     * @param cycleCount  The number of cycle
     * @param orderedPath The ordered path
     * @return list of entities to spawn
     */
    List<Entity> Spawn(int cycleCount, List<Pair<Integer, Integer>> orderedPath);
}
