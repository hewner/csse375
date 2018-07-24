package roulette;


public class CheatingBet extends Bet {

    public int getOdds() {
        return 0;
    }

    public String getDescription() {
        return "Secret cheating bet!";
    }

    public boolean wheelMatchesBet(Wheel wheel) {
        return false;
    }

    public int getPayoff(int amountBet, Wheel wheelState)
    {
        System.out.println("*wink*");
        return 100;
    }

}
