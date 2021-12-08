package dracula_punch.Characters.Enemies;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Camera.Coordinate;
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

public class BatController extends EnemyController{
    private int meleeDamage = 5;
    private final int meleeActionFrame = 7;

    private Coordinate targetTile;
    private boolean isMoving;
    public boolean getIsMoving(){ return isMoving; }

    private final DijkstraGraph navGraph;
    private ArrayList<DijkstraNode> navPath;

    private final int refreshTargetTime = 3000;
    private int refreshTargetClock = 0;

    public BatController(Coordinate startingTile, LevelState curLevelState) {
        super(0, 0, curLevelState);
        attackAction = new AttackAction(this, meleeActionFrame, AttackType.MELEE);
        currentTile = new Coordinate(startingTile);
        currentTilePlusPartial = new Coordinate(startingTile);
        navGraph = new DijkstraGraph(curLevelState.map);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        super.update(gameContainer, stateBasedGame, delta);
        System.out.println(currentTile.x + ", " + currentTile.y);
//        refreshTargetClock += delta;
//        if(refreshTargetClock > refreshTargetTime){
//            determineTarget();
//            refreshTargetClock = 0;
//        }
//        move();
//        smoothlyCatchUpToCurrentTile(delta);
    }

    private void determineTarget() {
        // TODO Implement AI
        GameObject target = curLevelState.playerObjects.get(0);
        navPath = navGraph.findPath(
                (int) currentTile.x,
                (int) currentTile.y,
                (int) target.currentTile.x,
                (int) target.currentTile.y
        );
    }

    private void move() {
        boolean moved = true;
        if (movingTime < TOTAL_MOVE_TIME){ }
        else if (targetTile != null && !currentTile.isEqual(targetTile) && !navPath.isEmpty()){
            DijkstraNode node = navPath.remove(0);
            Coordinate next = new Coordinate(node.x, node.y);
            changeCurrentTile((int)(next.x - currentTile.x), (int)(next.y - currentTile.y));

            float x = next.x - previousTile.x;
            float y = previousTile.y - next.y;
            Vector dir = new Vector(x, y);
            dir = dir.scale(1 / dir.length());
            if(dir.getX() != facingDir.getX() || dir.getY() != facingDir.getY()){
                // Change directions
                animateMove(dir);
            }
        }
        else moved = false;

        if(!moved && isMoving){
            // stopped
            animateMove(new Vector(0,0));
        }
        isMoving = moved;
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
