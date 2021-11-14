package dracula_punch.UI.Buttons;

import dracula_punch.DraculaPunchGame;
import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class CharSelectButton extends Button{
  private final DraculaPunchGame.charIdEnum charID;
  public DraculaPunchGame.charIdEnum getCharID(){ return charID; }

  public CharSelectButton(float x, float y, String image, DraculaPunchGame.charIdEnum charID) {
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
