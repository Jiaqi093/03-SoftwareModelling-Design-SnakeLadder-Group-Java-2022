package snakeladder.game;

import ch.aplu.jgamegrid.Actor;

public class Die extends Actor
{
  private int nb;
  private DieOperation dieOperation;

  Die(int nb, DieOperation dieOperation)
  {
    super("sprites/pips" + nb + ".gif", 7);
    this.nb = nb;
    this.dieOperation = dieOperation;
  }

  public void act()
  {
    showNextSprite();
    if (getIdVisible() == 6)
    {
      dieOperation.finishRolling();
      setActEnabled(false);
    }
  }
}
