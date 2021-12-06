package dracula_punch.Actions.Damage_System;

import dracula_punch.Actions.Action;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.IAttacker;

public class AttackAction extends Action {
    private final int frameActionIndex;
    private final IAttacker attacker;
    private final AttackType attackType;

    public int getFrameActionIndex(){ return frameActionIndex; }

    public boolean actionTriggered;

    public AttackAction(IAttacker attacker, int frameActionIndex, AttackType attackType){
        this.frameActionIndex = frameActionIndex;
        this.attacker = attacker;
        this.attackType = attackType;
    }

    @Override
    public void Execute() {
        actionTriggered = true;
        attacker.attack(attackType);
    }
}
