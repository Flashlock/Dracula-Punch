package dracula_punch.Characters;

import jig.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Inherit from this instead of Entity.
 * This way we can allow our entities to update and render themselves.
 */
public abstract class GameObject extends Entity {

    public GameObject(final float x, final float y){ super(x, y); }

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
}
