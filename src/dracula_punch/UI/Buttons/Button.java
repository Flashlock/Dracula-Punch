package dracula_punch.UI.Buttons;

import dracula_punch.Actions.Action;
import dracula_punch.Characters.GameObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public abstract class Button extends GameObject {
    protected boolean isMouseOver;
    public boolean getIsMouseOver(){ return isMouseOver; }

    public ArrayList<Action> hoverEvent, unHoverEvent, clickEvent;

    public Button(float x, float y) {
        super(x, y);
        hoverEvent = new ArrayList<>();
        unHoverEvent = new ArrayList<>();
        clickEvent = new ArrayList<>();
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

    /**
     * Call all actions assigned to the hover event.
     * Default is empty Execute(), override if you have data.
     */
    protected void hover(){
        for(Action action : hoverEvent)
            action.Execute();
    }

    /**
     * Call all actions assigned to unHover event.
     * Default is empty Execute(), override if you have data.
     */
    protected void unHover(){
        for(Action action : unHoverEvent)
            action.Execute();
    }

    /**
     * Call all actions assigned to click event.
     * Default is empty Execute(), override if you have data.
     */
    protected void click(){
        for(Action action : clickEvent)
            action.Execute();
    }
}
