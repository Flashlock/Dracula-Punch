package dracula_punch.Characters;

import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

/**
 * All Characters - including enemies - will inherit from this class
 */
public abstract class CharacterController extends GameObject {
    protected Animation curAnim;
    protected float scaleFactor;
    protected int xRenderOffset, yRenderOffset;
    protected LevelState curLevelState;
    protected Vector facingDir;

    private Image idleImage;

    public CharacterController(final float x, final float y, LevelState curLevelState){
        super(x, y);
        this.curLevelState = curLevelState;

        // Generate a random idle sprite
        int dirX, dirY;
        double randX = Math.random() * 1.5f;
        // X direction
        if(randX < .5f){
            dirX = -1;
        }
        else if(randX < 1){
            dirX = 0;
        }
        else{
            dirX = 1;
        }
        // Y direction
        if(dirX != 0){
            dirY = 0;
        }
        else{
            dirY = Math.random() < .5f ? -1 : 1;
        }

        facingDir = new Vector(dirX, dirY);
        idleImage = getIdleSprite();
        addImage(idleImage);
    }
    public CharacterController(final float x, final float y, LevelState curLevelState, boolean lameWorkaround){
      super(x, y);
      this.curLevelState = curLevelState;

      // Generate a random idle sprite
      int dirX, dirY;
      double randX = Math.random() * 1.5f;
      // X direction
      if(randX < .5f){
        dirX = -1;
      }
      else if(randX < 1){
        dirX = 0;
      }
      else{
        dirX = 1;
      }
      // Y direction
      if(dirX != 0){
        dirY = 0;
      }
      else{
        dirY = Math.random() < .5f ? -1 : 1;
      }
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics){
        Vector screenOffset = curLevelState.getScreenOffset();
        float x = screenOffset.getX() - 5 + xRenderOffset;
        // If idle pose, no current animation
        float y = curAnim == null ?
                screenOffset.getY() - 5 - getIdleHeight() / 2f * scaleFactor + yRenderOffset
                : screenOffset.getY() - 5 - curAnim.getHeight() / 2f * scaleFactor + yRenderOffset;

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

        String sheet = getRunSheet(x, y);
        String dirError = facingDir == null ? "Unknown Facing Direction: null" :
                "Unknown Facing Direction: " + facingDir.getX() + ", " + facingDir.getY();

        if(sheet != null){
            if(curAnim == null){
                // If there's no animation, then we have an idle image to remove
                removeImage(idleImage);
            }
            else{
                removeAnimation(curAnim);
            }

            // Set the new animation
            curAnim = new Animation(
                    ResourceManager.getSpriteSheet(
                            sheet, getRunWidth(), getRunHeight()
                    ),
                    DraculaPunchGame.ANIMATION_DURATION
            );
            curAnim.setLooping(true);
            addAnimation(curAnim);

            facingDir = direction;
        }
        else if(curAnim != null){
            removeAnimation(curAnim);
            curAnim = null;

            idleImage = getIdleSprite();
            addImage(idleImage);
        }
    }

    private Image getIdleSprite(){
        int x = (int) facingDir.getX();
        int y = (int) facingDir.getY();

        if(y == 1 && x == 0){
            // 0
            return ResourceManager
                    .getSpriteSheet(getIdleSheet(), getIdleWidth(), getIdleHeight())
                    .getSprite(0, 0);
        }
        else if(y == -1 && x == 0){
            // 180
            return ResourceManager
                    .getSpriteSheet(getIdleSheet(), getIdleWidth(), getIdleHeight())
                    .getSprite(2, 0);
        }
        else if(y == 0 && x == -1){
            // 90
            return ResourceManager
                    .getSpriteSheet(getIdleSheet(), getIdleWidth(), getIdleHeight())
                    .getSprite(1, 0);
        }
        else if(y == 0 && x == 1){
            // 270
            return ResourceManager
                    .getSpriteSheet(getIdleSheet(), getIdleWidth(), getIdleHeight())
                    .getSprite(3, 0);
        }
        else{
            System.out.println("Unknown Facing Direction: " + x + ", " + y);
            return null;
        }
    }

    /**
     * @return The character's Run Sprite Sheet for the given direction
     * @param x facing direction x
     * @param y facing direction y
     */
    public abstract String getRunSheet(int x, int y);

    /**
     * @return The width of each sprite in the character's run animation
     */
    public abstract int getRunWidth();

    /**
     * @return The height of each sprite in the character's run animation
     */
    public abstract int getRunHeight();

    /**
     * @return The character's Idle Sprite Sheet - currently no animation
     */
    public abstract String getIdleSheet();

    /**
     * @return The width of each sprite in the character's idle animation
     */
    public abstract int getIdleWidth();

    /**
     * @return The height of each sprite in the character's idle animation
     */
    public abstract int getIdleHeight();
}
