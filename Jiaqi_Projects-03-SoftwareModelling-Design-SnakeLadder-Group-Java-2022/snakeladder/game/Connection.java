package snakeladder.game;

import ch.aplu.jgamegrid.Location;

public abstract class Connection
{
  Location locStart;
  Location locEnd;
  int cellStart;
  int cellEnd;
  private boolean reversedConnection = false;

  Connection(int cellStart, int cellEnd)
  {
    this.cellStart = cellStart;
    this.cellEnd = cellEnd;
    locStart = GamePane.cellToLocation(cellStart);
    locEnd = GamePane.cellToLocation(cellEnd);
  }

  /*** the place where you swap the down and up path of one connection ***/
  public void reverseOneConnection(boolean reversedConnection) {
    Location locTemp = locStart;
    locStart = locEnd;
    locEnd = locTemp;

    int cellTemp = cellStart;
    cellStart = cellEnd;
    cellEnd = cellTemp;

    this.reversedConnection = reversedConnection;
  }

  String imagePath;

  public Location getLocStart() {
    return locStart;
  }

  public Location getLocEnd() {
    return locEnd;
  }

  public int getCellStart() { return cellStart; }

  public int getCellEnd() {return cellEnd; }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public double xLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_HORIZONTAL_CELLS;
  }
  public double yLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_VERTICAL_CELLS;
  }

  /***use cell index to check whether the connection is a ladder or a snake***/
  public boolean isLadder() {
    if (cellStart > cellEnd && !reversedConnection || cellEnd > cellStart && reversedConnection) {
      return false;
    }
    return true;
  }
}
