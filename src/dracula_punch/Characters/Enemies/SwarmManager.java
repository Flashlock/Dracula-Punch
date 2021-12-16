package dracula_punch.Characters.Enemies;

import dracula_punch.Characters.GameObject;
import dracula_punch.States.LevelState;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.LinkedList;

public class SwarmManager extends GameObject {
    public final ArrayList<EnemyController> deadSwarm;
    public final ArrayList<EnemyController> newSwarm;
    private final LinkedList<EnemyController> swarm;
    private final float sqrEngageRad;
    private final LevelState curLevelState;
    private final Vector position;

    private boolean isActivated;

    /**
     * There's no image, so use x and y for coordinates
     * @param x X coordinate it is placed at
     * @param y Y coordinate it is placed at
     */
    public SwarmManager(float x, float y, float engagementRadius, LevelState curLevelState) {
        super(x, y);
        position = new Vector(x, y);
        this.curLevelState = curLevelState;
        swarm = new LinkedList<>();
        deadSwarm = new ArrayList<>();
        newSwarm = new ArrayList<>();
        sqrEngageRad = engagementRadius * engagementRadius;
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        swarm.addAll(newSwarm);
        newSwarm.clear();

        swarm.removeAll(deadSwarm);
        deadSwarm.clear();

        // If my swarm is dead, I am dead
        if(swarm.isEmpty()){
            curLevelState.deadObjects.add(this);
            return;
        }

        // find avg distance to players
        float dist = curLevelState.playerObjects.isEmpty() ?
                Float.MAX_VALUE :
                position.distanceSquared(curLevelState.getAvgPlayerPos());

        if(dist < sqrEngageRad && !isActivated){
            // activate
            for(EnemyController controller : swarm){
                controller.activate();
            }
            isActivated = true;
        }
        else if(dist > sqrEngageRad && isActivated){
            // deactivate
            for(EnemyController controller : swarm){
                controller.deactivate();
            }
            isActivated = false;
        }
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {}
}
