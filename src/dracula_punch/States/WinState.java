package dracula_punch.States;

import dracula_punch.DraculaPunchGame;
import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class WinState extends BasicGameState {
    private int timer = 4000;

    @Override
    public int getID() {
        return DraculaPunchGame.WIN_STATE;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(true);
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        ResourceManager.loadImage(DraculaPunchGame.WIN_SCREEN);
        graphics.drawImage(ResourceManager.getImage(DraculaPunchGame.WIN_SCREEN), 0,0);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        timer -= i;
        if (timer <= 0) {
            stateBasedGame.enterState(DraculaPunchGame.START_STATE, new FadeOutTransition(), new FadeInTransition());
            timer = 4000;
        }
    }
}
