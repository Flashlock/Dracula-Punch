package dracula_punch.States;

import dracula_punch.TiledMap.DPTiledMap;
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
        tiledMap = new DPTiledMap(DraculaPunchGame.MAP);
        int floorID = tiledMap.getLayerIndex("Floor");
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawString("Test State", 10, 25);
        DraculaPunchGame dpg = (DraculaPunchGame) stateBasedGame;

        // number of tiles to render
        int width = 50;
        int height = 50;
        int pxHeight = height * tiledMap.getTileHeight();

        // point it starts rendering from is the top of the diamond, each call to render
        // renders layer individually.
        tiledMap.render(dpg.screenWidth /2,dpg.screenHeight / 2 - pxHeight / 2-200,
          0,0,width,height, 0, true);
        tiledMap.render(dpg.screenWidth /2,dpg.screenHeight / 2 - pxHeight / 2-200,
          0,0,width,height, 1, true);
        tiledMap.render(dpg.screenWidth /2,dpg.screenHeight / 2 - pxHeight / 2-200,
          0,0,width,height, 2, true);
        tiledMap.render(dpg.screenWidth /2,dpg.screenHeight / 2 - pxHeight / 2-200,
          0,0,width,height, 3, true);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }
}
