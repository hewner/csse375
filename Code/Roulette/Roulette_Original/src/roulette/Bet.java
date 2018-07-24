package roulette;

/**
 * Represents player's attempt to bet on outcome of the roulette wheel's spin.
 * 
 * @author Robert C. Duvall
 */
public abstract class Bet
{

    /**
     * @return odds given by the house for this kind of bet
     */
    public abstract int getOdds ();


    public int getPayoff(int amountBet, Wheel wheelState)
    {
        int amount = amountBet;
        if (wheelMatchesBet(wheelState))
        {
            System.out.println("*** Congratulations :) You win ***");
            amount *= getOdds();
        }
        else
        {
            System.out.println("*** Sorry :( You lose ***");
            amount *= -1;
        }
        return amount;
    }
    
    /**
     * @return name of this kind of bet
     */
    public abstract String getDescription ();

    public abstract boolean wheelMatchesBet(Wheel wheel);
}
