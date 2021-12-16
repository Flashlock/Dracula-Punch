package dracula_punch.Damage_System.Projectiles;

import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.Enemies.EnemyController;
import dracula_punch.Characters.GameObject;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.Vector;

import java.util.ArrayList;

public class Laser extends Projectile{
    private final int damage = 5;

    public Laser(float x, float y, Coordinate curTile, LevelState curLevelState, Vector direction) {
        super(x, y, curTile, curLevelState, direction);
        moveSpeed = .8f;
        TOTAL_MOVE_TIME = 100 * moveSpeed;

        String sheet = DraculaPunchGame.getSheetHelper(
                DraculaPunchGame.LASER_0_DEG,
                DraculaPunchGame.LASER_180_DEG,
                DraculaPunchGame.LASER_90_DEG,
                DraculaPunchGame.LASER_270_DEG,
                (int) direction.getX(),
                (int) direction.getY()
        );
        if(sheet == null) return;
        animate(sheet);
    }

    @Override
    protected void collide(ArrayList<GameObject> collisions) {
        for(GameObject gameObject : collisions){
            if(gameObject instanceof IDamageable && !(gameObject instanceof EnemyController)){
                ((IDamageable) gameObject).takeDamage(damage);
                curLevelState.deadObjects.add(this);
                return;
            }
        }
    }
}
