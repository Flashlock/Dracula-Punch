package dracula_punch;

import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class TestEnemy extends CharacterController {
  private Coordinate startingTile, targetTile;
  private boolean isMoving;
  public boolean getIsMoving(){ return isMoving; }

  //region Dijkstra's Variables
  private DijkstraNode[][] nodeGrid;
  private PriorityQueue<DijkstraNode> nodesToVisit = new PriorityQueue<>();
  private ArrayList<DijkstraNode> enemyPath;
  private Coordinate previousTargetTile = new Coordinate(0,0);
  //endregion

  public TestEnemy(Coordinate startingTile, LevelState curLevelState){
    super(0,0, curLevelState);
    this.startingTile = new Coordinate(startingTile);
    TOTAL_MOVE_TIME = 200;
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
    super.update(gameContainer, stateBasedGame, delta);
    determineTarget();
    updatePath();
    move(delta);
    updateAnimation();
  }

  private void determineTarget() {
    // TODO Replace camera with a function that picks the nearest player
    targetTile = curLevelState.playerObjects.get(0).currentTile;
    //targetTile = curLevelState.camera.currentTile;
  }

  private void updatePath() {
    if (!previousTargetTile.isEqual(targetTile)) {
      calculatePath();
      enemyPath = getPath();
      previousTargetTile.x = targetTile.x;
      previousTargetTile.y = targetTile.y;
    }
  }

  private void calculatePath() {
    // Adapted from https://thiloshon.wordpress.com/2017/04/03/dijkstras-algorithm-for-path-finding-problems/
    int gridWidth = curLevelState.map.getWidth();
    int gridHeight = curLevelState.map.getHeight();
    initializeNodeGrid(gridWidth, gridHeight);
    startAtTargetTile();
    calculateDistanceOfEachNodeFromTarget(gridWidth, gridHeight);
  }

  private void initializeNodeGrid(int gridWidth, int gridHeight) {
    nodeGrid = new DijkstraNode[gridWidth][gridHeight];
    for (int i = 0; i < gridWidth; i++) {
      for (int j = 0; j < gridHeight; j++) {
        nodeGrid[i][j] = new DijkstraNode(i, j);
        if (curLevelState.map.isPassable[i][j] || (i == startingTile.x && j == startingTile.y)) {
          nodeGrid[i][j].isPassable = true;
        }
      }
    }
  }

  private void startAtTargetTile() {
    DijkstraNode target = nodeGrid[(int)targetTile.x][(int)targetTile.y];
    target.distance = 0;
    nodesToVisit.add(target);
  }

  private void calculateDistanceOfEachNodeFromTarget(int gridWidth, int gridHeight) {
    while (nodesToVisit.size() > 0) {
      // Get the shortest distance node from top of priority queue
      DijkstraNode cursor = nodesToVisit.remove();
      cursor.visited = true;
      // Check nodes on left, above, below, and on right
      if (cursor.x > 0) { checkNeighbor(cursor, nodeGrid[cursor.x - 1][cursor.y]); }
      if (cursor.y > 0) { checkNeighbor(cursor, nodeGrid[cursor.x][cursor.y - 1]); }
      if (cursor.y + 1 < gridHeight) { checkNeighbor(cursor, nodeGrid[cursor.x][cursor.y + 1]); }
      if (cursor.x + 1 < gridWidth) { checkNeighbor(cursor, nodeGrid[cursor.x + 1][cursor.y]); }
    }
  }

  private void checkNeighbor(DijkstraNode cursor, DijkstraNode neighbor) {
    if (!neighbor.visited && neighbor.isPassable && neighbor.distance > cursor.distance + 1) {
      neighbor.distance = cursor.distance + 1;
      neighbor.nextInPath = cursor;
      nodesToVisit.add(neighbor);
    }
  }

  private ArrayList<DijkstraNode> getPath() {
    // Unwind nodes from enemy back to target
    DijkstraNode unwindCursor = nodeGrid[(int)currentTile.x][(int)currentTile.y];
    if ((nodeGrid[unwindCursor.x][unwindCursor.y].distance < Integer.MAX_VALUE)) {
      ArrayList<DijkstraNode> path = new ArrayList<>();
      while (unwindCursor.nextInPath != null) {
        path.add(unwindCursor.nextInPath);
        unwindCursor = unwindCursor.nextInPath;
      }
      return path;
    } else return null;
  }

  private void move(int delta) {
    boolean moved = true;
    if (movingTime < TOTAL_MOVE_TIME){ movingTime += delta; }
    else if (!currentTile.isEqual(previousTargetTile) && !enemyPath.isEmpty()){
      Coordinate next = enemyPath.remove(0).coord;
      movingTime = 0;
      previousTile.x = currentTile.x;
      previousTile.y = currentTile.y;
      currentTile.x = next.x;
      currentTile.y = next.y;

      float x = next.x - previousTile.x;
      float y = previousTile.y - next.y;
      Vector dir = new Vector(x, y);
      dir = dir.scale(1 / dir.length());
      if(dir.getX() != facingDir.getX() || dir.getY() != facingDir.getY()){
        // Change directions
        animateMove(dir);
      }
    }
    else moved = false;

    if(!moved && isMoving){
      // stopped
      animateMove(new Vector(0,0));
    }
    isMoving = moved;
  }

  private void updateAnimation() {
    float percentMoveDone = (TOTAL_MOVE_TIME - movingTime) / TOTAL_MOVE_TIME;
    float partialX = 0, partialY = 0;
    if (previousTile.y > currentTile.y){ partialY = percentMoveDone;}
    else if (previousTile.y < currentTile.y){ partialY = -percentMoveDone;}
    if (previousTile.x > currentTile.x){ partialX = percentMoveDone;}
    else if (previousTile.x < currentTile.x){ partialX = -percentMoveDone;}
    currentTilePlusPartial = new Coordinate(currentTile);
    currentTilePlusPartial.add(partialX, partialY);
  }

  //region Character Controller
  @Override
  public String getRunSheet(int x, int y) {
    return DraculaPunchGame.getSheetHelper(
            DraculaPunchGame.DRACULA_WALK_0_DEG,
            DraculaPunchGame.DRACULA_WALK_180_DEG,
            DraculaPunchGame.DRACULA_WALK_90_DEG,
            DraculaPunchGame.DRACULA_WALK_270_DEG,
            x,
            y
    );
  }

  @Override
  public String getIdleSheet() {
    return DraculaPunchGame.DRACULA_IDLE;
  }

  @Override
  public String getMeleeSheet() {
    return getSheetHelper(
            DraculaPunchGame.DRACULA_MELEE_0_DEG,
            DraculaPunchGame.DRACULA_MELEE_180_DEG,
            DraculaPunchGame.DRACULA_MELEE_90_DEG,
            DraculaPunchGame.DRACULA_MELEE_270_DEG
    );
  }

  @Override
  public String getRangedSheet() {
    return null;
  }
  //endregion
}
