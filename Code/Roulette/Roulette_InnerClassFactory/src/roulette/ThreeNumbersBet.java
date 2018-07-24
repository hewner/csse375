package roulette;


public class ThreeNumbersBet extends Bet {

    int myRangeStart;
    
    public ThreeNumbersBet(int numBet) {
        myRangeStart = numBet;
    }

    public ThreeNumbersBet() {
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
    
    public static class Factory extends BetFactory {
        public boolean isThisKindOfBet(String parseableString) {
            try {
                int numBet = Integer.parseInt(parseableString);
                if (numBet >= 1 && numBet < 34) {
                    return true;
                }
            } catch (NumberFormatException e) {
                // in this case, we really want to do nothing
            }
            return false;

        }

        public Bet parseBet(String bet) {
            int numBet = Integer.parseInt(bet);
            if (numBet >= 1 && numBet < 34) {
                return new ThreeNumbersBet(numBet);
            }
            return null;
        }

    }
}
