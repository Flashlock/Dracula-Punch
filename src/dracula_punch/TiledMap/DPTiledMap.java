package dracula_punch.TiledMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class DPTiledMap extends TiledMap {
    public DPTiledMap(String ref) throws SlickException {
        super(ref);
    }

    /**
     * Use this function to render characters
     * @param visualY I'm not sure yet, currently it's always 0.
     *                Maybe this will change when the screen begins scrolling.
     * @param mapY y coordinate of the row being rendered.
     * @param layer the layer being rendered.
     */
    @Override
    protected void renderedLine(int visualY, int mapY, int layer) {
        super.renderedLine(visualY, mapY, layer);
    }
}
