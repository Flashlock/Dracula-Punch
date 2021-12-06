package dracula_punch.Damage_System.Projectiles;

import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.GameObject;
import dracula_punch.Characters.PlayerController;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.Vector;

import java.util.ArrayList;

public class MagicBall extends Projectile{
  private final int damage = 5;

  public MagicBall(float x, float y, Coordinate curTile, LevelState curLevelState, Vector direction) {
    super(x, y, curTile, curLevelState, direction);
    moveSpeed = .1f;
    TOTAL_MOVE_TIME = 100 * moveSpeed;

    String sheet = DraculaPunchGame.getSheetHelper(
        DraculaPunchGame.MAGIC_BALL_0_DEG,
        DraculaPunchGame.MAGIC_BALL_180_DEG,
        DraculaPunchGame.MAGIC_BALL_90_DEG,
        DraculaPunchGame.MAGIC_BALL_270_DEG,
        (int) direction.getX(),
        (int) direction.getY()
    );
    if(sheet == null) return;
    animate(sheet);
  }

  @Override
  protected void collide(ArrayList<GameObject> collisions) {
    boolean dealtDamage = false;
    for(GameObject gameObject : collisions){
      if(gameObject instanceof IDamageable && !(gameObject instanceof PlayerController)){
        dealtDamage = true;
        ((IDamageable) gameObject).takeDamage(damage);
      }
    }

    if(dealtDamage){
      curLevelState.deadObjects.add(this);
    }
  }
}
