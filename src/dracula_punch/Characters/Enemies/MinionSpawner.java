package dracula_punch.Characters.Enemies;

import dracula_punch.States.LevelState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class MinionSpawner extends SwarmManager {
    private LevelState curLevelState;

    /**
     * There's no image, so use x and y for coordinates
     *
     * @param x                X coordinate it is placed at
     * @param y                Y coordinate it is placed at
     * @param engagementRadius
     * @param curLevelState
     */
    public MinionSpawner(float x, float y, float engagementRadius, LevelState curLevelState) {
        super(x, y, engagementRadius, curLevelState);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {

    }
}
