package roulette;

public class OddEvenBet extends Bet {
    
    public enum BetOption {
        ODD, EVEN
    };
    
    BetOption myChoice;
    
    public OddEvenBet(BetOption choice) {
        myChoice = choice;
    }
    
    public int getOdds() {
        return 1;
    }

    public String getDescription() {
        return "Odd or Even";
    }

    public boolean wheelMatchesBet(Wheel wheel) {
        return (wheel.getNumber() % 2 == 0 && myChoice == BetOption.EVEN) ||
        (wheel.getNumber() % 2 == 1 && myChoice == BetOption.ODD);
    }

}
