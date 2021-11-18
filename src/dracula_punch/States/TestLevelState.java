package dracula_punch.States;

import dracula_punch.Actions.*;
import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.*;
import dracula_punch.TestEnemy;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import dracula_punch.DraculaPunchGame;

import java.util.ArrayList;

public class TestLevelState extends LevelState {
  private ArrayList<GameObject> gameObjects;

  @Override
  public int getID() {
    return DraculaPunchGame.TEST_STATE;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    gameObjects = new ArrayList<>();

    map = new DPTiledMap(DraculaPunchGame.MAP);
    camera = new Camera(map);

    GameObject chosenPlayer;
    if (DraculaPunchGame.characterChoice == DraculaPunchGame.charIdEnum.AMANDA) {
      chosenPlayer = new AmandaController(
          DraculaPunchGame.SCREEN_WIDTH / 2f,
          DraculaPunchGame.SCREEN_HEIGHT / 2f,
          this
      );
    }
    else if (DraculaPunchGame.characterChoice == DraculaPunchGame.charIdEnum.AUSTIN) {
      chosenPlayer = new AustinController(
          DraculaPunchGame.SCREEN_WIDTH / 2f,
          DraculaPunchGame.SCREEN_HEIGHT / 2f,
          this
      );
    }
    else{
      chosenPlayer = new RittaController(
          DraculaPunchGame.SCREEN_WIDTH / 2f,
          DraculaPunchGame.SCREEN_HEIGHT / 2f,
          this
      );
    }
    gameObjects.add(chosenPlayer);
    GameObject testEnemy = new TestEnemy(new Coordinate(90,90), this);
    gameObjects.add(testEnemy);
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    graphics.drawString("Test State", 10, 25);
    graphics.scale(camera.zoomFactor, camera.zoomFactor);
    int tilesInWindowX = map.getWidth();
    int tilesInWindowY = map.getHeight();

    Vector camPos = getCameraPosition();
    int camX = (int) camPos.getX();
    int camY = (int) camPos.getY();

    // Render layers individually to avoid Slick2d bug
    map.render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 0, true);

    for(GameObject gameObject : gameObjects){
      gameObject.render(gameContainer, stateBasedGame, graphics);
    }

    map.render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 1, true);
    map.render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 2, true);
//    map.render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 3, true);

//    Vector screenOffset = getScreenOffset();
//    graphics.fillOval(screenOffset.getX() - 5, screenOffset.getY() - 5, 10, 10);
//    Coordinate test = new Coordinate(1,0);
//    test = test.getIsometricFromTile(map);
//    graphics.fillOval(camX - test.x - 5, camY - test.y - 5, 10, 10);
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
    controls(gameContainer.getInput());
    camera.update(delta);

    for(GameObject gameObject : gameObjects){
      gameObject.update(gameContainer, stateBasedGame, delta);
    }
  }

  /**
   * Handles game input
   * @param input
   */
  private void controls(Input input){
    // Observe input changes
    boolean left = input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT);
    boolean right = input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT);
    boolean up = input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP);
    boolean down = input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN);
    boolean isMoving = camera.moveLeft || camera.moveRight || camera.moveDown || camera.moveUp;

    // Trigger event on change
    if(!camera.moveLeft && left){
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(-1, 0));
      }
    }
    else if(!camera.moveRight && right){
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(1, 0));
      }
    }
    else if(!camera.moveUp && up){
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(0, 1));
      }
    }
    else if(!camera.moveDown && down){
      // change down
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(0, -1));
      }
    }
    else if(isMoving && !(left || right || up || down)){
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(0, 0));
      }
    }

    // Apply movement to camera
    camera.moveLeft = left;
    camera.moveRight = right;
    camera.moveUp = up;
    camera.moveDown = down;
    camera.zoomOut = input.isKeyDown(Input.KEY_O);
    camera.zoomIn = input.isKeyDown(Input.KEY_I);
  }
}
