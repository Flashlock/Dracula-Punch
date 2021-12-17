package dracula_punch.Characters.Enemies;

import dracula_punch.Camera.Coordinate;
import dracula_punch.States.LevelState;

public class DraculaBat extends BatController{
    private final DraculaController dracula;

    public DraculaBat(Coordinate startingTile, LevelState curLevelState, SwarmManager swarmManager, DraculaController dracula) {
        super(startingTile, curLevelState, swarmManager);
        this.dracula = dracula;
        batState = BatState.ATTACK;
        refreshTargetClock = refreshTargetTime + 1;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if(currentHealth <= 0){
            dracula.bats.remove(this);
        }
    }
}
