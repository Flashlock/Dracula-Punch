package dracula_punch.Characters.Enemies;

import dracula_punch.Characters.GameObject;
import dracula_punch.Characters.Players.PlayerController;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
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

        // compare distance to each player
        // if only one activate signal, then activate
        // if all deactivate signals, then deactivate
        boolean activate = false;
        int deactivates = 0;
        for(PlayerController player : curLevelState.playerObjects){
            float dist = position.distanceSquared(new Vector(player.currentTile.x, player.currentTile.y));
            activate = dist < sqrEngageRad && !isActivated;
            if(activate) break;
            deactivates += dist > sqrEngageRad && isActivated ? 1 : 0;
        }
        if(activate){

            // activate
            for(EnemyController controller : swarm){
                controller.activate();
                if(controller instanceof BatController){
                    ResourceManager.getSound(DraculaPunchGame.BAT_SND).play();
                }
                if(controller instanceof GargoyleController){
                    ResourceManager.getSound(DraculaPunchGame.GARGOYLE_SND).play();
                }
            }
            isActivated = true;
        }
        else if(deactivates == curLevelState.playerObjects.size()){
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
