package dracula_punch.States;

import dracula_punch.Camera;
import dracula_punch.Coordinate;
import dracula_punch.TiledMap.DPTiledMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import dracula_punch.DraculaPunchGame;
import org.newdawn.slick.tiled.TiledMap;

public class TestState extends BasicGameState {

  private TiledMap map;
  private Camera camera;
  private DraculaPunchGame dpg;

  @Override
  public int getID() {
    return DraculaPunchGame.TEST_STATE;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    DraculaPunchGame dpg = (DraculaPunchGame)game;

    map = new DPTiledMap(DraculaPunchGame.MAP);
    camera = new Camera(map);
    int floorID = map.getLayerIndex("Floor");
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    graphics.drawString("Test State", 10, 25);
    graphics.scale(camera.zoomFactor, camera.zoomFactor);
    int tilesInWindowX = 100;
    int tilesInWindowY = 100;
    float screenOffsetX = DraculaPunchGame.screenWidth / camera.zoomFactor / 2;
    float screenOffsetY = DraculaPunchGame.screenHeight / camera.zoomFactor / 2;
    int x = (int)(camera.isometric.x+screenOffsetX);
    int y = (int)(camera.isometric.y+screenOffsetY);
    // render layers individually to avoid Slick2d bug
    map.render(x, y, 0, 0, tilesInWindowX, tilesInWindowY, 0, true);
    map.render(x, y, 0, 0, tilesInWindowX, tilesInWindowY, 1, true);
    map.render(x, y, 0, 0, tilesInWindowX, tilesInWindowY, 2, true);
    map.render(x, y, 0, 0, tilesInWindowX, tilesInWindowY, 3, true);
    map.render(x, y, 0, 0, tilesInWindowX, tilesInWindowY, 5, true);
    graphics.fillOval(screenOffsetX - 5, screenOffsetY - 5, 10, 10);
    Coordinate test = new Coordinate(1,0);
    test = test.getIsometricFromTile(map);
    graphics.fillOval(x - test.x - 5, y - test.y - 5, 10, 10);
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
    controls(gameContainer.getInput());
    camera.update(delta);
  }

  public void controls(Input input){
    camera.moveLeft = input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT);
    camera.moveRight = input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT);
    camera.moveUp = input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP);
    camera.moveDown = input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN);
    camera.zoomOut = input.isKeyDown(Input.KEY_O);
    camera.zoomIn = input.isKeyDown(Input.KEY_I);
  }
}
