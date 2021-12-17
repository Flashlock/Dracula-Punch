package dracula_punch.Characters.Enemies;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.Projectiles.Laser;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class GargoyleController extends EnemyController{
    public enum GargoyleState { IDLE, ACTIVE }
    private GargoyleState gargoyleState;
    public GargoyleState getGargoyleState(){ return gargoyleState; }

    // how far from current tile to choose next tile
    private final int targetRadius = 8;

    public GargoyleController(Coordinate startingTile, LevelState curLevelState, SwarmManager swarmManager) {
        super(0, 0, startingTile, curLevelState, swarmManager);
        attackAction = new AttackAction(this, 10, AttackType.RANGED);
        refreshTargetTime = 8000;
        gargoyleState = GargoyleState.IDLE;

        TOTAL_MOVE_TIME = 300;

        maxHealth = 2;
        currentHealth = maxHealth;
        healthBars = new Image[]{
                ResourceManager.getImage(DraculaPunchGame.GARGOYLE_HEALTH_2),
                ResourceManager.getImage(DraculaPunchGame.GARGOYLE_HEALTH_1)
        };
        setHealthBar();
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        super.update(gameContainer, stateBasedGame, delta);
        if(getAnimLock()) return;

        if(gargoyleState == GargoyleState.ACTIVE) {
            Vector direction = isPlayerAroundMe(10);
            if (direction != null) {
                navTarget = null;
                animateMove(direction);
                animateAttack(getRangedSheet());
                return;
            }

            refreshTargetClock += delta;
            if (refreshTargetClock > refreshTargetTime) {
                refreshTarget();
                refreshTargetClock = 0;
            }
        }
    }

    @Override
    public void activate() {
        gargoyleState = GargoyleState.ACTIVE;
    }

    @Override
    public void deactivate() {
        ResourceManager.getSound(DraculaPunchGame.GARGOYLE_SND).stop();
        navPath = navGraph.findPath(currentTile, startingTile);
        navTarget = navPath.get(0);
        gargoyleState = GargoyleState.IDLE;
    }

    protected void refreshTarget() {
        int width = navGraph.getGraphWidth();
        int height = navGraph.getGraphHeight();

        int startX = (int) (currentTile.x - targetRadius);
        if(startX < 0)
            startX = 0;
        int endX = (int) (currentTile.x + targetRadius);
        if(endX > width)
            endX = width;

        int startY = (int) (currentTile.y - targetRadius);
        if(startY < 0)
            startY = 0;
        int endY = (int) (currentTile.y + targetRadius);
        if(endY > height)
            endY = height;

        ArrayList<Coordinate> tiles = new ArrayList<>(targetRadius * targetRadius);
        for(int i = startX; i < endX; i++){
            for(int j = startY; j < endY; j++){
                if(curLevelState.map.isPassable[i][j] && i != currentTile.x && j != currentTile.y){
                    tiles.add(new Coordinate(i, j));
                }
            }
        }

        int randIndex = (int) (Math.random() * tiles.size());
        navPath = navGraph.findPath(currentTile, tiles.get(randIndex));
        if(navPath == null) return;
        navTarget = navPath.remove(0);
    }

    //region Character Controller
    @Override
    public String getRunSheet(int x, int y) {
        return DraculaPunchGame.getSheetHelper(
                DraculaPunchGame.GARGOYLE_WALK_0_DEG,
                DraculaPunchGame.GARGOYLE_WALK_180_DEG,
                DraculaPunchGame.GARGOYLE_WALK_90_DEG,
                DraculaPunchGame.GARGOYLE_WALK_270_DEG,
                x,
                y
        );
    }

    @Override
    public String getIdleSheet() {
        return DraculaPunchGame.GARGOYLE_IDLE;
    }

    @Override
    public String getMeleeSheet() {
        return null;
    }

    @Override
    public String getRangedSheet() {
        return DraculaPunchGame.getSheetHelper(
                DraculaPunchGame.GARGOYLE_ATTACK_0_DEG,
                DraculaPunchGame.GARGOYLE_ATTACK_180_DEG,
                DraculaPunchGame.GARGOYLE_ATTACK_90_DEG,
                DraculaPunchGame.GARGOYLE_ATTACK_270_DEG,
                (int) facingDir.getX(),
                (int) facingDir.getY()
        );
    }
    //endregion

    //region Enemy Controller
    @Override
    public void postAttackAction() {
        refreshTarget();
    }
    //endregion

    //region IAttacker
    @Override
    public void attack(AttackType attackType) {
        if(attackType == AttackType.MELEE){
            System.out.println("No Melee Attack");
            return;
        }
        Vector screen = curLevelState.camera.getScreenPositionFromTile(currentTile);
        curLevelState.newObjects.add(
                new Laser(screen.getX(), screen.getY(), currentTile, curLevelState, facingDir)
        );
    }
    //endregion
}
