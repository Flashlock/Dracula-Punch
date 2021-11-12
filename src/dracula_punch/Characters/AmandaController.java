package dracula_punch.Characters;

import dracula_punch.Actions.InputMoveAction;
import dracula_punch.Camera;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class AmandaController extends CharacterController{
    public static final int RUN_HEIGHT = 900;
    public static final int RUN_WIDTH = 580;

    private Camera camera;

    public AmandaController(float x, float y, LevelState curLevelState) {
        super(x, y, curLevelState);
        xRenderOffset = 10;
        yRenderOffset = 30;
        scaleFactor = .2f;

        camera = curLevelState.camera;
        setScale(scaleFactor);

        // Add a movement action - for animation switching
        curLevelState.inputMoveEvent.add(new InputMoveAction(this));
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {}

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        // render at the camera position
        Vector screenOffset = curLevelState.getScreenOffset();
        float x = screenOffset.getX() - 5 + xRenderOffset;
        float y = screenOffset.getY() - 5 - 900 / 2f * scaleFactor + yRenderOffset;

        setPosition(x, y);
        render(graphics);
    }

    @Override
    public void animateMove(Vector direction) {
        int x = (int) direction.getX();
        int y = (int) direction.getY();

        //region Find Sheet
        String sheet = null;
        if(x == 1 && y == 0){
            // right
            sheet = DraculaPunchGame.AMANDA_RUN_270_DEG;
        }
        else if(x == -1 && y == 0){
            // left
            sheet = DraculaPunchGame.AMANDA_RUN_90_DEG;
        }
        else if(x == 0 && y == 1){
            // up
            sheet = DraculaPunchGame.AMANDA_RUN_0_DEG;
        }
        else if(x == 0 && y == -1){
            // down
            sheet = DraculaPunchGame.AMANDA_RUN_180_DEG;
        }
        else if(x == 0 && y == 0){
            // stop
            curAnim.stop();
        }
        else{
            System.out.println("Invalid Direction: Unable to Animate");
        }
        //endregion

        if(sheet != null){
            removeAnimation(curAnim);
            curAnim = new Animation(
                    ResourceManager.getSpriteSheet(
                            sheet, RUN_WIDTH, RUN_HEIGHT
                    ),
                    DraculaPunchGame.ANIMATION_DURATION
            );
            curAnim.setLooping(true);
            addAnimation(curAnim);
        }
    }
}
