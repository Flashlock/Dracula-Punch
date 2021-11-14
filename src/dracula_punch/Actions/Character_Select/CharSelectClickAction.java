package dracula_punch.Actions.Character_Select;

import dracula_punch.Actions.Action;
import dracula_punch.DraculaPunchGame;
import dracula_punch.UI.Buttons.CharSelectButton;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import static dracula_punch.DraculaPunchGame.TEST_STATE;

public class CharSelectClickAction extends Action {
  private final DraculaPunchGame dpg;
  private final CharSelectButton button;

  public CharSelectClickAction(DraculaPunchGame dpg, CharSelectButton button){
    this.dpg = dpg;
    this.button = button;
  }

  @Override
  public void Execute() {
    System.out.println("Button Clicked: " + button.getCharID());
    DraculaPunchGame.characterChoice = button.getCharID();
    dpg.enterState(TEST_STATE, new FadeOutTransition(), new EmptyTransition());
  }
}
