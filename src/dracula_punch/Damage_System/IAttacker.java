package dracula_punch.Damage_System;

public interface IAttacker {
    /**
     * Perform your attack
     * @param attackType The type of attack to perform
     */
    void attack(AttackType attackType);
}
