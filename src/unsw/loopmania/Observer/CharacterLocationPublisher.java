package unsw.loopmania.Observer;

public interface CharacterLocationPublisher {
    void subscribe(CharacterObserver observer);
    void unsubscribe(CharacterObserver observer);
    void notifyObservers();
}
