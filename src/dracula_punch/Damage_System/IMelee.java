package dracula_punch.Damage_System;

import java.util.ArrayList;

public interface IMelee {
    /**
     * @return The melee damage this attacker deals
     */
    int getMeleeDamage();

    /**
     * @return The objects attacker is damaging
     */
    ArrayList<IDamageable> getTargetObjects();
}
