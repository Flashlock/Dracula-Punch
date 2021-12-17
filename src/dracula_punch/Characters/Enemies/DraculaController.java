package dracula_punch.Characters.Enemies;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.GameObject;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class DraculaController extends EnemyController{
    public enum DraculaState { IDLE, ATTACK, RETREAT, VANISH }
    private DraculaState draculaState;
    public DraculaState getDraculaState(){ return draculaState; }

    private int phase2MaxHealth = 8;
    private int phase1MaxHealth = 10;

    private Image[] phase2HealthBars, phase1HealthBars;

    private int phaseCount = 3;

    public final ArrayList<DraculaBat> bats;

    private int meleeDamage = 5;
    private final int meleeActionFrame = 11;

    private int followThroughDist = 10;

    public DraculaController(Coordinate startingTile, LevelState curLevelState, SwarmManager swarmManager) {
        super(0, 0, startingTile, curLevelState, swarmManager);
        setScale(1.15f);
        bats = new ArrayList<>();
        attackAction = new AttackAction(this, meleeActionFrame, AttackType.MELEE);
        draculaState = DraculaState.IDLE;
        TOTAL_MOVE_TIME = 500;

        maxHealth = 6;
        currentHealth = maxHealth;
        healthBars = new Image[]{
                ResourceManager.getImage(DraculaPunchGame.DRACULA_1_HEALTH_6),
                ResourceManager.getImage(DraculaPunchGame.DRACULA_1_HEALTH_5),
                ResourceManager.getImage(DraculaPunchGame.DRACULA_1_HEALTH_4),
                ResourceManager.getImage(DraculaPunchGame.DRACULA_1_HEALTH_3),
                ResourceManager.getImage(DraculaPunchGame.DRACULA_1_HEALTH_2),
                ResourceManager.getImage(DraculaPunchGame.DRACULA_1_HEALTH_1),
        };

        phase2HealthBars = new Image[]{
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_2_HEALTH_8),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_2_HEALTH_7),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_2_HEALTH_6),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_2_HEALTH_5),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_2_HEALTH_4),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_2_HEALTH_3),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_2_HEALTH_2),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_2_HEALTH_1),
            };

        phase1HealthBars = new Image[]{
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_10),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_9),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_8),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_7),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_6),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_5),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_4),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_3),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_2),
                    ResourceManager.getImage(DraculaPunchGame.DRACULA_3_HEALTH_1),
            };

        setHealthBar();
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        super.update(gameContainer, stateBasedGame, delta);
        switch (draculaState){
            case IDLE:
                break;
            case ATTACK:
                attack(delta);
                break;
            case RETREAT:
                retreat();
                break;
            case VANISH:
                vanish();
                break;
            default:
                System.out.println("Unknown State: " + draculaState);
        }
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        if(draculaState == DraculaState.VANISH) return;
        super.render(gameContainer, stateBasedGame, graphics);
    }

    @Override
    public void takeDamage(int damage) {
        if(draculaState == DraculaState.VANISH) return;
        removeImage(healthBar);
        currentHealth -= damage;
        if(currentHealth <= 0){
            switch (phaseCount--){
                case 3:
                    // poof into bats
                    Coordinate[] tiles = new Coordinate[] {
                            new Coordinate(currentTile.x + 1, currentTile.y),
                            new Coordinate(currentTile.x - 1, currentTile.y),
                            new Coordinate(currentTile.x, currentTile.y + 1),
                            new Coordinate(currentTile.x, currentTile.y - 1)
                    };
                    healthBars = phase2HealthBars;
                    spawnBats(tiles);
                    disappear();
                    break;
                case 2:
                    // poof into more bats
                    tiles = new Coordinate[] {
                            new Coordinate(currentTile.x + 1, currentTile.y),
                            new Coordinate(currentTile.x - 1, currentTile.y),
                            new Coordinate(currentTile.x, currentTile.y + 1),
                            new Coordinate(currentTile.x, currentTile.y - 1),
                            new Coordinate(currentTile.x + 1, currentTile.y + 1),
                            new Coordinate(currentTile.x + 1, currentTile.y - 1),
                            new Coordinate(currentTile.x - 1, currentTile.y + 1),
                            new Coordinate(currentTile.x - 1, currentTile.y - 1)
                    };
                    healthBars = phase1HealthBars;
                    spawnBats(tiles);
                    disappear();
                    break;
                case 1:
                    // die
                    curLevelState.iWantToGoToBed();
                    break;
            }
        }
        if(currentHealth > 0)
            setHealthBar();
    }

    /**
     * Refreshes the target for either attacking or retreating.
     */
    private void refreshTarget() {
        Coordinate target;
        switch (draculaState){
            case ATTACK:
                target = targetPlayer().currentTile;
                break;
            case RETREAT:
                // follow through some distance
                float x = facingDir.getX();
                float y = facingDir.getY();
                Vector back = facingDir.scale(-1);
                target = new Coordinate(
                        x == 0 ? currentTile.x : currentTile.x + back.getX() * followThroughDist,
                        y == 0 ? currentTile.y : currentTile.y + back.getY() * followThroughDist
                );

                // if the target is unpassable, backtrack
                while(!curLevelState.map.isPassable[(int) target.x][(int) target.y]){
                    target.add(facingDir.getX(), facingDir.getY());
                }
                break;
            default:
                System.out.println("No need for targets: " + draculaState);
                return;
        }

        navPath = navGraph.findPath(currentTile, target);
        if(navPath == null) return;
        navTarget = navPath.isEmpty() ? null : navPath.remove(0);
    }

    /**
     * Attack State.
     * Charge at players until one is in front of me.
     * Attack the player, then retreat.
     * @param delta Time step from last frame.
     */
    private void attack(int delta){
        if(getAnimLock()) return;

        if(isPlayerBeforeMe(1)){
            navTarget = null;
            animateAttack(getMeleeSheet());
            return;
        }

        refreshTargetClock += delta;
        if(refreshTargetClock > refreshTargetTime){
            refreshTargetClock = 0;
            refreshTarget();
        }
    }

    /**
     * Retreat State. Run away from players.
     */
    private void retreat(){
        if(navTarget == null){
            draculaState = DraculaState.ATTACK;
        }
    }

    /**
     * Vanish State. Wait for bats to die then reappear.
     */
    private void vanish(){
        if(bats.size() == 1){
            // respawn on that bat
            DraculaBat bat = bats.remove(0);
            swarmManager.deadSwarm.add(bat);
            curLevelState.deadObjects.add(bat);

            currentTile = bat.currentTile;
            currentTilePlusPartial = bat.currentTilePlusPartial;

            draculaState = DraculaState.ATTACK;

            if(phaseCount == 2){
                maxHealth = phase2MaxHealth;
                currentHealth = maxHealth;
            }
            else if(phaseCount == 1){
                maxHealth = phase1MaxHealth;
                currentHealth = maxHealth;
            }
            setHealthBar();
        }
        else if(bats.isEmpty()){
            // respawn at starting tile
            currentTile = startingTile;
            currentTilePlusPartial = startingTile;

            draculaState = DraculaState.ATTACK;

            if(phaseCount == 2){
                maxHealth = phase2MaxHealth;
                currentHealth = maxHealth;
            }
            else if(phaseCount == 1){
                maxHealth = phase1MaxHealth;
                currentHealth = maxHealth;
            }
            setHealthBar();
        }
    }

    private void spawnBats(Coordinate[] spawnTiles) {
        DraculaBat bat;
        for (Coordinate spawnTile : spawnTiles) {
            Coordinate tile = curLevelState.map.isPassable[(int) spawnTile.x][(int) spawnTile.y] ?
                    spawnTile : currentTile;
            bat = new DraculaBat(tile, curLevelState, swarmManager, this);
            bats.add(bat);
            curLevelState.newObjects.add(bat);
            swarmManager.newSwarm.add(bat);
        }
    }

    private void disappear(){
        if(curAnim != null){
            removeAnimation(curAnim);
        }
        else{
            removeImage(idleImage);
        }
        draculaState = DraculaState.VANISH;
    }

    //region Character Controller
    @Override
    public String getRunSheet(int x, int y) {
        return DraculaPunchGame.getSheetHelper(
                DraculaPunchGame.DRACULA_WALK_0_DEG,
                DraculaPunchGame.DRACULA_WALK_180_DEG,
                DraculaPunchGame.DRACULA_WALK_90_DEG,
                DraculaPunchGame.DRACULA_WALK_270_DEG,
                x,
                y
        );
    }

    @Override
    public String getIdleSheet() {
        return DraculaPunchGame.DRACULA_IDLE;
    }

    @Override
    public String getMeleeSheet() {
        return DraculaPunchGame.getSheetHelper(
                DraculaPunchGame.DRACULA_MELEE_0_DEG,
                DraculaPunchGame.DRACULA_MELEE_180_DEG,
                DraculaPunchGame.DRACULA_MELEE_90_DEG,
                DraculaPunchGame.DRACULA_MELEE_270_DEG,
                (int) facingDir.getX(),
                (int) facingDir.getY()
        );
    }

    @Override
    public String getRangedSheet() {
        return null;
    }
    //endregion

    //region Enemy Controller
    @Override
    public void postAttackAction() {
        draculaState = DraculaState.RETREAT;
        refreshTarget();
    }

    @Override
    public void activate() {
        draculaState = DraculaState.ATTACK;
    }

    @Override
    public void deactivate() {
        navPath = navGraph.findPath(currentTile, startingTile);
        navTarget = navPath.get(0);
        draculaState = DraculaState.IDLE;
    }
    //endregion

    //region IAttacker
    @Override
    public void attack(AttackType attackType) {
        switch (attackType){
            case MELEE:
                Coordinate front = getLinedTiles(1, facingDir).getFirst();

                // damage all the things
                ArrayList<GameObject> targets = curLevelState.getObjectsFromTile(front);
                for(GameObject target : targets){
                    if(target instanceof IDamageable && !(target instanceof EnemyController)){
                        ((IDamageable) target).takeDamage(meleeDamage);
                    }
                }
                break;
            case RANGED:
                System.out.println("No Ranged Attack");
                break;
            default:
                System.out.println("Unknown Attack Type: " + attackType);
        }
    }
    //endregion
}
