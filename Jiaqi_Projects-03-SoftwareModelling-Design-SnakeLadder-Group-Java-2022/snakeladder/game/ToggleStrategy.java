package snakeladder.game;
import snakeladder.game.GamePane;
import snakeladder.game.NavigationPane;


public class ToggleStrategy implements Toggle{

    private final int UP = 1;
    private final int DOWN = 2;

    @Override
    public void operateToggle(GamePane gp, NavigationPane np) {
        int DiceNum = np.getNumberOfDice() * 6;
        Puppet nextPlayer = gp.getNextPlayer();

        int numUp = 0, numDown = 0;
        // get all of the possible cells index that opponents might reach
        for (int i=1; i<DiceNum; i++) {
            // assume the opponent is moving for "i" steps
            int currentCell = nextPlayer.getCellIndex()+i;
            // check if the current cell has connections such as ladder start or snake end
            if(gp.getConnectionAt(gp.cellToLocation(currentCell)) != null) {
                int checkConnectionType = gp.checkConnectionIsUp(currentCell);
                // current cell is going upwards
                if (checkConnectionType == UP) {
                    numUp++;
                    // current cell is going downwards
                } else if (checkConnectionType == DOWN) {
                    numDown++;
                }
            }
        }
        System.out.println("DOWN = " + numDown);
        System.out.println("UP = " + numUp);
        // more UPs then DOWNs, so we reverse all the connections.
        if (numUp >= numDown) {
            np.pressToggleButton();
            gp.reverseConnections(np.checkToggleButton());
        }
    }
}
