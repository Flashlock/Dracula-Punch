package dracula_punch.Characters.Enemies;

import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Characters.GameObject;
import dracula_punch.Characters.Players.PlayerController;
import dracula_punch.Damage_System.IAttacker;
import dracula_punch.Pathfinding.DijkstraGraph;
import dracula_punch.Pathfinding.DijkstraNode;
import dracula_punch.States.LevelState;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class EnemyController extends CharacterController implements IAttacker {
    protected final DijkstraGraph navGraph;
    protected ArrayList<DijkstraNode> navPath;
    protected DijkstraNode navTarget;

    public EnemyController(float x, float y, Coordinate startingTile, LevelState curLevelState) {
        super(x, y, curLevelState);
        currentTile = new Coordinate(startingTile);
        currentTilePlusPartial = new Coordinate(startingTile);

        /*
         * TOTAL_MOVE_TIME acts as move speed.
         * Small Values = Big Speed.
         */
        TOTAL_MOVE_TIME = 100;
        movingTime = TOTAL_MOVE_TIME;

        navGraph = new DijkstraGraph(curLevelState.map);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        super.update(gameContainer, stateBasedGame, delta);
        move();
        smoothlyCatchUpToCurrentTile(delta);
    }

    /**
     * Perform any post attack actions
     */
    public abstract void postAttackAction();

    /**
     * Updates the controllers current tile as it moves along its nav path
     */
    private void move(){
        // do nothing if there's no target
        if(navTarget == null) return;

        float percentMoved = calculatePercentMoved();
        if(percentMoved == 0f && currentTile.isEqual(navTarget.coordinate)){
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
                // start from stopped or change directions
                animateMove(dir);
            }
        }
        else if(percentMoved == 0f){
            // update current tile
            changeCurrentTile((int)(navTarget.x - currentTile.x), (int) (navTarget.y - currentTile.y));
        }
    }

    /**
     * @param range Distance across the map in a line to check for players in front of me
     * @return True if I encounter a player
     */
    protected boolean isPlayerBeforeMe(int range){
        LinkedList<Coordinate> tiles = getFacingTiles(range);
        ArrayList<GameObject> gameObjects;
        for(Coordinate tile : tiles){
            // can preemptively stop if tiles are no longer passable
            if(!curLevelState.map.isPassable[(int) tile.x][(int) tile.y]){
                break;
            }

            gameObjects = curLevelState.getObjectsFromTile(tile);
            for(GameObject gameObject : gameObjects){
                if(gameObject instanceof PlayerController) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Targets players using weighted random numbers.
     * The closer the player is, the more likely they'll be targeted.
     * @return The targeted player
     */
    protected PlayerController targetPlayer(){
        // find sum and distances
        float sum = 0;
        float[] distances = new float[curLevelState.playerObjects.size()];
        for(int i = 0; i < distances.length; i++){
            Vector myPos = new Vector(currentTile.x, currentTile.y);
            Coordinate playerTile = curLevelState.playerObjects.get(i).currentTile;
            Vector pPos = new Vector(playerTile.x, playerTile.y);
            distances[i] = 1 / myPos.distanceSquared(pPos);
            sum += distances[i];
        }

        // roll the dice
        float value = (float) Math.random();
        float base = 0;
        for(int i = 0; i < distances.length; i++){
            float weight = distances[i] / sum;
            if(value > base && value <= weight + base){
                return (PlayerController) curLevelState.playerObjects.get(i);
            }
            base += weight;
        }

        // if no character was selected then a 0 was rolled
        return (PlayerController) curLevelState.playerObjects.get(0);
    }
}
