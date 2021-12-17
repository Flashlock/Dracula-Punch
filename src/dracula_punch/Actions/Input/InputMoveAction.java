package dracula_punch.Actions.Input;

import dracula_punch.Actions.Action;
import dracula_punch.Characters.Players.PlayerController;
import jig.Vector;

public class InputMoveAction extends Action {
    private final PlayerController controller;

    /**
     * Create a new input action for movement
     * @param characterController The character to move
     */
    public InputMoveAction(PlayerController characterController){
        this.controller = characterController;
    }

    @Override
    public void Execute(Object data) {
        if(controller.getAnimLock() || controller.getIsDead()) return;
        
        Vector dir = (Vector) data;
        controller.animateMove(dir);
    }
}
