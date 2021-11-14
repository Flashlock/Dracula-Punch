package dracula_punch.States;

import dracula_punch.DraculaPunchGame;
import dracula_punch.UI.Buttons.CharSelectButton;
import dracula_punch.Actions.Character_Select.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class CharacterSelectState extends BasicGameState {
  private CharSelectButton[] charSelectButtons;

  @Override
  public int getID() {
    return DraculaPunchGame.CHARACTER_SELECT_STATE;
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {}

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    int xRadius = 200;
    int yRadius = 125;
    float midX = DraculaPunchGame.SCREEN_WIDTH / 2f;
    float midY = DraculaPunchGame.SCREEN_HEIGHT / 2f;

    charSelectButtons = new CharSelectButton[]{
        new CharSelectButton(
            midX,
            midY + yRadius,
            DraculaPunchGame.AUSTIN_CHAR_SELECT,
            DraculaPunchGame.charIdEnum.AUSTIN
        ),
        new CharSelectButton(
            midX - xRadius,
            midY - yRadius,
            DraculaPunchGame.AMANDA_CHAR_SELECT,
            DraculaPunchGame.charIdEnum.AMANDA
        ),
        new CharSelectButton(
            midX + xRadius,
            midY - yRadius,
            DraculaPunchGame.RITTA_CHAR_SELECT,
            DraculaPunchGame.charIdEnum.RITTA
        )
    };

    // Assign actions to each button's events
    DraculaPunchGame dpg = (DraculaPunchGame) game;
    for (CharSelectButton button : charSelectButtons) {
      button.clickEvent.add(new CharSelectClickAction(dpg, button));
      button.hoverEvent.add(new CharSelectHoverAction(dpg, button));
      button.unHoverEvent.add(new CharSelectUnHoverAction(dpg, button));
    }
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    graphics.drawString("Character Select", 10, 25);
    for(CharSelectButton button : charSelectButtons){
      button.render(gameContainer, stateBasedGame, graphics);
    }
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
    for(CharSelectButton button : charSelectButtons){
      button.update(gameContainer, stateBasedGame, i);
    }
  }
}
