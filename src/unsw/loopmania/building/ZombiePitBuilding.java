package unsw.loopmania.building;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Entity;
import unsw.loopmania.PathPosition;
import unsw.loopmania.BattleEntity.Zombie;

public class ZombiePitBuilding extends Building implements Spawner {
    private final Pair<Integer, Integer> spawnPosition;

    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse them
     */
    public ZombiePitBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y, Pair<Integer,
            Integer> spawnPosition) {
        super(x, y);
        assert(spawnPosition != null);
        this.spawnPosition = spawnPosition;
    }  

    // spawn a zombie next to the building on the path
    @Override
    public List<Entity> Spawn(int cycleCount, List<Pair<Integer, Integer>> orderedPath) {
        List<Entity> zombies = new ArrayList<>();
        if (!(cycleCount >= 1)) return zombies;
        
        zombies.add(new Zombie(new PathPosition(orderedPath.indexOf(spawnPosition), orderedPath)));
        return zombies;
    }
}
