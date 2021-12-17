package dracula_punch.Characters.Enemies;

import dracula_punch.Camera.Coordinate;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Wait until all players are within the arena.
 * Then Spawn boss and activate minion spawners
 */
public class BossSpawner extends SwarmManager {
    private boolean[] beginBattle;
    private MinionSpawner[] batSpawners, garSpawners;
    private DraculaController dracula;

    /**
     * There's no image, so use x and y for coordinates
     *
     * @param x                X coordinate it is placed at
     * @param y                Y coordinate it is placed at
     * @param engagementRadius
     * @param curLevelState
     */
    public BossSpawner(float x, float y, float engagementRadius, LevelState curLevelState,
                       MinionSpawner[] batSpawners, MinionSpawner[] garSpawners) {
        super(x, y, engagementRadius, curLevelState);
        beginBattle = new boolean[3];
        this.batSpawners = batSpawners;
        this.garSpawners = garSpawners;
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        if(isActivated) return;
        Vector p1 = new Vector(curLevelState.playerOne.currentTile.x, curLevelState.playerOne.currentTile.y);
        Vector p2 = new Vector(curLevelState.playerTwo.currentTile.x, curLevelState.playerTwo.currentTile.y);
        Vector p3 = new Vector(curLevelState.playerThree.currentTile.x, curLevelState.playerThree.currentTile.y);

        beginBattle[0] = p1.distanceSquared(position) < sqrEngageRad;
        beginBattle[1] = p2.distanceSquared(position) < sqrEngageRad;
        beginBattle[2] = p3.distanceSquared(position) < sqrEngageRad;

        boolean activate = true;
        for(boolean b : beginBattle){
            if(!b){
                activate = false;
                break;
            }
        }
        if(activate){

            isActivated = true;
            activate();
        }
    }

    private void activate(){
        sqrEngageRad = Float.MAX_VALUE;
        // spawn dracula
        dracula = new DraculaController(
                new Coordinate(position.getX(), position.getY()),
                curLevelState,
                this
        );
        curLevelState.newObjects.add(dracula);
        ResourceManager.getSound(DraculaPunchGame.DRACULA_SPAWN_SND).play();
        curLevelState.map.setTileId(42, 87, curLevelState.map.getLayerIndex("NE Walls"), 19);
        curLevelState.map.isPassable[42][87] = false;
        curLevelState.map.setTileId(42, 88, curLevelState.map.getLayerIndex("NE Walls"), 19);
        curLevelState.map.isPassable[42][88] = false;

        dracula.activate();
    }
}
