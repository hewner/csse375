package roulette;

public class BetFactory {
    
    Bet myBet;
    public BetFactory(Bet bet)
    {
        myBet = bet;
    }
    
    public boolean isThisKindOfBet(String parseableString)
    {
        return myBet.isThisKindOfBet(parseableString);
    }
    public Bet parseBet(String bet)
    {
        return myBet.parseBet(bet);
    }
}
