/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		April 2014
 * 		CS 342 Software Design
 * 
 * Class holds all variables and methods needed to manipulate a deck of cards
 * ------------------------------------------------------------------------*/
import java.awt.GridLayout;
import java.util.*;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class Deck 
{
	private Vector<Card> deck;
	private Vector<Card> discardPile;
	private Vector<Meld> melds;
	
	/**
	 * The constructor initializes a deck and shuffles it for distribution.
	 */
	public Deck()
	{
		// Prepare lists
		deck = new Vector<Card>();
		discardPile = new Vector<Card>();
		melds = new Vector<Meld>();
		for (int i=0; i<52; i++)
			deck.add(new Card());
		
		/*
		 * Initializes each card
		 * Unshuffled deck by index: 
		 * 			2  3  4  5  6  7  8  9  T  J  Q  K  A
		 * Clubs:	0  1  2  3  4  5  6  7  8  9  10 11 12
		 * Diamonds:13 14 15 16 17 18 19 20 21 22 23 24 25
		 * Hearts:	26 27 28 29 30 31 32 33 34 35 36 37 38 
		 * Spades:	39 40 41 42 43 44 45 46 47 48 49 50 51
		 */
		for (int i=0, j=2; j<=14; i++, j++)
		{
			if (j <= 9) // Suits 2 through 9
			{
				deck.get(i).setCard(Integer.toString(j) + "C");
				deck.get(i+13).setCard(Integer.toString(j) + "D");
				deck.get(i+26).setCard(Integer.toString(j) + "H");
				deck.get(i+39).setCard(Integer.toString(j) + "S");
			}
			else if (j == 10)
			{
				deck.get(i).setCard("TC");
				deck.get(i+13).setCard("TD");
				deck.get(i+26).setCard("TH");
				deck.get(i+39).setCard("TS");
			}
			else if (j == 11) // Jack
			{
				deck.get(i).setCard("JC");
				deck.get(i+13).setCard("JD");
				deck.get(i+26).setCard("JH");
				deck.get(i+39).setCard("JS");
			}
			else if (j == 12) // Queen
			{
				deck.get(i).setCard("QC");
				deck.get(i+13).setCard("QD");
				deck.get(i+26).setCard("QH");
				deck.get(i+39).setCard("QS");
			}
			else if (j == 13) // King
			{
				deck.get(i).setCard("KC");
				deck.get(i+13).setCard("KD");
				deck.get(i+26).setCard("KH");
				deck.get(i+39).setCard("KS");
			}
			else // Ace
			{
				deck.get(i).setCard("AC");
				deck.get(i+13).setCard("AD");
				deck.get(i+26).setCard("AH");
				deck.get(i+39).setCard("AS");
			}
		}
		
		// Shuffle the deck twice for good measure 
		Collections.shuffle(deck);
		Collections.shuffle(deck);	
	}
	
	/**
	 * Distributes the necessary amount of cards to each player
	 * 
	 * @param players
	 */
	public void distribute(Vector<Player> players)
	{
		// Determine how many cards each player receives 
		int numOfCards = 0;
		if (players.size() == 2)
			numOfCards = 10;
		else if (players.size() == 3 || players.size() == 4)
			numOfCards = 7;
		else 
			numOfCards = 6;
		
		// Distribute cards alternating among each player
		for (int i=0; i<numOfCards; i++)
			for (int j=0; j<players.size(); j++)
				players.get(j).getHand().add(drawCard());
		
		// Take top card from deck and add it to the discard pile
		discardPile.add(drawCard());
	}

	/**
	 * Removes first card from deck and returns it to caller
	 * 
	 * @return card
	 */
	public Card drawCard()
	{
		checkDeckSize();
		Card c = deck.get(0);
		deck.remove(0);
		return c;
	}
	
	/**
	 * Removes first card from discard list and returns it to caller
	 * 
	 * @return card
	 */
	public Card drawCardFromDiscard()
	{
		if (discardPile.size() > 0)
		{
			Card card = discardPile.get(0);
			discardPile.remove(0);
			return card;
		}
		return null;
	}
	
	/**
	 * Checks to see if the deck has run out of cards. If so,
	 * all but the top card of the discard pile are shuffled 
	 * and added to the deck.
	 */
	private void checkDeckSize()
	{
		if (deck.size() == 0)
		{
			deck = discardPile;
			discardPile = new Vector<Card>();
			discardPile.add(deck.get(0));
			deck.remove(0);
			
			// Shuffle the deck twice for good measure 
			Collections.shuffle(deck);
			Collections.shuffle(deck);	
		}
	}
	
	/**
	 * Add the card to the top of the discard pile
	 * 
	 * @param card
	 */
	public void addToDiscardPile(Card card)
	{
		discardPile.add(0, card);
	}
	
	/**
	 * @return top of the discard pile
	 */
	public String getTopDiscardCard()
	{
		return discardPile.get(0).getCard();
	}
	
	/**
	 * @return list of melds for the deck
	 */
	public Vector<Meld> getMelds()
	{
		return melds;
	}
	
	
	
	public JPanel updateGUIMelds()
	{
		// Calculate the size of rows and columns 
		int rows = 0;
		int cols;
		
		// Max of 4 columns 
		if (melds.size() < 4)
			cols = melds.size();
		else 
			cols = 4;
		
		for (int i=0; i<melds.size(); i++)
			if (i%4 == 0)
				rows++;
		
		// Add for any extra melds
		if (melds.size() % 4 != 0)
			rows++;
		
		JPanel panel = new JPanel(new GridLayout(rows, cols));
		
		// Create a JComboBox for each meld
		for (int i=0; i<melds.size(); i++)
		{
			JComboBox jcBox = new JComboBox(melds.get(i).getCards());
			jcBox.setSelectedIndex(0);
			panel.add(jcBox);
		}
	
		return panel;
	}
} 






