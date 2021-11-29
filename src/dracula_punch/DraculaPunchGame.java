package dracula_punch;

import dracula_punch.States.CharacterSelectState;
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
  public static int CHARACTER_SELECT_STATE = 0;
  //endregion

  public static final String MAP = "dracula_punch/Resources/Tiled/amandatest.tmx";
  public static final int ANIMATION_DURATION = 50;
  public static int SCREEN_WIDTH, SCREEN_HEIGHT;
  public static charIdEnum characterChoice = charIdEnum.UNCHOSEN;
  public enum charIdEnum { UNCHOSEN, AMANDA, AUSTIN, RITTA }


  //region Amanda
  public static final String AMANDA_RUN_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda/Amanda_Run/Amanda_Run_0.png";
  public static final String AMANDA_RUN_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda/Amanda_Run/Amanda_Run_90.png";
  public static final String AMANDA_RUN_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda/Amanda_Run/Amanda_Run_180.png";
  public static final String AMANDA_RUN_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda/Amanda_Run/Amanda_Run_270.png";

  public static final String AMANDA_ATTACK_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda/Amanda_Attack/Amanda_Attack_0.png";
  public static final String AMANDA_ATTACK_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda/Amanda_Attack/Amanda_Attack_90.png";
  public static final String AMANDA_ATTACK_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda/Amanda_Attack/Amanda_Attack_180.png";
  public static final String AMANDA_ATTACK_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Amanda/Amanda_Attack/Amanda_Attack_270.png";

  public static final String AMANDA_IDLE =
          "dracula_punch/Resources/Sprite_Sheets/Amanda/Amanda_Idle/Amanda_Idle.png";

  public static final String AMANDA_CHAR_SELECT =
          "dracula_punch/Resources/Character_Select/Amanda.png";
  //endregion

  //region Austin
  public static final String AUSTIN_RUN_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin/Austin_Run/Austin_Run_0.png";
  public static final String AUSTIN_RUN_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin/Austin_Run/Austin_Run_90.png";
  public static final String AUSTIN_RUN_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin/Austin_Run/Austin_Run_180.png";
  public static final String AUSTIN_RUN_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin/Austin_Run/Austin_Run_270.png";

  public static final String AUSTIN_ATTACK_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin/Austin_Attack/Austin_Attack_0.png";
  public static final String AUSTIN_ATTACK_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin/Austin_Attack/Austin_Attack_90.png";
  public static final String AUSTIN_ATTACK_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin/Austin_Attack/Austin_Attack_180.png";
  public static final String AUSTIN_ATTACK_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Austin/Austin_Attack/Austin_Attack_270.png";

  public static final String AUSTIN_IDLE =
          "dracula_punch/Resources/Sprite_Sheets/Austin/Austin_Idle/Austin_Idle.png";

  public static final String AUSTIN_CHAR_SELECT =
          "dracula_punch/Resources/Character_Select/Austin.png";
  //endregion

  //region Ritta
  public static final String RITTA_RUN_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta/Ritta_Run/Ritta_Run_0.png";
  public static final String RITTA_RUN_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta/Ritta_Run/Ritta_Run_90.png";
  public static final String RITTA_RUN_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta/Ritta_Run/Ritta_Run_180.png";
  public static final String RITTA_RUN_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta/Ritta_Run/Ritta_Run_270.png";

  public static final String RITTA_ATTACK_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta/Ritta_Attack/Ritta_Attack_0.png";
  public static final String RITTA_ATTACK_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta/Ritta_Attack/Ritta_Attack_90.png";
  public static final String RITTA_ATTACK_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta/Ritta_Attack/Ritta_Attack_180.png";
  public static final String RITTA_ATTACK_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ritta/Ritta_Attack/Ritta_Attack_270.png";

  public static final String RITTA_IDLE =
          "dracula_punch/Resources/Sprite_Sheets/Ritta/Ritta_Idle/Ritta_Idle.png";

  public static final String RITTA_CHAR_SELECT =
          "dracula_punch/Resources/Character_Select/Ritta.png";
  //endregion

  //region Dracula
  public static final String DRACULA_WALK_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Dracula/Dracula_Walk/Dracula_Walk_0.png";
  public static final String DRACULA_WALK_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Dracula/Dracula_Walk/Dracula_Walk_90.png";
  public static final String DRACULA_WALK_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Dracula/Dracula_Walk/Dracula_Walk_180.png";
  public static final String DRACULA_WALK_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Dracula/Dracula_Walk/Dracula_Walk_270.png";

  public static final String DRACULA_MELEE_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Dracula/Dracula_Melee/Dracula_Melee_0.png";
  public static final String DRACULA_MELEE_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Dracula/Dracula_Melee/Dracula_Melee_90.png";
  public static final String DRACULA_MELEE_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Dracula/Dracula_Melee/Dracula_Melee_180.png";
  public static final String DRACULA_MELEE_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Dracula/Dracula_Melee/Dracula_Melee_270.png";

  public static final String DRACULA_IDLE =
          "dracula_punch/Resources/Sprite_Sheets/Dracula/Dracula_Idle/Dracula_Idle.png";
  //endregion

  public DraculaPunchGame(String name, int width, int height) {
    super(name);
    SCREEN_WIDTH = width;
    SCREEN_HEIGHT = height;

    Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
  }

  @Override
  public void initStatesList(GameContainer gameContainer) throws SlickException {
    addState(new CharacterSelectState());
    addState(new TestLevelState());

    //region Amanda
    ResourceManager.loadImage(AMANDA_IDLE);
    ResourceManager.loadImage(AMANDA_CHAR_SELECT);
    ResourceManager.loadImage(AMANDA_RUN_0_DEG);
    ResourceManager.loadImage(AMANDA_RUN_90_DEG);
    ResourceManager.loadImage(AMANDA_RUN_180_DEG);
    ResourceManager.loadImage(AMANDA_RUN_270_DEG);
    ResourceManager.loadImage(AMANDA_ATTACK_0_DEG);
    ResourceManager.loadImage(AMANDA_ATTACK_90_DEG);
    ResourceManager.loadImage(AMANDA_ATTACK_180_DEG);
    ResourceManager.loadImage(AMANDA_ATTACK_270_DEG);
    //endregion

    //region Austin
    ResourceManager.loadImage(AUSTIN_IDLE);
    ResourceManager.loadImage(AUSTIN_CHAR_SELECT);
    ResourceManager.loadImage(AUSTIN_RUN_0_DEG);
    ResourceManager.loadImage(AUSTIN_RUN_90_DEG);
    ResourceManager.loadImage(AUSTIN_RUN_180_DEG);
    ResourceManager.loadImage(AUSTIN_RUN_270_DEG);
    ResourceManager.loadImage(AUSTIN_ATTACK_0_DEG);
    ResourceManager.loadImage(AUSTIN_ATTACK_90_DEG);
    ResourceManager.loadImage(AUSTIN_ATTACK_180_DEG);
    ResourceManager.loadImage(AUSTIN_ATTACK_270_DEG);
    //endregion

    //region Ritta
    ResourceManager.loadImage(RITTA_IDLE);
    ResourceManager.loadImage(RITTA_CHAR_SELECT);
    ResourceManager.loadImage(RITTA_RUN_0_DEG);
    ResourceManager.loadImage(RITTA_RUN_90_DEG);
    ResourceManager.loadImage(RITTA_RUN_180_DEG);
    ResourceManager.loadImage(RITTA_RUN_270_DEG);
    ResourceManager.loadImage(RITTA_ATTACK_0_DEG);
    ResourceManager.loadImage(RITTA_ATTACK_90_DEG);
    ResourceManager.loadImage(RITTA_ATTACK_180_DEG);
    ResourceManager.loadImage(RITTA_ATTACK_270_DEG);
    //endregion

    //region Dracula
    ResourceManager.loadImage(DRACULA_IDLE);
    ResourceManager.loadImage(DRACULA_WALK_0_DEG);
    ResourceManager.loadImage(DRACULA_WALK_90_DEG);
    ResourceManager.loadImage(DRACULA_WALK_180_DEG);
    ResourceManager.loadImage(DRACULA_WALK_270_DEG);
    ResourceManager.loadImage(DRACULA_MELEE_0_DEG);
    ResourceManager.loadImage(DRACULA_MELEE_90_DEG);
    ResourceManager.loadImage(DRACULA_WALK_180_DEG);
    ResourceManager.loadImage(DRACULA_MELEE_270_DEG);
    //endregion
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

