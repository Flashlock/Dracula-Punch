package dracula_punch.Characters;

import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class CharacterController extends Entity {
    protected Animation curAnim;
    protected float scaleFactor;
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
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics){
        // No idle pose yet, so everything is based off of curAnim
        if(curAnim == null) return;

        Vector screenOffset = curLevelState.getScreenOffset();
        float x = screenOffset.getX() - 5 + xRenderOffset;
        float y = screenOffset.getY() - 5 - curAnim.getHeight() / 2f * scaleFactor + yRenderOffset;

        setPosition(x, y);
        render(graphics);
    }

    /**
     * Animate the controller's movement
     * @param direction The direction to move
     */
    public void animateMove(Vector direction){
        int x = (int) direction.getX();
        int y = (int) direction.getY();

        String sheet = getSheet(x, y);

        if(sheet != null){
            removeAnimation(curAnim);
            curAnim = new Animation(
                    ResourceManager.getSpriteSheet(
                            sheet, getRunWidth(), getRunHeight()
                    ),
                    DraculaPunchGame.ANIMATION_DURATION
            );
            curAnim.setLooping(true);
            addAnimation(curAnim);
        }
        else if(curAnim != null){
            curAnim.stop();
        }
    }

    /**
     * Return the character's Sprite Sheet for the given direction
     * @param x facing direction x
     * @param y facing direction y
     */
    public abstract String getSheet(int x, int y);

    /**
     * @return The width of each sprite in the character's run animation
     */
    public abstract int getRunWidth();

    /**
     * @return The height of each sprite in the character's run animation
     */
    public abstract int getRunHeight();
}
