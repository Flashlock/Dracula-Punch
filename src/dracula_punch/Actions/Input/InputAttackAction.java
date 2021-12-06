package dracula_punch.Actions.Input;

import dracula_punch.Actions.Action;
import dracula_punch.Characters.AmandaController;
import dracula_punch.Characters.AustinController;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Characters.RittaController;
import dracula_punch.DraculaPunchGame;

public class InputAttackAction extends Action {
    private final CharacterController controller;
    private final DraculaPunchGame.charIdEnum charID;

    public InputAttackAction(CharacterController controller){
        this.controller = controller;
        if(controller instanceof AustinController)
            charID = DraculaPunchGame.charIdEnum.AUSTIN;
        else if(controller instanceof AmandaController)
            charID = DraculaPunchGame.charIdEnum.AMANDA;
        else if(controller instanceof RittaController)
            charID = DraculaPunchGame.charIdEnum.RITTA;
        else{
            charID = DraculaPunchGame.charIdEnum.UNCHOSEN;
            System.out.println("Unknown Character Controller: ");
        }
    }

    @Override
    public void Execute() {
        if(controller.getAnimLock()){ return; }
        String sheet;
        switch (charID){
            case AUSTIN:
                sheet = controller.getMeleeSheet();
                break;
            case AMANDA:
            case RITTA:
                sheet = controller.getRangedSheet();
                break;
            default:
                System.out.println("Unknown Character Controller");
                return;
        }

        controller.animateAttack(sheet);
    }
}
