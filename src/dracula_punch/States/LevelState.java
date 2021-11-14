package dracula_punch.States;

import dracula_punch.Actions.Action;
import dracula_punch.Camera.Camera;
import dracula_punch.DraculaPunchGame;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

public abstract class LevelState extends BasicGameState {
    public DPTiledMap map;
    public Camera camera;

    public ArrayList<Action> inputMoveEvent = new ArrayList<>();

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
