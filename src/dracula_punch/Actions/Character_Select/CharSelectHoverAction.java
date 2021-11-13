package dracula_punch.Actions.Character_Select;

import dracula_punch.Actions.Action;
import dracula_punch.DraculaPunchGame;
import dracula_punch.UI.Buttons.CharSelectButton;

public class CharSelectHoverAction extends Action {
    private DraculaPunchGame dpg;
    private CharSelectButton button;

    public CharSelectHoverAction(DraculaPunchGame dpg, CharSelectButton button){
        this.dpg = dpg;
        this.button = button;
    }

    @Override
    public void Execute() {
        System.out.println("Button Hovered: " + button.getCharID());
    }
}
