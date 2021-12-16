package dracula_punch;

import dracula_punch.States.*;
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
  public static int START_STATE = 1;
  public static int LOSE_STATE = 2;
  public static int WIN_STATE = 3;
  //endregion

  public static final String MAP = "dracula_punch/Resources/Tiled/dungeon_map.tmx";
  public static final int ANIMATION_DURATION = 50;
  public static final int SPRITE_SIZE = 500;
  public static int SCREEN_WIDTH, SCREEN_HEIGHT;
  public enum charIdEnum { UNCHOSEN, AMANDA, AUSTIN, RITTA }
  public static charIdEnum[] characterChoice = {charIdEnum.UNCHOSEN, charIdEnum.UNCHOSEN, charIdEnum.UNCHOSEN};
  public static int[] inputSource = {-1, -1, -1};

  //region control constants
  public static final int PS5_CONTROLLER_START_BUTTON = 13;
  public static final int PS5_CONTROLLER_X_BUTTON = 5;
  public static final int PS5_CONTROLLER_UP_BUTTON = 2;
  public static final int PS5_CONTROLLER_DOWN_BUTTON = 3;
  public static final int PS5_CONTROLLER_LEFT_BUTTON = 0;
  public static final int PS5_CONTROLLER_RIGHT_BUTTON = 1;

  public static final int KB_WASD = -2;
  public static final int KB_IJKL = -3;
  public static final int KB_ARROWS = -4;
  //endregion

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

  //region Bat
  public static final String BAT_FLY_0 =
          "dracula_punch/Resources/Sprite_Sheets/Bat/Bat_Fly/Bat_Fly_0.png";
  public static final String BAT_FLY_90 =
          "dracula_punch/Resources/Sprite_Sheets/Bat/Bat_Fly/Bat_Fly_90.png";
  public static final String BAT_FLY_180 =
          "dracula_punch/Resources/Sprite_Sheets/Bat/Bat_Fly/Bat_Fly_180.png";
  public static final String BAT_FLY_270 =
          "dracula_punch/Resources/Sprite_Sheets/Bat/Bat_Fly/Bat_Fly_270.png";

  public static final String BAT_ATTACK_0 =
          "dracula_punch/Resources/Sprite_Sheets/Bat/Bat_Attack/Bat_Attack_0.png";
  public static final String BAT_ATTACK_90 =
          "dracula_punch/Resources/Sprite_Sheets/Bat/Bat_Attack/Bat_Attack_90.png";
  public static final String BAT_ATTACK_180 =
          "dracula_punch/Resources/Sprite_Sheets/Bat/Bat_Attack/Bat_Attack_180.png";
  public static final String BAT_ATTACK_270 =
          "dracula_punch/Resources/Sprite_Sheets/Bat/Bat_Attack/Bat_Attack_270.png";

  public static final String BAT_IDLE =
          "dracula_punch/Resources/Sprite_Sheets/Bat/Bat_Idle/Bat_Idle.png";
  //endregion

  //region Gargoyle
  public static final String GARGOYLE_IDLE =
          "dracula_punch/Resources/Sprite_Sheets/Gargoyle/Gargoyle_Idle/Gargoyle_Idle.png";

  public static final String GARGOYLE_WALK_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Gargoyle/Gargoyle_Walk/Gargoyle_Walk_0.png";
  public static final String GARGOYLE_WALK_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Gargoyle/Gargoyle_Walk/Gargoyle_Walk_90.png";
  public static final String GARGOYLE_WALK_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Gargoyle/Gargoyle_Walk/Gargoyle_Walk_180.png";
  public static final String GARGOYLE_WALK_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Gargoyle/Gargoyle_Walk/Gargoyle_Walk_270.png";

  public static final String GARGOYLE_ATTACK_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Gargoyle/Gargoyle_Attack/Gargoyle_Attack_0.png";
  public static final String GARGOYLE_ATTACK_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Gargoyle/Gargoyle_Attack/Gargoyle_Attack_90.png";
  public static final String GARGOYLE_ATTACK_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Gargoyle/Gargoyle_Attack/Gargoyle_Attack_180.png";
  public static final String GARGOYLE_ATTACK_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Gargoyle/Gargoyle_Attack/Gargoyle_Attack_270.png";
  //endregion

  //region Projectiles
  public static final String MAGIC_BALL_0_DEG =
      "dracula_punch/Resources/Sprite_Sheets/Ammo/Magic_Ball/Ball_0.png";
  public static final String MAGIC_BALL_90_DEG =
      "dracula_punch/Resources/Sprite_Sheets/Ammo/Magic_Ball/Ball_90.png";
  public static final String MAGIC_BALL_180_DEG =
      "dracula_punch/Resources/Sprite_Sheets/Ammo/Magic_Ball/Ball_180.png";
  public static final String MAGIC_BALL_270_DEG =
      "dracula_punch/Resources/Sprite_Sheets/Ammo/Magic_Ball/Ball_270.png";

  public static final String ARROW_0_DEG =
      "dracula_punch/Resources/Sprite_Sheets/Ammo/Arrow/Arrow_0.png";
  public static final String ARROW_90_DEG =
      "dracula_punch/Resources/Sprite_Sheets/Ammo/Arrow/Arrow_90.png";
  public static final String ARROW_180_DEG =
      "dracula_punch/Resources/Sprite_Sheets/Ammo/Arrow/Arrow_180.png";
  public static final String ARROW_270_DEG =
      "dracula_punch/Resources/Sprite_Sheets/Ammo/Arrow/Arrow_270.png";

  public static final String LASER_0_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ammo/Laser/Laser_0.png";
  public static final String LASER_90_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ammo/Laser/Laser_90.png";
  public static final String LASER_180_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ammo/Laser/Laser_180.png";
  public static final String LASER_270_DEG =
          "dracula_punch/Resources/Sprite_Sheets/Ammo/Laser/Laser_270.png";
  //endregion

  //region Spash Screens
  public static final String START_SCREEN =
          "dracula_punch/Resources/Splash_Screens/Start_Screen.png";

  public static final String LOSE_SCREEN =
          "dracula_punch/Resources/Splash_Screens/Lose_Screen.png";

  public static final String WIN_SCREEN =
          "dracula_punch/Resources/Splash_Screens/Win_Screen.png";

  //endregion


  public DraculaPunchGame(String name, int width, int height) {
    super(name);
    SCREEN_WIDTH = width;
    SCREEN_HEIGHT = height;

    Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
  }

  @Override
  public void initStatesList(GameContainer gameContainer) throws SlickException {
    addState(new StartState());
    addState(new CharacterSelectState());
    addState(new TestLevelState());
    addState(new WinState());
    addState(new LoseState());

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
    ResourceManager.loadImage(DRACULA_MELEE_180_DEG);
    ResourceManager.loadImage(DRACULA_MELEE_270_DEG);
    //endregion

    //region Bat
    ResourceManager.loadImage(BAT_IDLE);
    ResourceManager.loadImage(BAT_ATTACK_0);
    ResourceManager.loadImage(BAT_ATTACK_90);
    ResourceManager.loadImage(BAT_ATTACK_180);
    ResourceManager.loadImage(BAT_ATTACK_270);
    ResourceManager.loadImage(BAT_FLY_0);
    ResourceManager.loadImage(BAT_FLY_90);
    ResourceManager.loadImage(BAT_FLY_180);
    ResourceManager.loadImage(BAT_FLY_270);
    //endregion

    //region Gargoyle
    ResourceManager.loadImage(GARGOYLE_IDLE);
    ResourceManager.loadImage(GARGOYLE_WALK_0_DEG);
    ResourceManager.loadImage(GARGOYLE_WALK_90_DEG);
    ResourceManager.loadImage(GARGOYLE_WALK_180_DEG);
    ResourceManager.loadImage(GARGOYLE_WALK_270_DEG);
    ResourceManager.loadImage(GARGOYLE_ATTACK_0_DEG);
    ResourceManager.loadImage(GARGOYLE_ATTACK_90_DEG);
    ResourceManager.loadImage(GARGOYLE_ATTACK_180_DEG);
    ResourceManager.loadImage(GARGOYLE_ATTACK_270_DEG);
    //endregion

    //region Projectiles
    ResourceManager.loadImage(MAGIC_BALL_0_DEG);
    ResourceManager.loadImage(MAGIC_BALL_90_DEG);
    ResourceManager.loadImage(MAGIC_BALL_180_DEG);
    ResourceManager.loadImage(MAGIC_BALL_270_DEG);

    ResourceManager.loadImage(ARROW_0_DEG);
    ResourceManager.loadImage(ARROW_90_DEG);
    ResourceManager.loadImage(ARROW_180_DEG);
    ResourceManager.loadImage(ARROW_270_DEG);

    ResourceManager.loadImage(LASER_0_DEG);
    ResourceManager.loadImage(LASER_90_DEG);
    ResourceManager.loadImage(LASER_180_DEG);
    ResourceManager.loadImage(LASER_270_DEG);
    //endregion

    //region Splash Screens
    ResourceManager.loadImage(START_SCREEN);
    ResourceManager.loadImage(LOSE_SCREEN);
    ResourceManager.loadImage(WIN_SCREEN);
    //endregion
  }

  /**
   * Determine sprite sheet for new facing direction
   * @param x The x direction to face
   * @param y The y direction to face
   * @return The given sheet correlating to the facing direction
   */
  public static String getSheetHelper(String up, String down, String left, String right, int x, int y){
    String sheet = null;
    if(x == 1 && y == 0){
      // right
      sheet = right;
    }
    else if(x == -1 && y == 0){
      // left
      sheet = left;
    }
    else if(x == 0 && y == 1){
      // up
      sheet = up;
    }
    else if(x == 0 && y == -1){
      // down
      sheet = down;
    }
    else if(x == 0 && y == 0){
      // stop - do nothing for now. No idle pose/anim
    }
    else{
      System.out.println("Invalid Direction: Unable to Animate " + x + ", " + y);
    }
    return sheet;
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

