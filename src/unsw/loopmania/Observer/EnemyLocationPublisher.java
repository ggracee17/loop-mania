package unsw.loopmania.Observer;

public interface EnemyLocationPublisher {
    void subscribe(EnemyObserver observer);
    void unsubscribe(EnemyObserver observer);
    void notifyObservers();
}
