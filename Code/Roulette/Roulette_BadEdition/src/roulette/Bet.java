package roulette;

/**
 * Represents player's attempt to bet on outcome of the roulette wheel's spin.
 * 
 * @author Robert C. Duvall
 */
public class Bet
{

    public enum BetOption {
        ODD, EVEN, RED, BLACK, NUMBER_RANGE
    };	

    private BetOption option;
    private int number;
    
    public Bet(BetOption option) {
    	this.option = option;
    }
    
    public Bet(int number) {
    	this.option = BetOption.NUMBER_RANGE;
    	this.number = number;
    }

    public BetOption option() {
    	return this.option;
    }
    
    public int number() {
    	return this.number;
    }
    
    public int getPayoff(int amountBet, Wheel wheelState)
    {
        int amount = amountBet;
        if (wheelState.matchesBet(this))
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
    
    public int getOdds() {
    	switch(option()) {
    	case RED:
		case BLACK:
		case ODD:
		case EVEN:
			return 1;
		case NUMBER_RANGE:
			 return 11;
		default:
			throw new Error("Invaid bet type");
    	}
    }
    
}
