package dracula_punch.States;

import dracula_punch.DraculaPunchGame;
import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class LoseState extends BasicGameState {
    @Override
    public int getID() {
        return DraculaPunchGame.LOSE_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(ResourceManager.getImage(DraculaPunchGame.LOSE_SCREEN), 0,0);

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input input = gameContainer.getInput();
        if (input.isKeyDown(Input.KEY_SPACE)) {
            stateBasedGame.enterState(DraculaPunchGame.CHARACTER_SELECT_STATE, new FadeOutTransition(), new FadeInTransition());
        }
    }
}
