package dracula_punch.Characters.Enemies;

import dracula_punch.States.LevelState;

public class BatController extends EnemyController{
    public BatController(float x, float y, LevelState curLevelState) {
        super(x, y, curLevelState);
    }

    @Override
    public String getRunSheet(int x, int y) {
        return null;
    }

    @Override
    public String getIdleSheet() {
        return null;
    }

    @Override
    public String getMeleeSheet() {
        return null;
    }

    @Override
    public String getRangedSheet() {
        return null;
    }
}
