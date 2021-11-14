package dracula_punch.Actions.Character_Select;

import dracula_punch.Actions.Action;
import dracula_punch.DraculaPunchGame;
import dracula_punch.UI.Buttons.CharSelectButton;

public class CharSelectUnHoverAction extends Action {
    private final DraculaPunchGame dpg;
    private final CharSelectButton button;

    public CharSelectUnHoverAction(DraculaPunchGame dpg, CharSelectButton button){
        this.dpg = dpg;
        this.button = button;
    }

    @Override
    public void Execute() {
        System.out.println("Button UnHovered: " + button.getCharID());
    }
}
