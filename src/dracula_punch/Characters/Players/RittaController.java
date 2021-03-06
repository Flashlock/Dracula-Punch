package dracula_punch.Characters.Players;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.Projectiles.Arrow;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class RittaController extends PlayerController{
  private final int rangedActionFrame;

  public RittaController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 30;
    scaleFactor = .95f;
    rangedActionFrame = 0;

    TOTAL_MOVE_TIME = 200;

    maxHealth = 3;
    currentHealth = maxHealth;
    healthBars = new Image[]{
            ResourceManager.getImage(DraculaPunchGame.RITTA_HEALTH_3),
            ResourceManager.getImage(DraculaPunchGame.RITTA_HEALTH_2),
            ResourceManager.getImage(DraculaPunchGame.RITTA_HEALTH_1)
    };
    setHealthBar();

    attackAction = new AttackAction(this, rangedActionFrame, AttackType.RANGED);

    setScale(scaleFactor);
  }

//  int count = 0;
//  @Override
//  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
//    super.update(gameContainer, stateBasedGame, delta);
//    count += delta;
//    if(count > 3000) {
//      System.out.println(currentTile.x + ", " + currentTile.y);
//      count = 0;
//    }
//  }

  @Override
  public void takeDamage(int damage){
    super.takeDamage(damage);
    ResourceManager.getSound(DraculaPunchGame.RITTA_OW_SND).play();
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
    ResourceManager.getSound(DraculaPunchGame.RITTA_ATTACK_SND).play();
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
