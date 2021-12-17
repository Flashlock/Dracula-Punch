package dracula_punch.Damage_System;

public interface IDamageable {
    /**
     * The attacker calls this function on the defender
     * @param damage Amount of damage taken
     */
    void takeDamage(int damage);

    /**
     * The healer calls this function on the defender
     * @param health Amount healed
     */
    void heal(int health);

    /**
     * @return Object's max health
     */
    int getMaxHealth();

    /**
     * @return Object's current health
     */
    int getCurrentHealth();
}
