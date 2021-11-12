package dracula_punch.Actions;

import dracula_punch.Characters.CharacterController;
import jig.Vector;

public class InputMoveAction extends Action{
    private final CharacterController characterController;

    /**
     * Create a new input action for movement
     * @param characterController The character to move
     */
    public InputMoveAction(CharacterController characterController){
        this.characterController = characterController;
    }

    @Override
    public void Execute(Object data) {
        Vector dir = (Vector) data;
        characterController.animateMove(dir);
    }
}
