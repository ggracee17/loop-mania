package unsw.loopmania.entityFactory;

import org.javatuples.Pair;
import unsw.loopmania.BattleEntity.Slug;
import unsw.loopmania.Entity;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.building.Spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlugSpawner implements Spawner {
    private final LoopManiaWorld world;

    public SlugSpawner(LoopManiaWorld world) {
        this.world = world;
    }

    @Override
    public List<Entity> Spawn(int cycleCount, List<Pair<Integer, Integer>> orderedPath) {
        List<Entity> slugs = new ArrayList<>();

        // Spawn 1 - 3 Slugs on random location
        for (int numSlug = new Random().nextInt(2); numSlug >= 0; --numSlug) {
            slugs.add(new Slug(new PathPosition(orderedPath.indexOf(world.getPositionToSpawnEnemy()), orderedPath)));
        }

        return slugs;
    }
}
