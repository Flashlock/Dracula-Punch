package dracula_punch.Characters;

import dracula_punch.States.LevelState;
import jig.Entity;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class CharacterController extends Entity {
    protected Animation curAnim;
    protected float moveSpeed, scaleFactor;
    protected int xRenderOffset, yRenderOffset;
    protected LevelState curLevelState;

    public CharacterController(final float x, final float y, LevelState curLevelState){
        super(x, y);
        this.curLevelState = curLevelState;
    }

    /**
     * Update the controller each frame
      * @param gameContainer
     * @param stateBasedGame
     * @param delta
     */
    public abstract void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta);

    /**
     * Render the controller each frame
     * @param gameContainer
     * @param stateBasedGame
     * @param graphics
     */
    public abstract void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics);

    /**
     * Animate the controller's movement
     * @param direction The direction to move
     */
    public abstract void animateMove(Vector direction);
}
