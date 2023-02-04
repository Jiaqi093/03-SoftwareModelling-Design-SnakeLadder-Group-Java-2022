package snakeladder.game;
import java.util.ArrayList;
import java.util.List;

public class DieOperation {

    private NavigationPane np;
    private List<Die> dice;
    private int totalPips;
    private int curNumOfDice;

    public DieOperation(NavigationPane np){
        this.np = np;
        this.dice = new ArrayList<>();
        this.totalPips = 0;
        this.curNumOfDice = 0;
    }

    public void addDie(int nb){
        Die die = new Die(nb, this);
        dice.add(die);
        totalPips += nb;
        curNumOfDice++;
    }

    public void finishRolling(){
        if(dice.size() == np.getNumberOfDice()){
            np.startMoving(totalPips);
            resetDieOperation();
        }
    }

    public Die getLastDie(){
        return dice.get(dice.size() - 1);
    }

    public void resetDieOperation(){
        this.totalPips = 0;
        this.curNumOfDice = 0;
        this.dice.clear();
    }

    public int getSizeOfDice(){
        return dice.size();
    }

    public int getCurNumOfDice() {
        return curNumOfDice;
    }
}
