package dracula_punch.Characters;

import dracula_punch.Camera;
import dracula_punch.States.LevelState;
import jig.Vector;
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

//        moveUp = new Animation(
//                ResourceManager.getSpriteSheet(DraculaPunchGame.AMANDA_RUN_0_DEG, 580,900),
//                50
//        );
//        moveUp.setLooping(true);
//        moveUp.stop();
//        addAnimation(moveUp);
//
//        moveDown = new Animation(
//                ResourceManager.getSpriteSheet(DraculaPunchGame.AMANDA_RUN_180_DEG, 580, 900),
//                50
//        );
//        moveDown.setLooping(true);
//        moveDown.stop();
        //addAnimation(moveDown);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        // Animate for the directions

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        // render at the camera position
        Vector screenOffset = curLevelState.getScreenOffset();
        float x = screenOffset.getX() - 5 + xRenderOffset;
        float y = screenOffset.getY() - 5 - 900 / 2f * scaleFactor + yRenderOffset;

        setPosition(x, y);
        render(graphics);
    }
}
