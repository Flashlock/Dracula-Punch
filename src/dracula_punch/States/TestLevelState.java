package dracula_punch.States;

import dracula_punch.Actions.Action;
import dracula_punch.Camera.Camera;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Characters.RittaController;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import dracula_punch.DraculaPunchGame;

import java.util.ArrayList;

public class TestLevelState extends LevelState {
  private ArrayList<CharacterController> characters;

  @Override
  public int getID() {
    return DraculaPunchGame.TEST_STATE;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    characters = new ArrayList<>();

    map = new DPTiledMap(DraculaPunchGame.MAP);
    camera = new Camera(map);

    characters.add(
            new RittaController(
                    DraculaPunchGame.SCREEN_WIDTH / 2f,
                    DraculaPunchGame.SCREEN_HEIGHT / 2f,
                    this
            )
    );
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

    Vector camPos = getCameraPosition();
    int camX = (int) camPos.getX();
    int camY = (int) camPos.getY();

    // Render layers individually to avoid Slick2d bug
    map.render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 0, true);

    for(CharacterController characterController : characters){
      characterController.render(gameContainer, stateBasedGame, graphics);
    }
    // Will also want to render enemies, objects, etc. here

    map.render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 1, true);
    map.render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 2, true);
    map.render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 3, true);
    map.render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 5, true);

//    Vector screenOffset = getScreenOffset();
//    graphics.fillOval(screenOffset.getX() - 5, screenOffset.getY() - 5, 10, 10);
//    Coordinate test = new Coordinate(1,0);
//    test = test.getIsometricFromTile(map);
    //graphics.fillOval(camX - test.x - 5, camY - test.y - 5, 10, 10);
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
    controls(gameContainer.getInput());
    camera.update(delta);

    for(CharacterController characterController : characters){
      characterController.update(gameContainer, stateBasedGame, delta);
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
