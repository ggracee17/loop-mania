package unsw.loopmania.entityFactory;

import org.javatuples.Pair;
import unsw.loopmania.Entity;
import unsw.loopmania.building.Spawner;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores a list of possible entity spawners.
 * Has method to get all entities to load
 */
public class EntityFactory {
    private final List<Spawner> spawnerList;

    public EntityFactory() {
        spawnerList = new ArrayList<>();
    }

    public void addSpawner(Spawner spawner) {
        spawnerList.add(spawner);
    }

    // Let the factory produce entities to spawn
    public List<Entity> getEntitiesToLoad(int cycleCount, List<Pair<Integer, Integer>> orderedPath) {
        List<Entity> entitiesToLoad = new ArrayList<>();
        for (Spawner spawner : spawnerList) {
            List<Entity> newEntities = spawner.Spawn(cycleCount, orderedPath);
            if (!newEntities.isEmpty()) {
                entitiesToLoad.addAll(newEntities);
            }
        }

        return entitiesToLoad;
    }
}
