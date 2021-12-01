package dracula_punch.Damage_System.Projectiles;

import dracula_punch.Characters.GameObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Projectile extends GameObject {
    public Projectile(float x, float y) {
        super(x, y);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {

    }
}
