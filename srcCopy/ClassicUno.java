import java.util.Scanner;

public class ClassicUno extends Game {
    Scanner scan = new Scanner(System.in);
    private Card tableCard;
    private boolean TheGameIsFinished = false;
    int chosenCardToPlay;
    boolean cardIsPlayed;
    Player currentPlayer;



    public ClassicUno(int numOfPlayers) {
        super(numOfPlayers, new DefaultDeckInitializer());
        tableCard = deck.getFirstCardOnTable();
    }

    @Override
    public void play() {
        while (!TheGameIsFinished) {
            // display the first card in the game.
            System.out.println("Top Card: " + tableCard);
            System.out.println(players.get(currentPlayerID).getPlayerName() + "'s turn \n");

            //if the user change the color by the wild card he can see the new color from here
            if (tableCard instanceof Wild) {
                System.out.println("The Chosen Color of the wild card is : " + getChosenColor());
            }

            currentPlayer = players.get(currentPlayerID);
            currentPlayer.displayCards(this,tableCard);


            chosenCardToPlay = currentPlayer.chooseCard(this, deck, tableCard);

            if(chosenCardToPlay == -1) {
                skipPlayer();
                System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*  \n");
                continue;
            }

            // Ensure valid card index before attempting to play
            cardIsPlayed = currentPlayer.getPlayerCards().get(chosenCardToPlay).canBePlayed(tableCard, getChosenColor());

            while (!cardIsPlayed) {
                System.out.println("Not Valid. Please choose a valid Card!");

                chosenCardToPlay = currentPlayer.chooseCard(this, deck, tableCard);

                cardIsPlayed = currentPlayer.getPlayerCards().get(chosenCardToPlay).canBePlayed(tableCard, getChosenColor());
            }


            int nextPlayerID = currentPlayerID + 1;
            if (currentPlayerID == players.size() - 1) {
                nextPlayerID = 0;
            }

            // calling howToWin() to decide the player can win (he must say uno + finish his cards
            howToWin();

            int prevPlayerId = currentPlayerID; // because the current player will change after the action

            // Applying card Effect.
            currentPlayer.getPlayerCards().get(chosenCardToPlay).cardEffect(this, players.get(nextPlayerID).getPlayerName());


            //if there is no winner we will continue
            skipPlayer();
            tableCard = players.get(prevPlayerId).getPlayerCards().get(chosenCardToPlay);
            deck.putOnTable(tableCard);
            players.get(prevPlayerId).getPlayerCards().remove(chosenCardToPlay);


            // check if there is a winner
            if (checkWinCondition(players.get(prevPlayerId))) {
                theWinnerFormat(players.get(prevPlayerId));
                TheGameIsFinished = true;

            }

            // if the player did not say Uno when he plays the card he should draw 2 cards
            Penalty(players.get(prevPlayerId));

            System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*  \n");
        }

    }

    public boolean checkWinCondition(Player player) {
        return (player.getPlayerCards().isEmpty() && player.getSayUno().isUnoDeclared() &&
                player.getSayUno().getUnoCount() == 2);
    }

    @Override
    public void howToWin() {
        SayUno unoStatus = currentPlayer.getSayUno();

        // Reset UNO status if the player has declared UNO but the game is still ongoing
        if (unoStatus.getUnoCount() == 2 && !TheGameIsFinished) {
            unoStatus.setUnoDeclared(false);
            unoStatus.setUnoCount(0);
        }

        // Ask the player to declare UNO if not already done
        if (unoStatus.getUnoCount() != 1) {
            System.out.println("If you have one card, type \"UNO\", otherwise type Enter");
            unoStatus.setUnoWord(scan.nextLine().trim());
        }

        // Validate if the player correctly declared UNO
        if (unoStatus.isUnoDeclared() && unoStatus.getUnoCount() == 1) {
            unoStatus.setUnoCount(2);
        }

        // Handle player's UNO declaration
        if ("UNO".equalsIgnoreCase(unoStatus.getUnoWord())) {
            unoStatus.setUnoDeclared(true);
            unoStatus.setUnoCount(1);
            unoStatus.setUnoWord("1");
        }
    }

}