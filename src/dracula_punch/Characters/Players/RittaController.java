package dracula_punch.Characters.Players;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.Projectiles.Arrow;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Image;

public class RittaController extends PlayerController{
  private final int rangedActionFrame;

  private final Image fullHealthImg = ResourceManager.getImage(DraculaPunchGame.RITTA_HEALTH_1);
  private final Image twoThirdHealthImg = ResourceManager.getImage(DraculaPunchGame.RITTA_HEALTH_2);
  private final Image oneThirdHealthImg = ResourceManager.getImage(DraculaPunchGame.RITTA_HEALTH_3);

  private Image[] Health = new Image[]{
          oneThirdHealthImg,
          twoThirdHealthImg,
          fullHealthImg
  };

  public RittaController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 30;
    scaleFactor = .95f;
    rangedActionFrame = 0;

    maxHealth = 3;
    currentHealth = maxHealth;
    addImage(fullHealthImg);

    attackAction = new AttackAction(this, rangedActionFrame, AttackType.RANGED);

    setScale(scaleFactor);
  }

  @Override
  public Image getHealthBar() {
    return Health[currentHealth-1];
  }

  //region Character Controller
  @Override
  public String getRunSheet(int x, int y) {
    return DraculaPunchGame.getSheetHelper(
            DraculaPunchGame.RITTA_RUN_0_DEG,
            DraculaPunchGame.RITTA_RUN_180_DEG,
            DraculaPunchGame.RITTA_RUN_90_DEG,
            DraculaPunchGame.RITTA_RUN_270_DEG,
            x,
            y
    );
  }

  @Override
  public String getIdleSheet() {
    return DraculaPunchGame.RITTA_IDLE;
  }

  public String getMeleeSheet() {
    return null;
  }

  @Override
  public String getRangedSheet() {
    return DraculaPunchGame.getSheetHelper(
            DraculaPunchGame.RITTA_ATTACK_0_DEG,
            DraculaPunchGame.RITTA_ATTACK_180_DEG,
            DraculaPunchGame.RITTA_ATTACK_90_DEG,
            DraculaPunchGame.RITTA_ATTACK_270_DEG,
            (int) facingDir.getX(),
            (int) facingDir.getY()
    );
  }
  //endregion

  //region IAttacker
  @Override
  public void attack(AttackType attackType) {
    // get the tile in front of me
    int x = (int) (currentTile.x + facingDir.getX());
    int y = (int) (currentTile.y - facingDir.getY());
    Coordinate spawn = new Coordinate(x, y);
    Vector screen = curLevelState.camera.getScreenPositionFromTile(spawn);
    curLevelState.newObjects.add(
            new Arrow(screen.getX(), screen.getY(), spawn, curLevelState, facingDir)
    );  }
  //endregion
}
