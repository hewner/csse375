package roulette;

public class RedBlackBet extends Bet {

    public enum BetOption {
        RED, BLACK
    }

    BetOption myOption;

    public RedBlackBet(BetOption option) {
        myOption = option;
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

}
