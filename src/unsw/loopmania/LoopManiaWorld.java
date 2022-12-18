package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import org.javatuples.Pair;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import unsw.loopmania.BattleEntity.AlliedSoldier;
import unsw.loopmania.BattleEntity.Enemy;
import unsw.loopmania.BattleEntity.Slug;
import unsw.loopmania.building.*;
import unsw.loopmania.card.BarrackCard;
import unsw.loopmania.card.CampfireCard;
import unsw.loopmania.card.Card;
import unsw.loopmania.card.TowerCard;
import unsw.loopmania.card.TrapCard;
import unsw.loopmania.card.VampireCastleCard;
import unsw.loopmania.card.VillageCard;
import unsw.loopmania.card.ZombiePitCard;
import unsw.loopmania.entityFactory.DoggieSpawner;
import unsw.loopmania.entityFactory.ElanMuskeSpawner;
import unsw.loopmania.entityFactory.EntityFactory;
import unsw.loopmania.entityFactory.SlugSpawner;
import unsw.loopmania.item.Armours.Armour;
import unsw.loopmania.item.Armours.BasicArmour;
import unsw.loopmania.item.Helmets.BasicHelmet;
import unsw.loopmania.item.Helmets.Helmet;
import unsw.loopmania.item.Shields.BasicShield;
import unsw.loopmania.item.Shields.Shield;
import unsw.loopmania.item.Weapons.Staff;
import unsw.loopmania.item.Weapons.Stake;
import unsw.loopmania.item.Weapons.Sword;
import unsw.loopmania.item.Weapons.Weapon;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 */
public class LoopManiaWorld {
    // TODO = add additional backend functionality

    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;

    /**
     * width of the world in GridPane cells
     */
    private final int width;

    /**
     * height of the world in GridPane cells
     */
    private final int height;

    /**
     * generic entities - i.e. those which don't have dedicated fields
     */
    private final List<Entity> nonSpecifiedEntities;

    private Character character;

    private final List<Enemy> enemies;

    private final List<Card> cardEntities;

    private final List<StaticEntity> unequippedInventoryItems;

    private final List<Building> buildingEntities;

    private ShoppingCart shoppingList;

    // TODO = expand the range of buildings

    private List<Spawner> spawningEntities;

    private final List<TowerBuilding> towers;

    private final List<CampfireBuilding> campfires;

    private final ObservableList<AlliedSoldier> alliedSoldiers;

    private final EntityFactory entityFactory;

    private List<TrapBuilding> traps;
    private List<Enemy> trappedEnemies;

    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse them
     */
    private final List<Pair<Integer, Integer>> orderedPath;

    /* World States */
    private final IntegerProperty cycleCount;
    private final IntegerProperty numHealthPotion;
    private final IntegerProperty numGold;
    private final IntegerProperty numXP;

    /**
     * create the world (constructor)
     * 
     * @param width width of world in number of cells
     * @param height height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        this.width = width;
        this.height = height;
        nonSpecifiedEntities = new ArrayList<>();
        character = null;
        enemies = new ArrayList<>();
        cardEntities = new ArrayList<>();
        unequippedInventoryItems = new ArrayList<StaticEntity>();
        shoppingList = new ShoppingCart();
        buildingEntities = new ArrayList<>();
        towers = new ArrayList<>();
        campfires = new ArrayList<>();
        this.orderedPath = orderedPath;
        alliedSoldiers = FXCollections.observableArrayList();

        traps = new ArrayList<>();
        trappedEnemies = new ArrayList<>();

        // Initialize with a slug spawner, doggie spawner and elan muske spawner
        entityFactory = new EntityFactory();
        entityFactory.addSpawner(new SlugSpawner(this));
        entityFactory.addSpawner(new DoggieSpawner(this));
        entityFactory.addSpawner(new ElanMuskeSpawner(this));

        // World states
        cycleCount = new SimpleIntegerProperty(0);
        numHealthPotion = new SimpleIntegerProperty(0);
        numGold = new SimpleIntegerProperty(0);
        numXP = new SimpleIntegerProperty(0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Pair<Integer, Integer>> getOrderedPath() {
        return orderedPath;
    }

    /* Getters for the world states */
    public IntegerProperty cycleCountProperty() {
        return cycleCount;
    }

    public IntegerProperty numHealthPotionProperty() {
        return numHealthPotion;
    }

    public IntegerProperty numGoldProperty() {
        return numGold;
    }

    public IntegerProperty numXPProperty() {
        return numXP;
    }

    public StringBinding numAlliedSoldiersProperty() {
        return Bindings.size(alliedSoldiers).asString();
    }
    
    public void loadHealthPotion() {
        numHealthPotion.set(numHealthPotion.get() + 1);
    }

    /**
     * Consume one potion to refill health.
     */
    public void takeHealthPotion() {
        if (numHealthPotion.get() > 0) {
            numHealthPotion.set(numHealthPotion.get() - 1);
            character.setHealth(character.getHealthLimit());
        }
    }

    /**
     * Gain gold as reward after winning a battle 
     */
    public void gainGold(Enemy enemy) {
        numGold.set(numGold.get()+enemy.getGoldReward());
    }

    /**
     * Gain XP as reward after winning a battle.
     */
    public void gainXP() {
        Random rand = new Random();
        int max = 10;
        int min = 1;
        int randPercent = rand.nextInt(max - min + 1) + min; // generate a random int between 1-10
        int charXP = numXP.get();
        if (charXP == 0) {
            numXP.set(100); // the first enemy defeated offers 100 XP
        } else {
            // character gain 0 ~ 10% of his XP each time
            numXP.set(charXP  + (int)(charXP * randPercent / 100));
        }
    }

    public void addEntitySpawner(Spawner spawner) {
        entityFactory.addSpawner(spawner);
    }

    public void addTower(TowerBuilding tower) {
        towers.add(tower);
    }

    public void addCampfire(CampfireBuilding campfire) {
        campfires.add(campfire);
    }

    public void oldCardtoGold() {
        numGold.set(numGold.get() + 30);
    }


    public void oldCardtoXP() {
        numXP.set(numXP.get() + 50);
    }

    public void oldEquipmenttoXP() {
        numXP.set(numXP.get() + 200);
    }


    public void addTrap(TrapBuilding trap) {
        traps.add(trap);
    }


    /**
     * set the character. This is necessary because it is loaded as a special entity out of the file
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    /**
     * add a generic entity (without it's own dedicated method for adding to the world)
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        // TODO = if more specialised types being added from main menu, add more methods like this with specific input types...
        nonSpecifiedEntities.add(entity);
    }

    public void addAlliedSoldier(AlliedSoldier alliedSoldier) {
        //
        alliedSoldiers.add(alliedSoldier);
    }

    public List<AlliedSoldier> getAlliedSoldiers() {
        return alliedSoldiers;
    }


    public List<Enemy> getTrappedEnemies() {
        return trappedEnemies;
    }

    public List<TrapBuilding> getTraps() {
        return traps;
    }

    public void addTrappedEnemies(Enemy e) {
        trappedEnemies.add(e);
    }

    public void clearTrappedEnemies() {
        trappedEnemies = new ArrayList<>();
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     * @return list of the enemies to be displayed on screen
     */
    public List<Enemy> possiblySpawnEnemies(){
        // TODO = expand this very basic version
        Pair<Integer, Integer> pos = possiblyGetEnemySpawnPosition();
        List<Enemy> spawningEnemies = new ArrayList<>();
        if (pos != null){
            int indexInPath = orderedPath.indexOf(pos);
            Enemy slug = new Slug(new PathPosition(indexInPath, orderedPath));
            traps.forEach(slug::subscribe);
            enemies.add(slug);
            spawningEnemies.add(slug);
        }
        return spawningEnemies;
    }

    /**
     * kill an enemy
     * @param enemy enemy to be killed
     */
    public void killEnemy(Enemy enemy){
        enemy.destroy();
        enemies.remove(enemy);
    }

    /**
     * Remove an allied soldier from the backend world
     */
    private void killAlliedSoldier(AlliedSoldier alliedSoldier) {
        alliedSoldier.destroy();
        alliedSoldiers.remove(alliedSoldier);
    }

    public void killTrap(TrapBuilding trap) {
        trap.destroy();
        buildingEntities.remove(trap);
    }

    /**
     * run the expected battles in the world, based on current world state
     * @return list of enemies which have been killed
     */
    public List<Enemy> runBattles() {
        List<Enemy> defeatedEnemies = new ArrayList<>();

        // If there is no enemy within the battle radius with character
        if (enemies.stream().noneMatch(enemy -> enemy.isWithinBattleRadius(character.getX(), character.getY()))) {
            return defeatedEnemies;
        }

        // Instantiate a battle and add all enemies in battle/ support radius
        Battle battle = new Battle(character);

        // double attack damage if in campfire's battle radius
        boolean withCampfire = false;
        for (CampfireBuilding campfire: campfires) {
            if (campfire.isWithinBattleRadius(character.getX(), character.getY())) {
                withCampfire = true;
                character.setAttackDamage(2*character.getAttackDamage());
                break;
            }
        }

        towers.stream().filter(t -> t.isWithinSupportRadius(character.getX(), character.getY()))
                       .forEach(battle::addBattleEntity);
        enemies.stream()
                .filter(e -> e.isWithinBattleRadius(character.getX(), character.getY())
                        || e.isWithinSupportRadius(character.getX(), character.getY()))
                .forEach(battle::addBattleEntity);
        alliedSoldiers.forEach(battle::addBattleEntity);

        battle.runBattle();

        // reset character's attack damage if battled with campfire
        if (withCampfire) {
            character.setAttackDamage(character.getAttackDamage()/2);
        }

        // Get the entities that need to be killer
        List<Battleable> defeatedEntities = battle.getDefeatedEntities();

        // Kill the allied soldiers
         List<AlliedSoldier> defeatedAllied = defeatedEntities.stream()
                 .filter(e -> !(e instanceof Enemy))
                 .map(AlliedSoldier.class::cast)
                 .collect(Collectors.toList());

         for (AlliedSoldier a: defeatedAllied) {
             killAlliedSoldier(a);
         }

        // Kill the enemies
        defeatedEntities.stream().filter(e -> e instanceof Enemy).forEach(e -> defeatedEnemies.add((Enemy) e));

        for (Enemy e: defeatedEnemies){
            // IMPORTANT = we kill enemies here, because killEnemy removes the enemy from the enemies list
            // if we killEnemy in prior loop, we get java.util.ConcurrentModificationException
            // due to mutating list we're iterating over
            killEnemy(e);
        }

        return defeatedEnemies;
    }

    /**
     * spawn a card in the world and return the card entity
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public VampireCastleCard loadVampireCard(){
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            // TODO = give some cash/experience/item rewards for the discarding of the oldest card
            oldCardtoGold();
            oldCardtoXP();
            removeCard(0);

        }
        VampireCastleCard vampireCastleCard = new VampireCastleCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(vampireCastleCard);
        return vampireCastleCard;
    }

    public ZombiePitCard loadZombiePitCard(){
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            // TODO = give some cash/experience/item rewards for the discarding of the oldest card
            oldCardtoGold();
            oldCardtoXP();
            removeCard(0);
        }
        ZombiePitCard zombiePitCard = new ZombiePitCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(zombiePitCard);
        return zombiePitCard;
    }

    public TrapCard loadTrapCard(){
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            // TODO = give some cash/experience/item rewards for the discarding of the oldest card
            oldCardtoGold();
            oldCardtoXP();
            removeCard(0);
        }
        TrapCard trapCard = new TrapCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(trapCard);
        return trapCard;
    }

    public VillageCard loadVillageCard(){
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            // TODO = give some cash/experience/item rewards for the discarding of the oldest card
            oldCardtoGold();
            oldCardtoXP();
            removeCard(0);
        }
        VillageCard villageCard = new VillageCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(villageCard);
        return villageCard;
    }

    /**
     * spawn a tower card in the world and return the card entity
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public TowerCard loadTowerCard(){
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            // TODO = give some cash/experience/item rewards for the discarding of the oldest card
            oldCardtoGold();
            removeCard(0);
        }
        TowerCard towerCard = new TowerCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(towerCard);
        return towerCard;
    }

    /**
     * spawn a barrack card in the world and return the card entity
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public BarrackCard loadBarrackCard(){
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            // TODO = give some cash/experience/item rewards for the discarding of the oldest card
            oldCardtoGold();
            removeCard(0);
        }
        BarrackCard barrackCard = new BarrackCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(barrackCard);
        return barrackCard;
    }

    /**
     * spawn a campfire card in the world and return the card entity
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public CampfireCard loadCampfireCard(){
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            oldCardtoGold();
            removeCard(0);
        }
        CampfireCard campfireCard = new CampfireCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(campfireCard);
        return campfireCard;
    }



    /**
     * remove card at a particular index of cards (position in gridpane of unplayed cards)
     * @param index the index of the card, from 0 to length-1
     */
    private void removeCard(int index){
        Card c = cardEntities.get(index);
        int x = c.getX();
        c.destroy();
        cardEntities.remove(index);
        shiftCardsDownFromXCoordinate(x);
    }


    /**
     * spawn a sword in the world and return the sword entity
     * @return a sword to be spawned in the controller as a JavaFX node
     */
     public void convertDroppedUnequippedInventoryItemstoGold(Entity item) {
        if (item instanceof Weapon) {
            Weapon w = (Weapon)item;
            numGold.set(numGold.get() + (int)(0.4*(w.getPrice())));
        } else if (item instanceof Shield) {
            Shield s = (Shield)item;
            numGold.set(numGold.get() + (int)(0.4*(s.getPrice())));
        } else if (item instanceof Helmet) {
            Helmet h = (Helmet)item;
            numGold.set(numGold.get() + (int)(0.4*(h.getPrice())));
        } else if (item instanceof Armour) {
            Armour a = (Armour)item;
            numGold.set(numGold.get() + (int)(0.4*(a.getPrice())));
        }
     }
    
     public Sword addUnequippedSword(){
        // TODO = expand this - we would like to be able to add multiple types of items, apart from swords
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            // TODO = give some cash/experience rewards for the discarding of the oldest sword
            Entity firstitem = unequippedInventoryItems.get(0);
            convertDroppedUnequippedInventoryItemstoGold(firstitem);
            oldEquipmenttoXP();
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        
        // now we insert the new sword, as we know we have at least made a slot available...
        Sword sword = new Sword(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(sword);
        return sword;
    }

     /**
     * spawn a staff in the world and return the staff entity
     * @return a staff to be spawned in the controller as a JavaFX node
     */
    public Staff addUnequippedStaff(){
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            // TODO = give some cash/experience rewards for the discarding of the oldest staff
            Entity firstitem = unequippedInventoryItems.get(0);
            convertDroppedUnequippedInventoryItemstoGold(firstitem);
            oldEquipmenttoXP();
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        
        // insert the new staff
        Staff staff = new Staff(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(staff);
        return staff;
    }

     /**
     * spawn a stake in the world and return the stake entity
     * @return a stake to be spawned in the controller as a JavaFX node
     */
    public Stake addUnequippedStake(){
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            // TODO = give some cash/experience rewards for the discarding of the oldest stake
            Entity firstitem = unequippedInventoryItems.get(0);
            convertDroppedUnequippedInventoryItemstoGold(firstitem);
            oldEquipmenttoXP();
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        
        // insert the new stake
        Stake stake = new Stake(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(stake);
        return stake;
    }

    /**
    * spawn an armour in the world and return the armour entity
    * @return an armour to be spawned in the controller as a JavaFX node
    */
    public Armour addUnequippedArmour(){
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            // TODO = give some cash/experience rewards for the discarding of the oldest armour
            Entity firstitem = unequippedInventoryItems.get(0);
            convertDroppedUnequippedInventoryItemstoGold(firstitem);
            oldEquipmenttoXP();
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        
        // insert the new armour
        Armour armour = new BasicArmour(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(armour);
        return armour;
    }

    /**
    * spawn a helmet in the world and return the helmet entity
    * @return an helmet to be spawned in the controller as a JavaFX node
    */
    public Helmet addUnequippedHelmet(){
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            // TODO = give some cash/experience rewards for the discarding of the oldest helmet
            Entity firstitem = unequippedInventoryItems.get(0);
            convertDroppedUnequippedInventoryItemstoGold(firstitem);
            oldEquipmenttoXP();
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        
        // insert the new helmet
        Helmet helmet = new BasicHelmet(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(helmet);
        return helmet;
    }

    /**
    * spawn a shield in the world and return the shield entity
    * @return an shield to be spawned in the controller as a JavaFX node
    */
    public Shield addUnequippedShield(){
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            // TODO = give some cash/experience rewards for the discarding of the oldest shield
            Entity firstitem = unequippedInventoryItems.get(0);
            convertDroppedUnequippedInventoryItemstoGold(firstitem);
            oldEquipmenttoXP();
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        
        // insert the new shield
        Shield shield = new BasicShield(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(shield);
        return shield;
    }

    /**
     * remove an item by x,y coordinates
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y){
        Entity item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryItem(item);
    }

    /**
     * run moves which occur with every tick without needing to spawn anything immediately.
     * @return The list of entities that need to be spawned on the front end
     */
    public List<Entity> runTickMoves(){
        character.moveDownPath();
        moveBasicEnemies();

        int characterIndexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
        if (characterIndexPosition != 0) {
            return new ArrayList<>();
        }

        // Update the cycle count if the character get back to the start
        cycleCount.set(cycleCount.get() + 1);

        // Only spawn entities at the start of each cycle
        List<Entity> entitiesToLoad = entityFactory.getEntitiesToLoad(cycleCount.get(), orderedPath);

        // Add them to the backend world
        for (Entity entity: entitiesToLoad) {
            if (entity instanceof Enemy) {
                Enemy newEnemy = (Enemy) entity;  
                
                // Let traps subscribe the enemies
                traps.forEach(newEnemy::subscribe);
                enemies.add(newEnemy);
            }
        }

        return entitiesToLoad;
    }

    /**
     * remove an item from the unequipped inventory
     * @param item item to be removed
     */
    private void removeUnequippedInventoryItem(Entity item){
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates
     * assumes that no 2 unequipped inventory items share x and y coordinates
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    private Entity getUnequippedInventoryItemEntityByCoordinates(int x, int y){
        for (Entity e: unequippedInventoryItems){
            if ((e.getX() == x) && (e.getY() == y)){
                return e;
            }
        }
        return null;
    }

    /**
     * remove item at a particular index in the unequipped inventory items list (this is ordered based on age in the starter code)
     * @param index index from 0 to length-1
     */
    private void removeItemByPositionInUnequippedInventoryItems(int index){
        Entity item = unequippedInventoryItems.get(index);
        item.destroy();
        unequippedInventoryItems.remove(index);
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the unequipped inventory
     * @return x,y coordinate pair
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForItem(){
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available slot defined by looking row by row
        for (int y=0; y<unequippedInventoryHeight; y++){
            for (int x=0; x<unequippedInventoryWidth; x++){
                if (getUnequippedInventoryItemEntityByCoordinates(x, y) == null){
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * shift card coordinates down starting from x coordinate
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x){
        for (Card c: cardEntities){
            if (c.getX() >= x){
                c.x().set(c.getX()-1);
            }
        }
    }

    /**
     * move all enemies
     */
    private void moveBasicEnemies() {
        enemies.forEach(e -> e.move());
    }

    /**
     * Get a randomly generated position that can be used to spawn an enemy
     * @return a random coordinate pair
     */
    public Pair<Integer, Integer> getPositionToSpawnEnemy() {
        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();

        // Exclude the position of the character and its surrounding
        int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));

        int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();
        int endNotAllowed = (indexPosition + 3)%orderedPath.size();

        for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
            orderedPathSpawnCandidates.add(orderedPath.get(i));
        }

        // Do not spawn on hero's castle
        orderedPathSpawnCandidates.remove(orderedPath.get(0));

        // choose a random position
        return orderedPathSpawnCandidates.get(new Random().nextInt(orderedPathSpawnCandidates.size()));
    }

    /**
     * get a randomly generated position which could be used to spawn an enemy
     * @return null if random choice is that wont be spawning an enemy or it isn't possible, or random coordinate pair if should go ahead
     */
    private Pair<Integer, Integer> possiblyGetEnemySpawnPosition(){
        // TODO = modify this
        
        // has a chance spawning a basic enemy on a tile the character isn't on or immediately before or after (currently space required = 2)...
        Random rand = new Random();
        int choice = rand.nextInt(2); // TODO = change based on spec... currently low value for dev purposes...
        // TODO = change based on spec
        if ((choice == 0) && (enemies.size() < 2)){
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();
            int endNotAllowed = (indexPosition + 3)%orderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }

            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));

            return spawnPosition;
        }
        return null;
    }

    /**
     * remove a card by its x, y coordinates. Return null if the card the drop location doesn't match
     * @param cardNodeX x index from 0 to width-1 of card to be removed
     * @param cardNodeY y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        // Buildings cannot be placed on top of each other
        if (hasNoExistingBuilding(buildingNodeX, buildingNodeY)) return null;

        // start by getting card
        Card card = getCardByCoordinates(cardNodeX, cardNodeY);
        assert card != null;
        
        if (!card.isDroppedLocationValid(this, buildingNodeX, buildingNodeY)) {
            return null;
        }

        Building newBuilding = card.getBuilding(this, buildingNodeX, buildingNodeY);
        buildingEntities.add(newBuilding);

        // destroy the card
        card.destroy();
        cardEntities.remove(card);
        shiftCardsDownFromXCoordinate(cardNodeX);

        return newBuilding;
    }

    /**
     * Get the card being dragged by coordination
     * @param cardNodeX
     * @param cardNodeY
     * @return The card being dragged, null if no
     */
    public Card getCardByCoordinates(int cardNodeX, int cardNodeY) {
        Card card = null;
        for (Card c: cardEntities){
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)){
                card = c;
                break;
            }
        }

        return card;
    }

    /**
     * Given the source and target location, check if card is dropped at the correct location.
     * @return whether it is valid to drop source at target
     */
    public boolean isCardDroppedLocationValid(int sourceX, int sourceY, int targetX, int targetY) {
        Card card = getCardByCoordinates(sourceX, sourceY);
        if (card != null) {
            return card.isDroppedLocationValid(this, targetX, targetY); 
        }
        return false;
    }

    /**
     * Check if the given x, y pair is on a path tile
     * @param nodeX x index
     * @param nodeY y index
     * @return true if entity is on path, false if not
     */
    public boolean isOnPath(int nodeX, int nodeY) {
        return orderedPath.contains(Pair.with(nodeX, nodeY));
    }

    public boolean isAdjacentToPath(int nodeX, int nodeY) {
        return getAdjacentPath(nodeX, nodeY) != null && (!isOnPath(nodeX, nodeY));
    }

    public boolean hasNoExistingBuilding(int nodeX, int nodeY) {
        for (Building b: buildingEntities) {
            if (b.getX() == nodeX && b.getY() == nodeY) return true;
        }
        return false;
    }

    /**
     * Check if an entity is next to a path tile
     * @param x x index
     * @param y y index
     * @return list of coordinates of the closest path positions if entity is next to path, empty list if not
     */
    public Pair<Integer, Integer> getAdjacentPath(int x, int y) {
        // get relevant up, down, left, right positions
        Pair<Integer, Integer> upPos = y - 1 >= 0 ? Pair.with(x, y - 1) : null;
        Pair<Integer, Integer> rightPos = x + 1 < width ? Pair.with(x + 1, y): null;
        Pair<Integer, Integer> downPos = y + 1 < height ? Pair.with(x, y + 1) : null;
        Pair<Integer, Integer> leftPos = x - 1 >= 0 ? Pair.with(x - 1, y) : null;

        // check if any of the directions is on a path tile
        if (orderedPath.contains(upPos)) {
            return upPos;
        } else if (orderedPath.contains(rightPos)) {
            return rightPos;
        } else if (orderedPath.contains(downPos)) {
            return downPos;
        } else if (orderedPath.contains(leftPos)) {
            return rightPos;
        }

        return null;
    }

    /**
     * Get an item from the unequipped inventory of the desired Class by its x, y coordinates.
     * Return null if no such item or not of the desired Class
     * @param itemNodeX x index from 0 to unequipped item
     * @param itemNodeY y index from 0 to unequipped item
     * @param desiredClass The desired class of the item
     * @return The corresponding item
     */
    public StaticEntity getItemByCoordinates(int itemNodeX, int itemNodeY, Class<?> desiredClass) {
        StaticEntity item = null;
        for (StaticEntity s: unequippedInventoryItems) {
            if ((s.getX() == itemNodeX) && (s.getY() == itemNodeY)){
                item = s;
                break;
            }
        }

        return desiredClass.isInstance(item) ? item : null;
    }

    /**
     * Let the Character equip this item.
     * @param itemToEquipped The item to be equipped
     */
    public void equipItem(StaticEntity itemToEquipped) {
        if (itemToEquipped instanceof Weapon) {
            character.setItem((Weapon) itemToEquipped);
        } else if (itemToEquipped instanceof Shield) {
            character.setItem((Shield) itemToEquipped);
        } else if (itemToEquipped instanceof Helmet) {
            character.setItem((Helmet) itemToEquipped);
        } else if (itemToEquipped instanceof Armour) {
            character.setItem((Armour) itemToEquipped);
        }
    }
}


