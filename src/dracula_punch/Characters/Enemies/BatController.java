package dracula_punch.Characters.Enemies;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Characters.GameObject;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

/**
 * Bats Fly at you, attack, and then retreat.
 * They select their targets with weighted random numbers.
 * The closer you are to the bat, the higher probability they'll attack.
 */
public class BatController extends EnemyController{
    public enum BatState { IDLE, ATTACK, RETREAT }
    private BatState batState;
    public BatState getBatState(){ return batState; }

    private int meleeDamage = 5;
    private final int meleeActionFrame = 7;

    private final int refreshTargetTime = 3000;
    private int refreshTargetClock = 5000;

    private boolean isAttacking;

    public BatController(Coordinate startingTile, LevelState curLevelState) {
        super(0, 0, startingTile, curLevelState);
        attackAction = new AttackAction(this, meleeActionFrame, AttackType.MELEE);
        batState = BatState.ATTACK;
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
                retreat(delta);
                break;
            default:
                System.out.println("Unknown State: " + batState);
        }
    }

    private void refreshTarget() {
        CharacterController target = curLevelState.playerObjects.get(0);
        navPath = navGraph.findPath(currentTile, target.currentTile);
        navTarget = navPath.isEmpty() ? null : navPath.remove(0);
    }

    /**
     * Attack State.
     * Charge at players until one is in front of me.
     * Attack the player, then retreat.
     * @param delta Time step from last frame.
     */
    private void attack(int delta){
        if(isPlayerBeforeMe(1)){
            navTarget = null;
            animateAttack(getMeleeSheet());
            isAttacking = true;
            batState = BatState.RETREAT;
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
     * @param delta Time step from last frame.
     */
    private void retreat(int delta){
        if(isAttacking && curAnim != null) return;
        isAttacking = false;
        System.out.println("Enter Retreat");
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

    //region IAttacker
    @Override
    public void attack(AttackType attackType) {
        switch (attackType){
            case MELEE:
                Coordinate front = getFacingTiles(1).getFirst();

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
