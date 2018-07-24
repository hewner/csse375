package roulette;

public class RedBlackBet extends Bet {

    public enum BetOption {
        RED, BLACK
    }

    BetOption myOption;

    public RedBlackBet(BetOption option) {
        myOption = option;
    }

    public RedBlackBet() {
    }
    
    public int getOdds() {
        return 1;
    }

    public String getDescription() {
        return "Red or Black";
    }

    public boolean wheelMatchesBet(Wheel wheel) {
        return (wheel.getColor().equals(Wheel.BLACK) && myOption == BetOption.BLACK)
                || (wheel.getColor().equals(Wheel.RED) && myOption == BetOption.RED);
    }

    public boolean isThisKindOfBet(String parseableString) {
        return parseableString.contains("red") || parseableString.contains("black");
    }
    
    public Bet parseBet(String bet) {
        if (bet.contains("red"))
            return new RedBlackBet(RedBlackBet.BetOption.RED);

        if (bet.contains("black"))
            return new RedBlackBet(RedBlackBet.BetOption.BLACK);
        
        return null;
    }
    
}
