package dracula_punch.Actions.Input;

import dracula_punch.Actions.Action;
import dracula_punch.Characters.CharacterController;
import jig.Vector;

public class InputAttackAction extends Action {
    private CharacterController controller;

    public InputAttackAction(CharacterController controller){
        this.controller = controller;
    }

    @Override
    public void Execute() {
        if(controller.getInputLock()) return;
        controller.animateAttack();
    }
}
