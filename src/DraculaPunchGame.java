import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class DraculaPunchGame extends StateBasedGame {

//    public BounceGame(String title, int width, int height) {
//        super(title);
//        ScreenHeight = height;
//        ScreenWidth = width;
//
//        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
//        explosions = new ArrayList<Bang>(10);
//
//    }

    public DraculaPunchGame(String name, int width, int height) {
        super(name);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

    }

    public static void main(String[] args) {
        AppGameContainer app;
        try {
            app = new AppGameContainer(new DraculaPunchGame("Dracula Punch", 800, 600));
            app.setDisplayMode(800, 600, false);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }
}
