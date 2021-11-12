package dracula_punch.States;

import dracula_punch.Camera;
import dracula_punch.DraculaPunchGame;
import jig.Vector;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.tiled.TiledMap;

public abstract class LevelState extends BasicGameState {
    public TiledMap map;
    public Camera camera;

    public Vector getCameraPosition(){
        Vector screenOffset = getScreenOffset();
        return new Vector(
                camera.isometric.x + screenOffset.getX(),
                camera.isometric.y + screenOffset.getY()
        );
    }

    public Vector getScreenOffset(){
        return new Vector(
                DraculaPunchGame.SCREEN_WIDTH / camera.zoomFactor / 2,
                DraculaPunchGame.SCREEN_HEIGHT / camera.zoomFactor / 2
        );
    }
}
