package roulette;

import java.util.ArrayList;
import java.util.List;

import roulette.RedBlackBet.BetOption;
import util.ConsoleReader;


/**
 * Plays a game of roulette.
 * 
 * @author Robert C. Duvall
 */
public class Game
{
    // name of the game
    private static final String DEFAULT_NAME = "Roulette";

    private Wheel myWheel;

    /**
     * Construct the game.
     */
    public Game ()
    {
        myWheel = new Wheel();
    }


    /**
     * @return name of the game
     */
    public String getName ()
    {
        return DEFAULT_NAME;
    }


    /**
     * Play a round of this game.
     *
     * For Roulette, this means prompting the player to make a bet, 
     * spinning the roulette wheel, and then verifying that the bet
     * is won or lost.
     *
     * @param player one that wants to play a round of the game
     */
    public void play (Gambler player)
    {
        int amount = ConsoleReader.promptRange("How much do you want to bet", 
                                               0, player.getBankroll());
        Bet betChoice = getAndParseBet();

        System.out.print("Spinning ...");
        myWheel.spin();
        System.out.println("Dropped into " + myWheel.getColor() + " " + myWheel.getNumber());
        amount = betChoice.getPayoff(amount, myWheel);
        player.updateBankroll(amount);
    }

    /**
     * Place the given bet by prompting the user for the specific information
     * need to complete the given bet.
     *
     * @param whichBet specific bet chosen by the user
     */
    private Bet getAndParseBet() {
        while (true) {
            String bet = ConsoleReader.promptString("What would you like to bet on? ");
            
            List<BetFactory> betKinds = new ArrayList<BetFactory>();
            betKinds.add(new RedBlackBet.Factory());
            betKinds.add(new OddEvenBet.Factory());
            betKinds.add(new ThreeNumbersBet.Factory());
            
            for(BetFactory betKind : betKinds) {
                if(betKind.isThisKindOfBet(bet))
                    return betKind.parseBet(bet);
            }

            System.out.println("Invalid bet!  Try again.");
        }
    }

}
