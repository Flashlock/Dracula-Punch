package dracula_punch.Actions.Damage_System;

import dracula_punch.Actions.Action;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.Damage_System.IAttacker;

import java.util.ArrayList;

public class MeleeAction extends AttackAction {
    private final IAttacker attacker;

    public MeleeAction(IAttacker attacker, int frameActionIndex){
        super(frameActionIndex);
        this.attacker = attacker;
    }

    @Override
    public void Execute() {
        super.Execute();
        ArrayList<IDamageable> targets = attacker.getTargetObjects();
        int damage = attacker.getMeleeDamage();
        for(IDamageable target : targets){
            target.takeDamage(damage);
        }
    }
}
