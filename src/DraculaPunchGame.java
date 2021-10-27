import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import jig.Entity;

public class DraculaPunchGame extends StateBasedGame {

    public final int screenWidth, screenHeight;

    public DraculaPunchGame(String name, int width, int height) {
        super(name);
        screenWidth = width;
        screenHeight = height;

        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
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
