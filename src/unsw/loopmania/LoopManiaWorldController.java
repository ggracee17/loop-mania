package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import unsw.loopmania.BattleEntity.Doggie;
import unsw.loopmania.BattleEntity.ElanMuske;
import unsw.loopmania.BattleEntity.Enemy;
import unsw.loopmania.BattleEntity.Slug;
import unsw.loopmania.BattleEntity.Vampire;
import unsw.loopmania.BattleEntity.Zombie;
import unsw.loopmania.building.BarrackBuilding;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.building.TrapBuilding;
import unsw.loopmania.building.VampireCastleBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.building.ZombiePitBuilding;
import unsw.loopmania.card.BarrackCard;
import unsw.loopmania.card.CampfireCard;
import unsw.loopmania.card.Card;
import unsw.loopmania.card.TowerCard;
import unsw.loopmania.card.TrapCard;
import unsw.loopmania.card.VampireCastleCard;
import unsw.loopmania.card.VillageCard;
import unsw.loopmania.card.ZombiePitCard;
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

import java.util.EnumMap;

import java.io.File;
import java.io.IOException;


/**
 * the draggable types.
 * If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE{
    CARD,
    ITEM
}

/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application thread:
 *     https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 *     Note in https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html under heading "Threading", it specifies animation timelines are run in the application thread.
 * This means that the starter code does not need locks (mutexes) for resources shared between the timeline KeyFrame, and all of the  event handlers (including between different event handlers).
 * This will make the game easier for you to implement. However, if you add time-consuming processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend:
 *     using Task https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by itself or within a Service https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 *     Tasks ensure that any changes to public properties, change notifications for errors or cancellation, event handlers, and states occur on the JavaFX Application thread,
 *         so is a better alternative to using a basic Java Thread: https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm
 *     The Service class is used for executing/reusing tasks. You can run tasks without Service, however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need to implement locks on resources shared with the application thread (i.e. Timeline KeyFrame and drag Event handlers).
 * You can check whether code is running on the JavaFX application thread by running the helper method printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using Platform.runLater https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 *     This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    /**
     * squares gridpane includes path images, enemies, character, empty grass, buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot stretches over the entire game world,
     * so we can detect dragging of cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    /**
     * Labels for cycle count, health, gold, experience
     */
    @FXML
    private Label cycleCountLabel;

    @FXML
    private Label healthLabel;

    @FXML
    private Label numHealthPotionLabel;

    @FXML
    private Button PotionButton;

    @FXML
    private Label numGoldLabel;

    @FXML
    private Label numXPLabel;

    @FXML
    private Label numAlliedSoldierLable;

    // all image views including tiles, character, enemies, cards... even though cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    /**
    * Images of Entities in the game
    * */

    private Image vampireCastleCardImage;
    private Image zombiePitCardImage;
    private Image trapCardImage;
    private Image villageCardImage;
    private Image towerCardImage;
    private Image barrackCardImage;
    private Image campfireCardImage;

    private Image slugImage;
    private Image zombieImage;
    private Image vampireImage;
    private Image doggieImage;
    private Image elanMuskeImage;

    private Image swordImage;
    private Image stakeImage;
    private Image staffImage;
    private Image armourImage;
    private Image shieldImage;
    private Image helmetImage;

    private Image vampireCastleBuildingImage;
    private Image zombiePitBuildingImage;
    private Image trapBuildingImage;
    private Image villageBuildingImage;
    private Image herosCastleBuildingImage;
    private Image towerBuildingImage;
    private Image barrackBuildingImage;
    private Image campfireBuildingImage;

    /**
     * the image currently being dragged, if there is one, otherwise null.
     * Holding the ImageView being dragged allows us to spawn it again in the drop location if appropriate.
     */
    // TODO = it would be a good idea for you to instead replace this with the building/item which should be dropped
    private ImageView currentlyDraggedImage;
    
    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged into the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged outside of the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    /**
     * @param world world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        entityImages = new ArrayList<>(initialEntities);

        /* Load GUI images */
        vampireCastleCardImage = new Image((new File("src/images/vampire_castle_card.png")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit_card.png")).toURI().toString());
        trapCardImage = new Image((new File("src/images/trap_card.png")).toURI().toString());
        villageCardImage = new Image((new File("src/images/village_card.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower_card.png")).toURI().toString());
        barrackCardImage = new Image((new File("src/images/barracks_card.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire_card.png")).toURI().toString());

        slugImage = new Image((new File("src/images/slug.png")).toURI().toString());
        zombieImage = new Image((new File("src/images/zombie.png")).toURI().toString());
        vampireImage = new Image((new File("src/images/vampire.png")).toURI().toString());
        doggieImage = new Image((new File("src/images/doggie.png")).toURI().toString());
        elanMuskeImage = new Image((new File("src/images/elan_muske.png")).toURI().toString());

        swordImage = new Image((new File("src/images/basic_sword.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        armourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());

        zombiePitBuildingImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        vampireCastleBuildingImage = new Image((new File("src/images/vampire_castle_building_purple_background.png")).toURI().toString());
        trapBuildingImage = new Image((new File("src/images/trap.png")).toURI().toString());
        villageBuildingImage = new Image((new File("src/images/village.png")).toURI().toString());
        herosCastleBuildingImage = new Image((new File("src/images/heros_castle.png")).toURI().toString());
        towerBuildingImage = new Image((new File("src/images/tower.png")).toURI().toString());
        barrackBuildingImage = new Image((new File("src/images/barracks.png")).toURI().toString());
        campfireBuildingImage = new Image((new File("src/images/campfire.png")).toURI().toString());

        currentlyDraggedImage = null;
        currentlyDraggedType = null;

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
    }

    @FXML
    public void initialize() {        
        Image pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (inculding all the twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages){
            squares.getChildren().add(entity);
        }
        
        // add the ground underneath the cards
        for (int x=0; x<world.getWidth(); x++){
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x=0; x<LoopManiaWorld.unequippedInventoryWidth; x++){
            for (int y=0; y<LoopManiaWorld.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        // Add the heros castle building on starting point
        ImageView herosCastleView = new ImageView(herosCastleBuildingImage);
        herosCastleView.setViewport(imagePart);
        squares.add(herosCastleView, 0, 0);

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);

        // Bind GUI states to back-end variables
        cycleCountLabel.setAlignment(Pos.CENTER);
        cycleCountLabel.textProperty().bind(world.cycleCountProperty().asString());

        healthLabel.setAlignment(Pos.CENTER);
        healthLabel.textProperty().bind(world.getCharacter().healthProperty().asString());

        numHealthPotionLabel.setAlignment(Pos.CENTER);
        numHealthPotionLabel.textProperty().bind(world.numHealthPotionProperty().asString());

        numGoldLabel.setAlignment(Pos.CENTER);
        numGoldLabel.textProperty().bind(world.numGoldProperty().asString());

        numXPLabel.setAlignment(Pos.CENTER);
        numXPLabel.textProperty().bind(world.numXPProperty().asString());

        numAlliedSoldierLable.setAlignment(Pos.CENTER);
        numAlliedSoldierLable.textProperty().bind(world.numAlliedSoldiersProperty());
    }

    private void onLoadEntities(List<Entity> entitiesToLoad) {
        for (Entity entity : entitiesToLoad) {
            if (entity instanceof Slug) {
                onLoad((Slug) entity);
            } else if (entity instanceof Zombie) {
                onLoad((Zombie) entity);
            } else if (entity instanceof Vampire) {
                onLoad((Vampire) entity);
            } else if (entity instanceof Doggie) {
                onLoad((Doggie) entity);
            } else if (entity instanceof ElanMuske) {
                onLoad((ElanMuske) entity);
            }
        }
    }

    /**
     * create and run the timer
     */
    public void startTimer(){
        System.out.println("starting timer");
        isPaused = false;
        // trigger adding code to process main game logic to queue. JavaFX will target framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            List<Entity> entitiesToLoad = world.runTickMoves();
            onLoadEntities(entitiesToLoad);

            List<Enemy> trappedEnemies = world.getTrappedEnemies();
            for (Enemy e: trappedEnemies){
                reactToEnemyDefeat(e);
            }
            world.clearTrappedEnemies();

            List<Enemy> defeatedEnemies = world.runBattles();
            for (Enemy e: defeatedEnemies){
                reactToEnemyDefeat(e);
            }
        
            printThreadingNotes("HANDLED TIMER");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * pause the execution of the game loop
     * the human player can still drag and drop items during the game pause
     */
    public void pause(){
        isPaused = true;
        System.out.println("pausing");
        timeline.stop();
    }

    public void terminate(){
        pause();
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * @param entity backend entity to be paired with view
     * @param view frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load a vampire card from the world, and pair it with an image in the GUI
     */
    private void loadVampireCastleCard() {
        VampireCastleCard vampireCastleCard = world.loadVampireCard();
        onLoad(vampireCastleCard);
    }

    /**
     * load a zombie card from the world, and pair it with an image in the GUI
     */
    private void loadZombiePitCard() {
        ZombiePitCard zombiePitCard = world.loadZombiePitCard();
        onLoad(zombiePitCard);
    }

     /**
     * load a trap card from the world, and pair it with an image in the GUI
     */
    private void loadTrapCard() {
        TrapCard trapCard = world.loadTrapCard();
        onLoad(trapCard);
    }

     /**
     * load a village card from the world, and pair it with an image in the GUI
     */
    private void loadVillageCard() {
        VillageCard villageCard = world.loadVillageCard();
        onLoad(villageCard);
    }

    /**
     * load a tower card from the world, and pair it with an image in the GUI
     */
    private void loadTowerCard() {
        TowerCard towerCard = world.loadTowerCard();
        onLoad(towerCard);
    }

    /**
     * load a barracks card from the world, and pair it with an image in the GUI
     */
    private void loadBarrackCard() {
        BarrackCard barrackCard = world.loadBarrackCard();
        onLoad(barrackCard);
    }

    /**
     * load a campfire card from the world, and pair it with an image in the GUI
     */
    private void loadCampfireCard() {
        CampfireCard campfireCard = world.loadCampfireCard();
        onLoad(campfireCard);
    }


    /**
     * load a sword from the world, and pair it with an image in the GUI
     */
    private void loadSword(){
        // start by getting first available coordinates
        Sword sword = world.addUnequippedSword();
        onLoad(sword);
    }

    /**
     * load a staff from the world, and pair it with an image in the GUI
     */
    private void loadStaff(){
        // start by getting first available coordinates
        Staff staff = world.addUnequippedStaff();
        onLoad(staff);
    }

    /**
     * load a stake from the world, and pair it with an image in the GUI
     */
    private void loadStake(){
        // start by getting first available coordinates
        Stake stake = world.addUnequippedStake();
        onLoad(stake);
    }

    /**
     * load an armour from the world, and pair it with an image in the GUI
     */
    private void loadArmour(){
        // start by getting first available coordinates
        Armour armour = world.addUnequippedArmour();
        onLoad(armour);
    }

    /**
     * load an helmet from the world, and pair it with an image in the GUI
     */
    private void loadHelmet(){
        // start by getting first available coordinates
        Helmet helmet = world.addUnequippedHelmet();
        onLoad(helmet);
    }

    /**
     * load an shield from the world, and pair it with an image in the GUI
     */
    private void loadShield(){
        // start by getting first available coordinates
        Shield shield = world.addUnequippedShield();
        onLoad(shield);
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning items/experience/gold
     * @param enemy defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(Enemy enemy){
        // react to character defeating an enemy
        // in starter code, spawning extra card/weapon...
        // TODO = provide different benefits to defeating the enemy based on the type of enemy
        world.gainXP();
        world.gainGold(enemy);
        Random random = new Random();
        if (enemy instanceof Slug) {         
            if (random.nextDouble() > 0.5) {
                loadSword();
            }
            if (random.nextDouble() > 0.5) {
                loadArmour();
            }
            if (random.nextDouble() > 0.5) {
                loadZombiePitCard(); 
            }
            if (random.nextDouble() > 0.5) {
                loadVampireCastleCard();
            }
            if (random.nextDouble() > 0.5) {
                loadBarrackCard();
            }
            if (random.nextDouble() > 0.6) {
                loadCampfireCard();
            }
            if (random.nextDouble() > 0.7) {
                loadHelmet();
            }
            
        } else if (enemy instanceof Zombie) {
            if (random.nextDouble() > 0.5) {
                loadSword();
            }
            if (random.nextDouble() > 0.5) {
                loadArmour();
            }
            if (random.nextDouble() > 0.5) {
                loadShield();
            }
            if (random.nextDouble() > 0.5) {
                loadZombiePitCard(); 
            }
            if (random.nextDouble() > 0.5) {
                loadStake();
            }
            if (random.nextDouble() > 0.4) {
                loadVampireCastleCard();
            }
            if (random.nextDouble() > 0.3) {
                loadBarrackCard();
            }
            if (random.nextDouble() > 0.4) {
                loadHelmet();
            }
            if (random.nextDouble() > 0.4) {
                loadTowerCard();
            }
            if (random.nextDouble() > 0.3) {
                world.loadHealthPotion();
            }
        } else if (enemy instanceof Vampire) {
            if (random.nextDouble() > 0.4) {
                loadStaff();
            }
            if (random.nextDouble() > 0.4) {
                loadTrapCard();
            }
            if (random.nextDouble() > 0.4) {
                loadVillageCard(); 
            }
            if (random.nextDouble() > 0.4) {
                loadVampireCastleCard();
            }
            if (random.nextDouble() > 0.4) {
                loadBarrackCard();
            }
            if (random.nextDouble() > 0.4) {
                loadHelmet();
            }
            if (random.nextDouble() > 0.3) {
                loadTowerCard();
            }
            if (random.nextDouble() > 0.1) {
                world.loadHealthPotion();
            }
        } else if (enemy instanceof Doggie) {
            if (random.nextDouble() > 0.2) {
                loadArmour();
            }
            if (random.nextDouble() > 0.2) {
                loadBarrackCard();
            }
            if (random.nextDouble() > 0.2) {
                loadCampfireCard();
            }
            if (random.nextDouble() > 0.2) {
                loadHelmet();
            }
            if (random.nextDouble() > 0.2) {
                loadShield();
            }
            if (random.nextDouble() > 0.2) {
                loadStaff();
            }
            if (random.nextDouble() > 0.2) {
                loadStake();
            }
            if (random.nextDouble() > 0.2) {
                loadSword();
            }
            if (random.nextDouble() > 0.2) {
                loadTowerCard();
            }
            if (random.nextDouble() > 0.2) {
                loadTrapCard();
            }
            if (random.nextDouble() > 0.2) {
                loadVampireCastleCard();
            }
            if (random.nextDouble() > 0.2) {
                loadVillageCard();
            }
            if (random.nextDouble() > 0.2) {
                loadZombiePitCard();
            }
            if (random.nextDouble() > 0.2) {
                world.loadHealthPotion();
            }

        } else if (enemy instanceof ElanMuske) {
            if (random.nextDouble() > 0.1) {
                loadArmour();
            }
            if (random.nextDouble() > 0.1) {
                loadBarrackCard();
            }
            if (random.nextDouble() > 0.1) {
                loadCampfireCard();
            }
            if (random.nextDouble() > 0.1) {
                loadHelmet();
            }
            if (random.nextDouble() > 0.1) {
                loadShield();
            }
            if (random.nextDouble() > 0.1) {
                loadStaff();
            }
            if (random.nextDouble() > 0.1) {
                loadStake();
            }
            if (random.nextDouble() > 0.1) {
                loadSword();
            }
            if (random.nextDouble() > 0.1) {
                loadTowerCard();
            }
            if (random.nextDouble() > 0.1) {
                loadTrapCard();
            }
            if (random.nextDouble() > 0.1) {
                loadVampireCastleCard();
            }
            if (random.nextDouble() > 0.1) {
                loadVillageCard();
            }
            if (random.nextDouble() > 0.1) {
                loadZombiePitCard();
            }
            if (random.nextDouble() > 0.1) {
                world.loadHealthPotion();
            }

        }  
    }

    /**
     * load a vampire castle card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param buildingCard
     */
    private void onLoad(Card buildingCard) {
        ImageView view = null;
        if (buildingCard instanceof VampireCastleCard) {
            view = new ImageView(vampireCastleCardImage);
        } else if (buildingCard instanceof ZombiePitCard) {
            view = new ImageView(zombiePitCardImage);
        } else if (buildingCard instanceof TrapCard) {
            view = new ImageView(trapCardImage);
        } else if (buildingCard instanceof VillageCard) {
            view = new ImageView(villageCardImage);
        } else if (buildingCard instanceof TowerCard) {
            view = new ImageView(towerCardImage);
        } else if (buildingCard instanceof BarrackCard) {
            view = new ImageView(barrackCardImage);
        } else if (buildingCard instanceof CampfireCard) {
            view = new ImageView(campfireCardImage);
        }
        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);
        addEntity(buildingCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a sword into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * @param sword
     */
    private void onLoad(Sword sword) {
        ImageView view = new ImageView(swordImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(sword, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a staff into the GUI.
     * @param staff
     */
    private void onLoad(Staff staff) {
        ImageView view = new ImageView(staffImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(staff, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load a stake into the GUI.
     * @param stake
     */
    private void onLoad(Stake stake) {
        ImageView view = new ImageView(stakeImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(stake, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load an armour into the GUI.
     * @param armour
     */
    private void onLoad(Armour armour) {
        ImageView view = new ImageView(armourImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(armour, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
    * load an helmet into the GUI.
    * @param helmet
    */
    private void onLoad(Helmet helmet) {
        ImageView view = new ImageView(helmetImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(helmet, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
    * load an shield into the GUI.
    * @param shield
    */
    private void onLoad(Shield shield) {
        ImageView view = new ImageView(shieldImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(shield, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load an slug into the GUI
     * @param slug  The slug to be loaded
     */
    private void onLoad(Slug slug) {
        ImageView view = new ImageView(slugImage);
        onLoadHelper(slug, view);
    }

    /**
     * load a zombie into the GUI
     * @param zombie  The zombie to be loaded
     */
    private void onLoad(Zombie zombie) {
        ImageView view = new ImageView(zombieImage);
        onLoadHelper(zombie, view);
    }

    /**
     * load a vampire into the GUI
     * @param vampire  The vampire to be loaded
     */
    private void onLoad(Vampire vampire) {
        ImageView view = new ImageView(vampireImage);
        onLoadHelper(vampire, view);
    }

    /**
     * load a doggie into the GUI
     * @param doggie  The doggie to be loaded
     */
    private void onLoad(Doggie doggie) {
        ImageView view = new ImageView(doggieImage);
        onLoadHelper(doggie, view);
    }

    /**
     * load an Elan Muske into the GUI
     * @param elanMuske  The elanMuske to be loaded
     */
    private void onLoad(ElanMuske elanMuske) {
        ImageView view = new ImageView(elanMuskeImage);
        onLoadHelper(elanMuske, view);
    }

    /**
     * Help loading an enemy into the GUI
     */
    private void onLoadHelper(Entity entity, ImageView view) {
        addEntity(entity, view);
        squares.getChildren().add(view);
    }

    /**
     * load a vampire castle into the GUI
     * @param vampireCastle
     */
    private void onLoad(VampireCastleBuilding vampireCastle){
        ImageView view = new ImageView(vampireCastleBuildingImage);
        addEntity(vampireCastle, view);
        squares.getChildren().add(view);
    }

    /**
     * load a zombie pit into the GUI
     * @param zombiePit
     */
    private void onLoad(ZombiePitBuilding zombiePit) {
        ImageView view = new ImageView(zombiePitBuildingImage);
        addEntity(zombiePit, view);
        squares.getChildren().add(view);
    }

    /**
     * load a trap into the GUI
     * @param trap
     */
    private void onLoad(TrapBuilding trapBuilding) {
        ImageView view = new ImageView(trapBuildingImage);
        addEntity(trapBuilding, view);
        squares.getChildren().add(view);
    }

    /**
     * load a village into the GUI
     * @param village
     */
    private void onLoad(VillageBuilding villageBuilding) {
        ImageView view = new ImageView(villageBuildingImage);
        addEntity(villageBuilding, view);
        squares.getChildren().add(view);
    }

    /**
     * load a tower into the GUI
     * @param tower
     */
    private void onLoad(TowerBuilding towerBuilding) {
        ImageView view = new ImageView(towerBuildingImage);
        addEntity(towerBuilding, view);
        squares.getChildren().add(view);
    }

    /**
     * load a barrack into the GUI
     * @param barrack
     */
    private void onLoad(BarrackBuilding barrackBuilding) {
        ImageView view = new ImageView(barrackBuildingImage);
        addEntity(barrackBuilding, view);
        squares.getChildren().add(view);
    }

    /**
     * load a campfire into the GUI
     * @param campfire
     */
    private void onLoad(CampfireBuilding campfireBuilding) {
        ImageView view = new ImageView(campfireBuildingImage);
        addEntity(campfireBuilding, view);
        squares.getChildren().add(view);
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the background, dropping over the background.
     * These are not attached to invidual items such as swords/cards.
     * @param draggableType the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        // for example, in the specification, villages can only be dropped on path, whilst vampire castles cannot go on the path

        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /*
                 *you might want to design the application so dropping at an invalid location drops at the most recent valid location hovered over,
                 * or simply allow the card/item to return to its slot (the latter is easier, as you won't have to store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType){
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();     // The target node
                    if(node != targetGridPane && db.hasImage()){

                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);

                        boolean dropSuccess = false;
                        switch (draggableType){
                            case CARD:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);

                                Building newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                if (newBuilding == null) break;
                                
                                onLoadBuilding(newBuilding);
                                dropSuccess = true;
                                break;

                            case ITEM:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);

                                if (!(node instanceof ImageView)) {
                                    break;
                                }

                                Class<?> desiredClass = getDesiredItemClass((ImageView) node);
                                StaticEntity newEquipment = getItemByCoordinates(nodeX, nodeY, desiredClass);
                                // If there is not matching equipment
                                if (newEquipment == null) break;
                                
                                dropSuccess = true;
                                equipItem(newEquipment);                // Equip the item
                                removeItemByCoordinates(nodeX, nodeY);  // Remove from backend
                                targetGridPane.add(image, x, y, 1, 1); // Add to front end
                                break;
                            default:
                                break;
                        }

                        // If we didn't drop the item, set the original image to visible
                        if (!dropSuccess) {
                            currentlyDraggedImage.setVisible(true);
                        }

                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);


                        // remove drag event handlers before setting currently dragged image to null
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>(){
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    if(event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()){
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null){
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != anchorPaneRoot && db.hasImage()){
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);
                        
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * onload a building to frontend
     * @param building the building to be onloaded
     */
    private void onLoadBuilding(Building building) {
        if (building instanceof VampireCastleBuilding) {
            onLoad((VampireCastleBuilding) building);
        } else if (building instanceof ZombiePitBuilding) {
            onLoad((ZombiePitBuilding) building);
        } else if (building instanceof TrapBuilding) {
            onLoad((TrapBuilding) building);
        } else if (building instanceof VillageBuilding) {
            onLoad((VillageBuilding) building);
        } else if (building instanceof TowerBuilding) {
            onLoad((TowerBuilding) building);
        } else if (building instanceof BarrackBuilding) {
            onLoad((BarrackBuilding) building);
        } else if (building instanceof CampfireBuilding) {
            onLoad((CampfireBuilding) building);
        }
    }

    /**
     * equip an item
     * @param newEquipment the item to be equipped
     */
    private void equipItem(StaticEntity newEquipment) {
        world.equipItem(newEquipment);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where the card was dropped
     * @param cardNodeX the x coordinate of the card which was dragged, from 0 to width-1
     * @param cardNodeY the y coordinate of the card which was dragged (in starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card, where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card, where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        return world.convertCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    public boolean isCardDroppedLocationValid(int sourceX, int sourceY, int targetX, int targetY) {
        return world.isCardDroppedLocationValid(sourceX, sourceY, targetX, targetY);
    }

    /**
     * From the world, get an item from the unequipped inventory of the desired Class by its x, y
     * coordinates.
     * Return null if no such item or not of the desired Class
     * @param itemNodeX x index from 0 to unequipped item
     * @param itemNodeY y index from 0 to unequipped item
     * @param desiredClass The desired class of the item
     * @return The corresponding item
     */
    private StaticEntity getItemByCoordinates(int itemNodeX, int itemNodeY, Class<?> desiredClass) {
        return world.getItemByCoordinates(itemNodeX, itemNodeY, desiredClass);
    }

    /**
     * remove an item from the unequipped inventory by its x and y coordinates in the unequipped inventory gridpane
     * @param nodeX x coordinate from 0 to unequippedInventoryWidth-1
     * @param nodeY y coordinate from 0 to unequippedInventoryHeight-1
     */
    private void removeItemByCoordinates(int nodeX, int nodeY) {
        world.removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
    }

    Class<?> getDesiredItemClass(ImageView imageView) {
        String slot_id = imageView.getId();

        Class<?> desiredClass = null;
        switch (slot_id) {
            case ("swordCell"):
                desiredClass = Weapon.class;
                break;  
            case ("armourCell"):
                desiredClass = Armour.class;
                break;
            case ("shieldCell"):
                desiredClass = Shield.class;
                break;
            case ("helmetCell"):
                desiredClass = Helmet.class;
                break;
        }

        return desiredClass;
    }

    /**
     * add drag event handlers to an ImageView
     * @param view the view to attach drag event handlers to
     * @param draggableType the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be dragged
     * @param targetGridPane the relevant gridpane to which the entity would be dragged to
     */
    private void addDragEventHandlers(ImageView view, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can detect it...
                currentlyDraggedType = draggableType;
                //Drag was detected, start drap-and-drop gesture
                //Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);
    
                //Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);
                
                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

                int sourceX = GridPane.getColumnIndex(currentlyDraggedImage);
                int sourceY = GridPane.getRowIndex(currentlyDraggedImage);
                switch (draggableType){
                    case CARD:
                        draggedEntity.setImage(getBuildingImageByCardCoordination(sourceX, sourceY));
                        break;
                    case ITEM:
                        draggedEntity.setImage(getItemImageByCoordination(sourceX, sourceY));
                        break;
                    default:
                        break;
                }
                
                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));
                
                // n is the target node
                for (Node n: targetGridPane.getChildren()){
                    // events for entering and exiting are attached to squares children because that impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                            //The drag-and-drop gesture entered the target
                            //show the user that it is an actual gesture target
                                if(event.getGestureSource() != n && event.getDragboard().hasImage()){
                                    // Get the source and target coordination
                                    Integer cIndex = GridPane.getColumnIndex(n);
                                    Integer rIndex = GridPane.getRowIndex(n);
                                    int targetX = cIndex == null ? 0 : cIndex;
                                    int targetY = rIndex == null ? 0 : rIndex;

                                    Node source = (Node) event.getGestureSource();
                                    int sourceX = GridPane.getColumnIndex(source);
                                    int sourceY = GridPane.getRowIndex(source);
                                    
                                    switch (draggableType) {
                                        case CARD:
                                            if (isCardDroppedLocationValid(sourceX, sourceY, targetX, targetY)) {
                                                n.setOpacity(0.7); 
                                            }
                                            break;
                                        
                                        case ITEM:
                                            Class<?> desiredClass = getDesiredItemClass((ImageView) n);
                                            StaticEntity equipment = getItemByCoordinates(sourceX, sourceY, desiredClass);
                                            if (equipment != null) {
                                                n.setOpacity(0.7); 
                                            }
                                            break;
                                    }    
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = since being more selective about whether highlighting changes, you could program the game so if the new highlight location is invalid the highlighting doesn't change, or leave this as-is
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                                n.setOpacity(1);
                            }
                
                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }
            
        });
    }

    /**
     * Get the image of the corresponding item
     * @param sourceX
     * @param sourceY
     * @return the image of the corresponding item
     */
    private Image getItemImageByCoordination(int sourceX, int sourceY) {
        StaticEntity item = world.getItemByCoordinates(sourceX, sourceY, Object.class);
        if (item instanceof Sword) {
            return swordImage;
        } else if (item instanceof Staff) {
            return staffImage;
        } else if (item instanceof Stake) {
            return stakeImage;
        } else if (item instanceof BasicShield) {
            return shieldImage;
        } else if (item instanceof BasicHelmet) {
            return helmetImage;
        } else if (item instanceof BasicArmour) {
            return armourImage;
        }

        return swordImage;
    }

    /**
     * Get the image of the corresponding building card
     * @param sourceX
     * @param sourceY
     * @return the image of the corresponding building card
     */
    private Image getBuildingImageByCardCoordination(int sourceX, int sourceY) {
        Card card = world.getCardByCoordinates(sourceX, sourceY);
        if (card instanceof VampireCastleCard) {
            return vampireCastleCardImage;
        } else if (card instanceof ZombiePitCard) {
            return zombiePitCardImage;
        } else if (card instanceof TrapCard) {
            return trapCardImage;
        } else if (card instanceof VillageCard) {
            return villageCardImage;
        } else if (card instanceof TowerCard) {
            return towerCardImage;
        } else if (card instanceof BarrackCard) {
            return barrackCardImage;
        } else if (card instanceof CampfireCard) {
            return campfireCardImage;
        }
        return vampireCastleCardImage;
    }

    /**
     * remove drag event handlers so that we don't process redundant events
     * this is particularly important for slower machines such as over VLAB.
     * @param draggableType either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane){
        // remove event handlers from nodes in children squares, from anchorPaneRoot, and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n: targetGridPane.getChildren()){
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
        }
    }

    /**
     * handle the pressing of keyboard keys.
     * Specifically, we should pause when pressing SPACE
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        // TODO = handle additional key presses, e.g. for consuming a health potion
        switch (event.getCode()) {
        case SPACE:
            if (isPaused){
                startTimer();
            }
            else{
                pause();
            }
            break;
        default:
            break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        // TODO = possibly set other menu switchers
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        // TODO = possibly set other menu switchers
        pause();
        mainMenuSwitcher.switchMenu();
    }
    
    /**
     * this method is triggered when click button to consume a health potion
     * @param event
     */
    @FXML
    void takeHealthPotion(ActionEvent event) {
        world.takeHealthPotion();
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we need to track positions of spawned entities such as enemy
     * or items which might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So it is vital this is handled in this Controller class
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        // TODO = tweak this slightly to remove items from the equipped inventory?
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                                               .onAttach((o, l) -> o.addListener(xListener))
                                               .onDetach((o, l) -> {
                                                    o.removeListener(xListener);
                                                    entityImages.remove(node);
                                                    squares.getChildren().remove(node);
                                                    cards.getChildren().remove(node);
                                                    equippedItems.getChildren().remove(node);
                                                    unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                                               .onAttach((o, l) -> o.addListener(yListener))
                                               .onDetach((o, l) -> {
                                                   o.removeListener(yListener);
                                                   entityImages.remove(node);
                                                   squares.getChildren().remove(node);
                                                   cards.getChildren().remove(node);
                                                   equippedItems.getChildren().remove(node);
                                                   unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here, position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is running on the application thread.
     * By running everything on the application thread, you will not need to worry about implementing locks, which is outside the scope of the course.
     * Always writing code running on the application thread will make the project easier, as long as you are not running time-consuming tasks.
     * We recommend only running code on the application thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel){
        System.out.println("\n###########################################");
        System.out.println("current method = "+currentMethodLabel);
        System.out.println("In application thread? = "+Platform.isFxApplicationThread());
        System.out.println("Current system time = "+java.time.LocalDateTime.now().toString().replace('T', ' '));
    }
}
