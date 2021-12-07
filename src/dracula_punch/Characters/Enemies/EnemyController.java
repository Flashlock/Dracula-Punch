package dracula_punch.Characters.Enemies;

import dracula_punch.Characters.CharacterController;
import dracula_punch.States.LevelState;

public abstract class EnemyController extends CharacterController {
    public EnemyController(float x, float y, LevelState curLevelState) {
        super(x, y, curLevelState);
    }
}
