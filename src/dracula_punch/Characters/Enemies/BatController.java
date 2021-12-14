package dracula_punch.Characters.Enemies;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Characters.GameObject;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.DraculaPunchGame;
import dracula_punch.Pathfinding.DijkstraGraph;
import dracula_punch.Pathfinding.DijkstraNode;
import dracula_punch.States.LevelState;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class BatController extends EnemyController{
    private int meleeDamage = 5;
    private final int meleeActionFrame = 7;
    private float moveSpeed = .1f;

    private final DijkstraGraph navGraph;
    private ArrayList<DijkstraNode> navPath;
    private DijkstraNode navTarget;

    private final int refreshTargetTime = 3000;
    private int refreshTargetClock = 5000;

    public BatController(Coordinate startingTile, LevelState curLevelState) {
        super(0, 0, curLevelState);
        TOTAL_MOVE_TIME = 100;
        movingTime = 100;
        attackAction = new AttackAction(this, meleeActionFrame, AttackType.MELEE);
        currentTile = new Coordinate(startingTile);
        currentTilePlusPartial = new Coordinate(startingTile);

        navGraph = new DijkstraGraph(curLevelState.map);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        super.update(gameContainer, stateBasedGame, delta);

        refreshTargetClock += delta;
        if(refreshTargetClock > refreshTargetTime){
            refreshTargetClock = 0;
            refreshTarget();
        }

        move();
        smoothlyCatchUpToCurrentTile(delta);
    }

    private void refreshTarget() {
        CharacterController target = curLevelState.playerObjects.get(0);
        navPath = navGraph.findPath(currentTile, target.currentTile);
        navTarget = navPath.isEmpty() ? null : navPath.remove(0);
    }

    private void move(){
        if(navTarget == null) return;
        float percentMoved = calculatePercentMoved();
        if(percentMoved == 0 && currentTile.isEqual(navTarget.coordinate)){
            // re-evaluate
            // stop if we're empty
            if(navPath.isEmpty()){
                navTarget = null;
                animateMove(new Vector(0, 0));
                return;
            }

            // get next node in the path
            DijkstraNode nextTarget = navPath.remove(0);
            Vector dir = new Vector(
                    nextTarget.x - navTarget.x,
                    navTarget.y - nextTarget.y
            );
            navTarget = nextTarget;

            if(curAnim == null || dir.getX() != facingDir.getX() || dir.getY() != facingDir.getY()){
                // change directions
                animateMove(dir);
            }
        }
        else if(percentMoved == 0){
            // update current tile
            changeCurrentTile((int)(navTarget.x - currentTile.x), (int) (navTarget.y - currentTile.y));
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

    @Override
    public void attack(AttackType attackType) {
        switch (attackType){
            case MELEE:
                // get the tile in front of me
                int x = (int) (currentTile.x + facingDir.getX());
                int y = (int) (currentTile.y - facingDir.getY());

                // damage all the things
                ArrayList<GameObject> targets = curLevelState.getObjectsFromTile(x, y);
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
}
