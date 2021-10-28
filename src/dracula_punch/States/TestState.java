package dracula_punch.States;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import dracula_punch.DraculaPunchGame;
import org.newdawn.slick.tiled.TiledMap;

public class TestState extends BasicGameState {

    private TiledMap tiledMap;

    @Override
    public int getID() {
        return DraculaPunchGame.TEST_STATE;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        tiledMap = new TiledMap(DraculaPunchGame.MAP);
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawString("Test State", 10, 25);
        DraculaPunchGame dpg = (DraculaPunchGame) stateBasedGame;

        // this point is the top of the diamond
        tiledMap.render(dpg.screenWidth / 2, 0);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }
}
