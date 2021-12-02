package dracula_punch.Damage_System.Projectiles;

import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.GameObject;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.logging.Level;

public abstract class Projectile extends GameObject {
    protected float moveSpeed;
    protected final LevelState curLevelState;

    protected final Vector direction;
    protected Coordinate curTile;
    protected Coordinate previousTile = new Coordinate();
    public Coordinate currentTilePlusPartial = new Coordinate();
    protected float TOTAL_MOVE_TIME = 100;
    protected float movingTime = 99; // one less than total to trigger calculation once on startup
    protected float percentMoveDone;
    private Coordinate previousTargetTile = new Coordinate(0,0);

    private Vector isoDirection;

    public Projectile(float x, float y, Coordinate curTile, LevelState curLevelState, Vector direction) {
        super(x, y);
        this.curTile = curTile;
        this.direction = direction;
        this.curLevelState = curLevelState;
        TOTAL_MOVE_TIME = 200;

        // first rotate the direction vector clockwise by 45 deg or pi/4 rad
        float cos = (float) Math.cos(7 * Math.PI / 4);
        float sin = (float) Math.sin(7 * Math.PI / 4);
        float dirX = cos * direction.getX() - sin * direction.getY();
        float dirY = cos * direction.getX() + sin * direction.getY();
        isoDirection = new Vector(dirX, dirY);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        translate(isoDirection.scale(delta * moveSpeed));
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        render(graphics);
    }

    /**
     * Animate the projectile with the given sheet
     * @param sheet The Sprite Sheet to animate from
     */
    protected void animate(String sheet){
        Animation animation = new Animation(
                ResourceManager.getSpriteSheet(
                        sheet,
                        DraculaPunchGame.SPRITE_SIZE,
                        DraculaPunchGame.SPRITE_SIZE
                ),
                DraculaPunchGame.ANIMATION_DURATION
        );
        animation.setLooping(true);
        addAnimation(animation);
    }

    private void move(int delta) {
        if (movingTime < TOTAL_MOVE_TIME){ movingTime += delta; }
        else if (!currentTile.isEqual(previousTargetTile)){
            movingTime = 0;
            previousTile.x = currentTile.x;
            previousTile.y = currentTile.y;
//            currentTile.x = next.x;
//            currentTile.y = next.y;
        }
    }

    private void updateAnimation() {
        float percentMoveDone = (TOTAL_MOVE_TIME - movingTime) / TOTAL_MOVE_TIME;
        float partialX = 0, partialY = 0;
        if (previousTile.y > currentTile.y){ partialY = percentMoveDone;}
        else if (previousTile.y < currentTile.y){ partialY = -percentMoveDone;}
        if (previousTile.x > currentTile.x){ partialX = percentMoveDone;}
        else if (previousTile.x < currentTile.x){ partialX = -percentMoveDone;}
        currentTilePlusPartial = new Coordinate(currentTile);
        currentTilePlusPartial.add(partialX, partialY);
    }
}
