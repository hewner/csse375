package roulette;


public class ThreeNumbersBet extends Bet {

    int myRangeStart;
    
    public ThreeNumbersBet(int numBet) {
        myRangeStart = numBet;
    }

    public int getOdds() {
        return 11;
    }

    public String getDescription() {
        return "Three in a Row";
    }

    public boolean wheelMatchesBet(Wheel wheel) {
        return (myRangeStart <= wheel.getNumber() && wheel.getNumber() < myRangeStart + 3);
    }

}
