package dracula_punch.Actions.Damage_System;

import dracula_punch.Actions.Action;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.Damage_System.IMelee;

import java.util.ArrayList;

public class FinishMeleeAction extends Action {
    private final IMelee attacker;

    public FinishMeleeAction(IMelee attacker){
        this.attacker = attacker;
    }

    @Override
    public void Execute() {
        ArrayList<IDamageable> targets = attacker.getTargetObjects();
        int damage = attacker.getMeleeDamage();
        for(IDamageable target : targets){
            target.takeDamage(damage);
        }
    }
}
