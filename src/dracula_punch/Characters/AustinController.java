package dracula_punch.Characters;

import dracula_punch.Actions.Damage_System.FinishMeleeAction;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.Damage_System.IMelee;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AustinController extends PlayerController implements IMelee {
  public static final int RUN_HEIGHT = 628;
  public static final int RUN_WIDTH = 360;
  public static final int IDLE_HEIGHT = 620;
  public static final int IDLE_WIDTH = 544;
  public static final int ATTACK_WIDTH = 550;
  public static final int ATTACK_HEIGHT = 550;

  private int meleeDamage;

  public AustinController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 35;
    scaleFactor = 1f;  // changed scaling to new tiledmap!

    meleeDamage = 5;
    finishMeleeAction = new FinishMeleeAction(this);

    setScale(scaleFactor);
  }

  @Override
  public String getRunSheet(int x, int y) {
    return getSheetHelper(
            DraculaPunchGame.AUSTIN_RUN_0_DEG,
            DraculaPunchGame.AUSTIN_RUN_180_DEG,
            DraculaPunchGame.AUSTIN_RUN_90_DEG,
            DraculaPunchGame.AUSTIN_RUN_270_DEG,
            x,
            y
    );
  }

  @Override
  public int getRunWidth() {
    return RUN_WIDTH;
  }

  @Override
  public int getRunHeight() {
    return RUN_HEIGHT;
  }

  @Override
  public String getIdleSheet() {
    return DraculaPunchGame.AUSTIN_IDLE;
  }

  @Override
  public int getIdleWidth() {
    return IDLE_WIDTH;
  }

  @Override
  public int getIdleHeight() {
    return IDLE_HEIGHT;
  }

  @Override
  public String getMeleeSheet() {
    return getSheetHelper(
            DraculaPunchGame.AUSTIN_ATTACK_0_DEG,
            DraculaPunchGame.AUSTIN_ATTACK_180_DEG,
            DraculaPunchGame.AUSTIN_ATTACK_90_DEG,
            DraculaPunchGame.AUSTIN_ATTACK_270_DEG
    );
  }

  @Override
  public int getMeleeWidth() {
    return ATTACK_WIDTH;
  }

  @Override
  public int getMeleeHeight() {
    return ATTACK_HEIGHT;
  }

  @Override
  public String getRangedSheet() {
    return null;
  }

  @Override
  public int getRangedWidth() {
    return 0;
  }

  @Override
  public int getRangedHeight() {
    return 0;
  }

  @Override
  public int getMeleeDamage() {
    return meleeDamage;
  }

  @Override
  public ArrayList<IDamageable> getTargetObjects() {
    // get the tile in front of me
    int x = (int) (currentTile.x + facingDir.getX());
    int y = (int) (currentTile.y + facingDir.getY());

    // filter through all the objects for damageable ones
    ArrayList<GameObject> gameObjects = curLevelState.getObjectsFromTile(x, y);
    ArrayList<IDamageable> damageables = new ArrayList<>();
    for(GameObject gameObject : gameObjects){
      if(gameObject instanceof IDamageable){
        damageables.add((IDamageable) gameObject);
      }
    }

    return damageables;
  }
}
