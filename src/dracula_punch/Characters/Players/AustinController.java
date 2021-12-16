package dracula_punch.Characters.Players;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.GameObject;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;

import java.util.ArrayList;

public class AustinController extends PlayerController {
  private int meleeDamage;
  private final int meleeActionFrame;

  public AustinController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 35;
    scaleFactor = 1f;
    meleeActionFrame = 10;

    meleeDamage = 5;
    attackAction = new AttackAction(this, meleeActionFrame, AttackType.MELEE);

    setScale(scaleFactor);
  }

  //region Character Controller
  @Override
  public String getRunSheet(int x, int y) {
    return DraculaPunchGame.getSheetHelper(
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
    return DraculaPunchGame.getSheetHelper(
            DraculaPunchGame.AUSTIN_ATTACK_0_DEG,
            DraculaPunchGame.AUSTIN_ATTACK_180_DEG,
            DraculaPunchGame.AUSTIN_ATTACK_90_DEG,
            DraculaPunchGame.AUSTIN_ATTACK_270_DEG,
            (int) facingDir.getX(),
            (int) facingDir.getY()
    );
  }

  @Override
  public String getRangedSheet() {
    return null;
  }
  //endregion

  //region IAttacker
  public void attack(AttackType attackType){
    switch (attackType){
      case MELEE:
        Coordinate front = getLinedTiles(1, facingDir).getFirst();

        // damage all the things
        ArrayList<GameObject> targets = curLevelState.getObjectsFromTile(front);
        for(GameObject target : targets){
          if(target instanceof IDamageable && !(target instanceof PlayerController)){
            ((IDamageable) target).takeDamage(meleeDamage);
          }
        }
        break;
      case RANGED:
        System.out.println("No Ranged Attack");
        break;
      default:
        System.out.println("Unknown Attack Type: " + attackType);
    }
  }
  //endregion
}