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
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

/**
 * Bats Fly at you, attack, and then retreat.
 * They select their targets with weighted random numbers.
 * The closer you are to the bat, the higher probability they'll attack.
 */
public class BatController extends EnemyController{

    public enum BatState { IDLE, ATTACK, RETREAT }
    protected BatState batState;
    public BatState getBatState(){ return batState; }

    private int meleeDamage = 1;
    private final int meleeActionFrame = 7;

    private int followThroughDist = 8;

    public BatController(Coordinate startingTile, LevelState curLevelState, SwarmManager swarmManager) {
        super(0, 0, startingTile, curLevelState, swarmManager);
        setScale(.8f);

        TOTAL_MOVE_TIME = 200;

        maxHealth = 3;
        currentHealth = maxHealth;
        healthBars = new Image[]{
                ResourceManager.getImage(DraculaPunchGame.BAT_HEALTH_3),
                ResourceManager.getImage(DraculaPunchGame.BAT_HEALTH_2),
                ResourceManager.getImage(DraculaPunchGame.BAT_HEALTH_1)
        };
        setHealthBar();

        attackAction = new AttackAction(this, meleeActionFrame, AttackType.MELEE);
        batState = BatState.IDLE;
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        super.update(gameContainer, stateBasedGame, delta);
        switch (batState){
            case IDLE:
                break;
            case ATTACK:
                attack(delta);
                break;
            case RETREAT:
                retreat();
                break;
            default:
                System.out.println("Unknown State: " + batState);
        }
    }

    @Override
    public void activate() {
        ResourceManager.getSound(DraculaPunchGame.BAT_SND).play();
        batState = BatState.ATTACK;
    }

    @Override
    public void deactivate() {
        ResourceManager.getSound(DraculaPunchGame.BAT_SND).stop();
        navPath = navGraph.findPath(currentTile, startingTile);
        navTarget = navPath.get(0);
        batState = BatState.IDLE;
    }

    /**
     * Refreshes the target for either attacking or retreating.
     */
    private void refreshTarget() {
        Coordinate target;
        switch (batState){
            case ATTACK:
                target = targetPlayer().currentTile;
                break;
            case RETREAT:
                // follow through some distance
                float x = facingDir.getX();
                float y = facingDir.getY();
                target = new Coordinate(
                        x == 0 ? currentTile.x : currentTile.x + facingDir.getX() * followThroughDist,
                        y == 0 ? currentTile.y : currentTile.y + facingDir.getY() * followThroughDist
                );

                // if the target is unpassable, backtrack
                Vector back = facingDir.scale(-1);
                while(!curLevelState.map.isPassable[(int) target.x][(int) target.y]){
                    target.add(back.getX(), back.getY());
                }
                break;
            default:
                System.out.println("No need for targets: " + batState);
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
            batState = BatState.ATTACK;
        }
    }


    //region Character Controller
    @Override
    public String getRunSheet(int x, int y) {
        return DraculaPunchGame.getSheetHelper(
                DraculaPunchGame.BAT_FLY_0,
                DraculaPunchGame.BAT_FLY_180,
                DraculaPunchGame.BAT_FLY_90,
                DraculaPunchGame.BAT_FLY_270,
                x, y
        );
    }

    @Override
    public String getIdleSheet() {
        return DraculaPunchGame.BAT_IDLE;
    }

    @Override
    public String getMeleeSheet() {
        return DraculaPunchGame.getSheetHelper(
                DraculaPunchGame.BAT_ATTACK_0,
                DraculaPunchGame.BAT_ATTACK_180,
                DraculaPunchGame.BAT_ATTACK_90,
                DraculaPunchGame.BAT_ATTACK_270,
                (int) facingDir.getX(),
                (int) facingDir.getY()
        );
    }

    @Override
    public String getRangedSheet() {
        return null;
    }
    //endregion

    //region EnemyController
    @Override
    public void postAttackAction() {
        batState = BatState.RETREAT;
        refreshTarget();
        animateMove(facingDir);
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
