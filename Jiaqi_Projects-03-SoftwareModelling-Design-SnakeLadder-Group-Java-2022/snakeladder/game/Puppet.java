package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.Point;

public class Puppet extends Actor
{
  private GamePane gamePane;
  private NavigationPane navigationPane;
  private int cellIndex = 0;
  private int nbSteps;
  private Connection currentCon = null;
  private int y;
  private int dy;
  private boolean isAuto;
  private String puppetName;
  private boolean isLowest = false;
  private boolean moved = false;
  private Statisitics statisitics;
  private Toggle toggleStrategy = new ToggleStrategy();

  Puppet(GamePane gp, NavigationPane np, String puppetImage)
  {
    super(puppetImage);
    this.gamePane = gp;
    this.navigationPane = np;
    this.statisitics = new Statisitics(np.getNumberOfDice());
  }

  public boolean isAuto() {
    return isAuto;
  }

  public void setAuto(boolean auto) {
    isAuto = auto;
  }

  public String getPuppetName() {
    return puppetName;
  }

  public void setPuppetName(String puppetName) {
    this.puppetName = puppetName;
    this.statisitics.setPlayerName(puppetName);
  }

  void go(int nbSteps)
  {
    if (cellIndex == 100)  // after game over
    {
      cellIndex = 0;
      setLocation(gamePane.startLocation);
    }
    this.nbSteps = nbSteps;
    this.moved = false;
    setActEnabled(true);

    //Check if roll the lowest possible roll
    if (nbSteps == navigationPane.getNumberOfDice()){
      this.isLowest = true;
    }
    else {
      this.isLowest = false;
    }
  }

  void resetToStartingPoint() {
    cellIndex = 0;
    setLocation(gamePane.startLocation);
    setActEnabled(true);
  }

  int getCellIndex() {
    return cellIndex;
  }

  private void moveToNextCell()
  {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
    {
      if (ones == 0 && cellIndex > 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() + 1, getY()));
    }
    else     // Cells starting left 20, 40, .. 100
    {
      if (ones == 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() - 1, getY()));
    }
    cellIndex++;
  }

  Statisitics getStatistics(){
    return statisitics;
  }

  private void moveBack()
  {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
    {
      if (ones == 1 && cellIndex > 0)
        setLocation(new Location(getX(), getY() + 1));

      else
        setLocation(new Location(getX() - 1, getY()));
    }
    else     // Cells starting left 20, 40, .. 100
    {
      if(ones == 1) {
        setLocation(new Location(getX(), getY() + 1));
      }else{
        setLocation(new Location(getX() + 1, getY()));
      }
    }

    cellIndex--;
  }

  public void act()
  {
    if ((cellIndex / 10) % 2 == 0)
    {
      if (isHorzMirror())
        setHorzMirror(false);
    }
    else
    {
      if (!isHorzMirror())
        setHorzMirror(true);
    }

    // Animation: Move on connection
    if (currentCon != null && !(isLowest && currentCon.cellEnd < currentCon.cellStart)
        && nbSteps == 0)
    {
      int x = gamePane.x(y, currentCon);
      setPixelLocation(new Point(x, y));
      y += dy;

      // Check end of connection
      if ((dy > 0 && (y - gamePane.toPoint(currentCon.locEnd).y) > 0)
        || (dy < 0 && (y - gamePane.toPoint(currentCon.locEnd).y) < 0))
      {
        gamePane.setSimulationPeriod(100);
        setActEnabled(false);
        setLocation(currentCon.locEnd);
        cellIndex = currentCon.cellEnd;
        setLocationOffset(new Point(0, 0));
        currentCon = null;
        navigationPane.prepareRoll(cellIndex);
      }
      return;
    }

    // Normal movement
    if (nbSteps > 0)
    {
      moveToNextCell();
      moved = true;
      if (cellIndex == 100)  // Game over
      {
        setActEnabled(false);
        navigationPane.prepareRoll(cellIndex);
        return;
      }

      nbSteps--;
      if (nbSteps == 0)
      {
        checkConnection();
      }
    }
    else if (nbSteps == -1 && !moved){
      moveBack();
      System.out.println(moved);
      checkConnection();
      System.out.print("back");
      moved = true;
      System.out.println(puppetName);
      System.out.println(getLocation());
    }
  }

  private void checkConnection() {
    // Check if on connection start
    if ((currentCon = gamePane.getConnectionAt(getLocation())) != null
            && !(isLowest && currentCon.cellEnd < currentCon.cellStart)) {
      gamePane.setSimulationPeriod(50);
      y = gamePane.toPoint(currentCon.locStart).y;
      if (currentCon.locEnd.y > currentCon.locStart.y) {
        //update travel down
        dy = gamePane.animationStep;
        statisitics.setTravelDown(statisitics.getTravelDown()+1);
      }
      else {
        dy = -gamePane.animationStep;
        //update travel up
        statisitics.setTravelUp(statisitics.getTravelUp()+1);
      }
      // print statistics
      System.out.println(statisitics);
      if (currentCon instanceof Snake) {
        navigationPane.showStatus("Digesting...");
        navigationPane.playSound(GGSound.MMM);
      } else {
        navigationPane.showStatus("Climbing...");
        navigationPane.playSound(GGSound.BOING);
      }
    } else {
      setActEnabled(false);
      navigationPane.prepareRoll(cellIndex);
    }

    if (isAuto) {
      toggleStrategy.operateToggle(gamePane, navigationPane);
    }
  }
}
