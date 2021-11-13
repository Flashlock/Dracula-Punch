package dracula_punch.UI.Buttons;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class CharSelectButton extends Button{
    private final int charID;
    public int getCharID(){ return charID; }

    public CharSelectButton(float x, float y, String image, int charID) {
        super(x, y);
        addImageWithBoundingBox(ResourceManager.getImage(image));
        setScale(.15f);
        this.charID = charID;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        render(graphics);
    }
}
