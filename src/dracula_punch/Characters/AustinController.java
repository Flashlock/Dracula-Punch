package dracula_punch.Characters;

import dracula_punch.Actions.Input.InputMoveAction;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class AustinController extends CharacterController{
    public static final int RUN_HEIGHT = 772;
    public static final int RUN_WIDTH = 540;

    public AustinController(float x, float y, LevelState curLevelState) {
        super(x, y, curLevelState);
        xRenderOffset = 0;
        yRenderOffset = 35;
        scaleFactor = .2f;

        setScale(scaleFactor);

        // Add a movement action - for animation switching
        curLevelState.inputMoveEvent.add(new InputMoveAction(this));
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {}

    @Override
    public String getSheet(int x, int y) {
        String sheet = null;
        if(x == 1 && y == 0){
            // right
            sheet = DraculaPunchGame.AUSTIN_RUN_270_DEG;
        }
        else if(x == -1 && y == 0){
            // left
            sheet = DraculaPunchGame.AUSTIN_RUN_90_DEG;
        }
        else if(x == 0 && y == 1){
            // up
            sheet = DraculaPunchGame.AUSTIN_RUN_0_DEG;
        }
        else if(x == 0 && y == -1){
            // down
            sheet = DraculaPunchGame.AUSTIN_RUN_180_DEG;
        }
        else if(x == 0 && y == 0){
            // stop - do nothing for now. No idle pose/anim
        }
        else{
            System.out.println("Invalid Direction: Unable to Animate");
        }
        return sheet;
    }

    @Override
    public int getRunWidth() {
        return RUN_WIDTH;
    }

    @Override
    public int getRunHeight() {
        return RUN_HEIGHT;
    }
}
