package unsw.loopmania.entityFactory;

import org.javatuples.Pair;
import unsw.loopmania.Entity;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.building.Spawner;

import java.util.ArrayList;
import java.util.List;

public class ElanMuskeSpawner implements Spawner {
    private final LoopManiaWorld world;

    public ElanMuskeSpawner(LoopManiaWorld world) {
        this.world = world;
    }

    @Override
    public List<Entity> Spawn(int cycleCount, List<Pair<Integer, Integer>> orderedPath) {
        return new ArrayList<>();
    }
}
