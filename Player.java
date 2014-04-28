/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		April 2014
 * 		CS 342 Software Design
 * 
 * ------------------------------------------------------------------------*/
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JPanel;

public class Player 
{
	private Vector<Card> hand;
	private GameGUI gui;
	private Client client;
	private Deck deck;
	private boolean drewCard;
	private int from = 0;
	
	public Player(GameGUI gui, Client client, Deck deck)
	{
		hand = new Vector<Card>();
		this.gui = gui;
		this.client = client;
		this.deck = deck;
		drewCard = false;
	}
	
	public void executeTurn()
	{
		//----------------------------------------------------------------
        // Draw phase
        //----------------------------------------------------------------
		
		// Print rules for draw phase
		String message = "Draw a card from the deck or from the top of the discard pile\n\n";
		gui.getPlayArea().append(message);
		
		// Wait for the user to draw a card
		while (!drewCard){}
		drewCard = false;
		
		// Draw card and add to hand
		if (from == 1)
		{
			Card card = deck.drawCardFromDiscard();
			if (card == null)
			{
				gui.getPlayArea().append("Discard pile is empty, drawing from deck instead\n\n");
				hand.add(deck.drawCard());
			}
			else
			{
				hand.add(card);
				
				/* ******************** TO DO: Send message to all users ********************
				 * message: client.username + " drew " + card.getCard() + "from the discard pile\n"
				 */
			}
		}
		else // Draw from deck
		{
			hand.add(deck.drawCard());
		}
			
		// ******************** TO DO: disable draw buttons ********************
		
		//----------------------------------------------------------------
        // Optional phase - user can chose to meld or lay off cards
        //----------------------------------------------------------------
		message = "To play enter a command and the number(s) of the card\n"
				+ "to be acted upon separated by a space. Examples:\n\n"
				+ "Hand: 0  1  2  3  4\n"
				+ "      3C 4S 4H 4C 7D\n\n"
				+ "Commands:\n"
				+ "M = meld\tExample: \"M 2 3 4\"\n"
				+ "L = lay off\tExample: \"L a 4\"\n"
				+ "                            ^meld to add to if there is one\n"
				+ "D = discard\tExample: \"D 0\"\n"
				+ "Discard 1 card to end your turn\n\n";
		
		gui.getPlayArea().append(message); // Print rules to user
		
		// Accept input until user discards a card
		while (true)
		{	
			printHand();
			
			String string = gui.getGameInteractionArea().getText();
			gui.getGameInteractionArea().setText(""); 	// Clear the text
			String input[] = string.split("\\s+");	// Split string up by spaces
			
			if (input[0].compareTo("M") == 0)	// Meld
			{
				// Translate input to cardo\0q	2	wy7i9o0-55 tvin
				Vector<Card> tempCards = new Vector<Card>();
				intputToCards(tempCards, input, 1);
				sortCards(tempCards);
				
				Meld meld = new Meld(tempCards, deck.getMelds().size());
				
				// If the cards create a valid meld then add it to the list and
				// remove the cards from the player's hands
				if (meld.valid == false)
					gui.getPlayArea().append("Specified cards do not make a vaild meld\n\n");
				else
				{
					deck.getMelds().add(meld);
					removeCards(input, 1);
					
					// Update the melds for all users
					gui.setMeldsPanel(deck.updateGUIMelds());
					
					/* ******************** TO DO: Send message to all users ********************
					 * message: client.username + " created meld " + meld.getName() + "\n"
					 * 
					 * print out cards added
					 */
					
					checkForWinner();
				}
			}
			else if (input[0].compareTo("L") == 0)	// Lay off
			{
				// Translate input to cards
				Vector<Card> tempCards = new Vector<Card>();
				intputToCards(tempCards, input, 2);
				sortCards(tempCards);
				
				// Check to see if the meld exists
				boolean exists = false;
				int index = 0;
				for (int i=0; i<deck.getMelds().size(); i++)
				{
					if (deck.getMelds().get(i).getName() == input[1].charAt(0))
					{
						exists = true;
						index = i;
					}
				}
				
				if (exists == false)
				{
					gui.getPlayArea().append("Specified meld name does not exist\n\n");
					continue;
				}
				
				// Check if input can add to a meld and remain valid
				Meld meld = new Meld(tempCards, deck.getMelds().get(index));
				
				// If the cards can add to and keep a valid meld then 
				// update the list and remove the cards from the player's hands
				if (meld.valid == false)
					gui.getPlayArea().append("Specified cards are not a vaild lay off\n\n");
				else
				{
					deck.getMelds().remove(index);
					deck.getMelds().add(meld);
					removeCards(input, 2);
					
					// Update the melds for all users
					gui.setMeldsPanel(deck.updateGUIMelds());
					
					/* ******************** TO DO: Send message to all users ********************
					 * message: client.username + " laid off to meld " + meld.getName() + "\n"
					 * 
					 * print out cards added
					 */
					
					checkForWinner();
				}
			}
			else if (input[0].compareTo("D") == 0)	// Discard
			{
				int index = Integer.parseInt(input[1]);
				
				// Make sure the input was valid
				if (index >= hand.size() || index < 0)
				{
					gui.getPlayArea().append("Card is not in the range shown\n\n");
				}
				else
				{
					deck.addToDiscardPile(hand.get(index));
					
					/* ******************** TO DO: Send message to all users ********************
					 * message: client.username + " discarded " + hand.get(index).getCard() + "\n"
					 */
					
					hand.remove(index);
					checkForWinner();
					break;
				}
			}
			else	// Invalid input
			{
				gui.getPlayArea().append("You entered an invalid command\n");
			}
		}
	
		/* ******************** TO DO: Send message to server ********************
		 * message: this client is done with its turn
		 */
	}

	/**
	 * Sorts a list of cards by rank. 
	 */
	private void sortCards(Vector<Card> cards)
	{
		Collections.sort(cards, new Comparator<Card>()
		{
			@Override
			public int compare (Card c1, Card c2)
			{
				if (c1.getNumericRank() > c2.getNumericRank())
					return 1;
				else if (c1.getNumericRank() < c2.getNumericRank())
					return -1;
				else 
					return 0;
			}
		});
	}
	
	/**
	 * Prints out the cards in the player's hand
	 */
	private void printHand()
	{
		sortCards(hand);		// Sort hand for printing
		StringBuilder builder = new StringBuilder();
		
		for (int i=0; i<hand.size(); i++)
		{
			if (i<10)
				builder.append(i + "  ");
			else
				builder.append(i + " ");
		}
		
		builder.append("\n");
		
		for (int i=0; i<hand.size(); i++)
			builder.append(hand.get(i).getCard() + " ");
		
		builder.append("\n");
		builder.append("\n");
		
		gui.getPlayArea().append(builder.toString());
	}
	
	/**
	 * Flags that user has drawn a card
	 */
	public void drawCard(int from)
	{ 
		drewCard = true;
		this.from = from;
	}
	
	/** 
	 * @return list of the player's hand of cards 
	 */
	public Vector<Card> getHand()
	{
		return hand;
	}
	
	/**
	 * Takes input array of stings, converts values to indexes in hand vector,
	 * adds cards from hand to a list that will be used for melds. Cards in the
	 * hand are not removed yet.
	 * 
	 * @param list		Destination for cards
	 * @param input		String array of indexes
	 * @param start		Where to start looping through 
	 * @return			true/false if all values are valid (not out of range)
	 */
	private boolean intputToCards(Vector<Card> list, String input[], int start)
	{
		for (int i=start; i<input.length; i++)
		{
			int index = Integer.parseInt(input[i]);
			if (index < 0 || index > hand.size()) 	// Not in vector size range
			{
				gui.getPlayArea().append("You specified cards not in the range\n\n");
				return false;
			}
			list.add(hand.get(index));
		}
		return true;
	}
	
	/**
	 * Removes the cards specified in the input array of indexes in the hand.
	 * Note that error checking is not necessary since it was done prior to 
	 * this method call.
	 * 
	 * @param input		String array of indexes
	 * @param start		Where to start looping through 
	 */
	private void removeCards(String input[], int start)
	{
		for (int i=start; i<input.length; i++)
			hand.remove(Integer.parseInt(input[i]));
	}
	
	/**
	 * Method check to see if a player has won the game, and notifies the server if
	 * the do. A game is won when the play has no cards in their hand.
	 */
	private void checkForWinner()
	{
		if (hand.size() == 0)
		{
			/* ******************** TO DO: Send message to server ********************
			 * This player has won - notify all other players and stop the game
			 */
		}
	}
}













