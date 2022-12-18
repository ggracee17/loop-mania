package unsw.loopmania.Observer;
import unsw.loopmania.Character;

public interface CharacterObserver {
    // This method is called whenever the character's position is changed.
    void update(Character character);
}
