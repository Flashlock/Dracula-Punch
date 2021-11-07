package dracula_punch.States;

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
  private int tilesInWindowX = 20;
  private int tilesInWindowY = 20;
  private Coordinate cameraTile = new Coordinate();
  private float movingTime;
  private float cycleTime = 100;
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
    int floorID = map.getLayerIndex("Floor");
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    graphics.drawString("Test State", 10, 25);

    // render layers individually to avoid Slick2d bug
    Coordinate iso = toIso(cameraTile);
    map.render(iso.x, iso.y, cameraTile.x, cameraTile.y, tilesInWindowX, tilesInWindowY, 0, true);
    map.render(iso.x, iso.y, cameraTile.x, cameraTile.y, tilesInWindowX, tilesInWindowY, 1, true);
    map.render(iso.x, iso.y, cameraTile.x, cameraTile.y, tilesInWindowX, tilesInWindowY, 2, true);
    map.render(iso.x, iso.y, cameraTile.x, cameraTile.y, tilesInWindowX, tilesInWindowY, 3, true);
    graphics.fillOval(dpg.screenWidth / 2 - 5, dpg.screenHeight / 2 - 5, 10, 10);
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
    Input input = gameContainer.getInput();
    moveCamera(input, delta);
  }

  private Coordinate toIso(Coordinate tile) {
    Coordinate cartesian = new Coordinate();
    cartesian.x = -tile.x * map.getTileWidth() / 2;
    cartesian.y = -tile.y * map.getTileHeight();
    Coordinate isometric = new Coordinate();
    isometric.x = cartesian.x - cartesian.y + dpg.screenWidth / 2 - map.getTileWidth() / 2;
    isometric.y = (cartesian.x + cartesian.y) / 2 + dpg.screenHeight / 2 - map.getTileHeight() / 2;
    return isometric;
  }

  private void moveCamera(Input input, int delta) {
    if (movingTime < cycleTime) { movingTime += delta; }
    else {
      movingTime = 0;
      if (cameraTile.y > 0 && (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP))) {
        cameraTile.y--;
        System.out.println(cameraTile.x + " " + cameraTile.y);
      }
      if (cameraTile.x > 0 && (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT))) {
        cameraTile.x--;
        System.out.println(cameraTile.x + " " + cameraTile.y);
      }
      if (cameraTile.y < map.getHeight() - 1 && (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN))) {
        cameraTile.y++;
        System.out.println(cameraTile.x + " " + cameraTile.y);
      }
      if (cameraTile.x < map.getWidth() - 1 && (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT))) {
        cameraTile.x++;
        System.out.println(cameraTile.x + " " + cameraTile.y);
      }
    }
  }
}
