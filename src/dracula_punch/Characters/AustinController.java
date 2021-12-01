package dracula_punch.Characters;

import dracula_punch.Actions.Damage_System.MeleeAction;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.Damage_System.IAttacker;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;

import java.util.ArrayList;

public class AustinController extends PlayerController implements IAttacker {
  private int meleeDamage;
  private final int meleeActionFrame;

  public AustinController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 35;
    scaleFactor = 1f;  // changed scaling to new tiledmap!
    meleeActionFrame = 10;

    meleeDamage = 5;
    attackAction = new MeleeAction(this, meleeActionFrame);

    setScale(scaleFactor);
  }

  //region Character Controller
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
  public String getIdleSheet() {
    return DraculaPunchGame.AUSTIN_IDLE;
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
  public String getRangedSheet() {
    return null;
  }
  //endregion

  //region IAttacker
  @Override
  public int getMeleeDamage() {
    return meleeDamage;
  }

  @Override
  public int getMeleeDamageFrame() {
    return meleeActionFrame;
  }

  @Override
  public int getRangedDamage() {
    return 0;
  }

  @Override
  public int getRangedFireFrame() {
    return 0;
  }

  @Override
  public ArrayList<IDamageable> getTargetObjects() {
    // get the tile in front of me
    int x = (int) (currentTile.x + facingDir.getX());
    int y = (int) (currentTile.y - facingDir.getY());

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
  //endregion
}
