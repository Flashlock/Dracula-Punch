package dracula_punch.Characters;

import dracula_punch.DraculaPunchGame;
import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.Animation;

public class CharacterController extends Entity {
    private Animation run;

    public CharacterController(final float x, final float y){
        super(x, y);
        run = new Animation(
                ResourceManager.getSpriteSheet(DraculaPunchGame.AMANDA_RUN_0_DEG, 580,900),
                50
                );
        addAnimation(run);
        run.setLooping(true);
    }

}
