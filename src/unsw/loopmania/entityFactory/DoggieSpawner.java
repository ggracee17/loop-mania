package unsw.loopmania.entityFactory;

import org.javatuples.Pair;
import unsw.loopmania.BattleEntity.Doggie;
import unsw.loopmania.Entity;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.building.Spawner;

import java.util.ArrayList;
import java.util.List;

public class DoggieSpawner implements Spawner {
    private final LoopManiaWorld world;

    public DoggieSpawner(LoopManiaWorld world) {
        this.world = world;
    }

    @Override
    public List<Entity> Spawn(int cycleCount, List<Pair<Integer, Integer>> orderedPath) {
        List<Entity> doggies = new ArrayList<>();

        // Doggie spawns in the 20, 25, 30-th ... cycle
        if (cycleCount < 20 || cycleCount % 5 != 0) {
            return doggies;
        }

        doggies.add(new Doggie(new PathPosition(orderedPath.indexOf(world.getPositionToSpawnEnemy()), orderedPath)));
        return doggies;
    }
}
