package dracula_punch.Damage_System;

import java.util.ArrayList;

public interface IAttacker {
    /**
     * @return The melee damage this attacker deals
     */
    int getMeleeDamage();

    /**
     * @return The animation frame index which damage is dealt
     */
    int getMeleeDamageFrame();

    /**
     * @return The ranged damage this attacker deals
     */
    int getRangedDamage();

    /**
     * @return The animation frame index which the projectile is fired
     */
    int getRangedFireFrame();

    /**
     * @return The objects attacker is damaging
     */
    ArrayList<IDamageable> getTargetObjects();
}
