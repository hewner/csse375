package roulette;

public abstract class BetFactory {
    public abstract boolean isThisKindOfBet(String parseableString);
    public abstract Bet parseBet(String bet);
}
