package dracula_punch;

import dracula_punch.States.TestLevelState;
import jig.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import jig.Entity;

public class DraculaPunchGame extends StateBasedGame {
  //region State ID's
  public static int TEST_STATE = -1;
  //endregion

  public static final int ANIMATION_DURATION = 50;
  public static int SCREEN_WIDTH, SCREEN_HEIGHT;

  public static final String MAP = "dracula_punch/Resources/Tiled/test.tmx";

  //region Amanda
  public static final String AMANDA_RUN_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda_Run/Amanda_Run_0_Deg.png";
  public static final String AMANDA_RUN_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda_Run/Amanda_Run_90_Deg.png";
  public static final String AMANDA_RUN_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda_Run/Amanda_Run_180_Deg.png";
  public static final String AMANDA_RUN_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda_Run/Amanda_Run_270_Deg.png";
  //endregion

  //region Austin
  public static final String AUSTIN_RUN_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin_Run/Austin_Run_0_Deg.png";
  public static final String AUSTIN_RUN_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin_Run/Austin_Run_90_Deg.png";
  public static final String AUSTIN_RUN_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin_Run/Austin_Run_180_Deg.png";
  public static final String AUSTIN_RUN_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin_Run/Austin_Run_270_Deg.png";
  //endregion

  //region Ritta
  public static final String RITTA_RUN_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta_Run/Ritta_Run_0_Deg.png";
  public static final String RITTA_RUN_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta_Run/Ritta_Run_90_Deg.png";
  public static final String RITTA_RUN_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta_Run/Ritta_Run_180_Deg.png";
  public static final String RITTA_RUN_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta_Run/Ritta_Run_270_Deg.png";
  //endregion

  public DraculaPunchGame(String name, int width, int height) {
    super(name);
    SCREEN_WIDTH = width;
    SCREEN_HEIGHT = height;

    Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
  }

  @Override
  public void initStatesList(GameContainer gameContainer) throws SlickException {
    addState(new TestLevelState());

    ResourceManager.loadImage(AMANDA_RUN_0_DEG);
    ResourceManager.loadImage(AMANDA_RUN_90_DEG);
    ResourceManager.loadImage(AMANDA_RUN_180_DEG);
    ResourceManager.loadImage(AMANDA_RUN_270_DEG);

    ResourceManager.loadImage(AUSTIN_RUN_0_DEG);
    ResourceManager.loadImage(AUSTIN_RUN_90_DEG);
    ResourceManager.loadImage(AUSTIN_RUN_180_DEG);
    ResourceManager.loadImage(AUSTIN_RUN_270_DEG);

    ResourceManager.loadImage(RITTA_RUN_0_DEG);
    ResourceManager.loadImage(RITTA_RUN_90_DEG);
    ResourceManager.loadImage(RITTA_RUN_180_DEG);
    ResourceManager.loadImage(RITTA_RUN_270_DEG);
  }

  public static void main(String[] args) {
    AppGameContainer app;
    ResourceManager.filterMethod = ResourceManager.FILTER_NEAREST;
    try {
      app = new AppGameContainer(new DraculaPunchGame("Dracula Punch", 800, 600));
      app.setDisplayMode(800, 600, false);
      app.setVSync(true);
      app.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }

  }
}
