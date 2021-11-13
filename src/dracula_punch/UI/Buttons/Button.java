package dracula_punch.UI.Buttons;

import dracula_punch.Characters.GameObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Button extends GameObject {
    protected boolean isMouseOver;
    public boolean getIsMouseOver(){ return isMouseOver; }

    public Button(float x, float y) {
        super(x, y);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
        Input input = gameContainer.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        boolean xCheck = mouseX > getCoarseGrainedMinX() && mouseX < getCoarseGrainedMaxX();
        boolean yCheck = mouseY > getCoarseGrainedMinY() && mouseY < getCoarseGrainedMaxY();

        if(xCheck && yCheck){
            if(!isMouseOver){
                // Begin hovering
                isMouseOver = true;
                hover();
            }

            if(input.isMousePressed(0)){
                // Click the button
                click();
            }
        }

        if(isMouseOver && !(xCheck && yCheck)){
            // Stop hovering
            isMouseOver = false;
            unHover();
        }
    }

    protected abstract void hover();

    protected abstract void unHover();

    protected abstract void click();
}
