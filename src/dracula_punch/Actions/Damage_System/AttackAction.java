package dracula_punch.Actions.Damage_System;

import dracula_punch.Actions.Action;

public abstract class AttackAction extends Action {
    private final int frameActionIndex;

    public int getFrameActionIndex(){ return frameActionIndex; }

    public boolean actionTriggered;

    public AttackAction(int frameActionIndex){
        this.frameActionIndex = frameActionIndex;
    }

    @Override
    public void Execute() {
        actionTriggered = true;
    }

    @Override
    public void Execute(Object data) {
        actionTriggered = true;
    }
}
