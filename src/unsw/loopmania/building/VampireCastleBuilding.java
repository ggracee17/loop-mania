package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;
import unsw.loopmania.BattleEntity.Vampire;
import unsw.loopmania.Entity;
import unsw.loopmania.PathPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * a basic form of building in the world
 */
public class VampireCastleBuilding extends Building implements Spawner {
    private final Pair<Integer, Integer> spawnPosition;

    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, Pair<Integer,
            Integer> spawnPosition) {
        super(x, y);
        assert(spawnPosition != null);
        this.spawnPosition = spawnPosition;
    }

    // spawn a vampire next to the building on the path
    @Override
    public List<Entity> Spawn(int cycleCount, List<Pair<Integer, Integer>> orderedPath) {
        List<Entity> vampires = new ArrayList<>();
        if (!(cycleCount % 5 == 0)) return vampires;

        vampires.add(new Vampire(new PathPosition(orderedPath.indexOf(spawnPosition), orderedPath)));
        return vampires;
    }
}
