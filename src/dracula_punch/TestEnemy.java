package dracula_punch;

import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.CharacterController;
import dracula_punch.States.LevelState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class TestEnemy extends CharacterController {
  private Coordinate startingTile, targetTile;

  //region Variables for implementing Dijkstra's pathfinding
  private DijkstraNode[][] nodeGrid;
  private PriorityQueue<DijkstraNode> nodesToVisit = new PriorityQueue<>();
  private ArrayList<DijkstraNode> enemyPath;
  private Coordinate previousTargetTile = new Coordinate(0,0);
  //endregion

  public TestEnemy(Coordinate startingTile, LevelState curLevelState){
    super(0,0,curLevelState, true);
    this.startingTile = new Coordinate(startingTile);
    TOTAL_MOVE_TIME = 200;
  }

  @Override
  public String getRunSheet(int x, int y) {
    return null;
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics){
    Camera cam = curLevelState.camera;
    setPosition(cam.getScreenPositionFromTile(currentTilePlusPartial));
    if(cam.isInScreenRange(currentTile)) {
      graphics.fillOval(
          getX() - 5,
          getY() - 5,
          10,
          10
      );
    }
  }
  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
    determineTarget();
    updatePath();
    move(delta);
    updateAnimation();
  }

  private void determineTarget() {
    // TODO Replace camera with a function that picks the nearest player
    targetTile =curLevelState.playerObjects.get(0).currentTile;
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
    if (movingTime < TOTAL_MOVE_TIME){ movingTime += delta; }
    else if (!currentTile.isEqual(previousTargetTile) && !enemyPath.isEmpty()){
      Coordinate next = enemyPath.remove(0).coord;
      movingTime = 0;
      previousTile.x = currentTile.x;
      previousTile.y = currentTile.y;
      currentTile.x = next.x;
      currentTile.y = next.y;
    }
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


  @Override
  public int getRunWidth() {
    return 0;
  }

  @Override
  public int getRunHeight() {
    return 0;
  }

  @Override
  public String getIdleSheet() {
    return null;
  }

  @Override
  public int getIdleWidth() {
    return 0;
  }

  @Override
  public int getIdleHeight() {
    return 0;
  }
}
