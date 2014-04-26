/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		April 2014
 * 		CS 342 Software Design
 * 
 * ------------------------------------------------------------------------*/
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Meld 
{
	public boolean valid;
	private char name;
	private int type;	// 0 = run, 1 = set
	private Vector<String> cards;
	
	/**
	 * Constructor is used to create a new meld. All error checking 
	 * is handled internally and the valid variable is set to true if
	 * the input creates a valid new meld
	 * 
	 * @param newCards	Cards to create the meld
	 * @param meldsSize	Size of melds list used for name generation
	 */
	public Meld(Vector<Card> newCards, int meldsSize)
	{				
		cards = new Vector<String>();
		
		// Convert cards to strings - note they are already sorted by rank
		for (int i=0; i< newCards.size(); i++)
			cards.add(newCards.get(i).getCard());
					
		if (newCards.size() >= 3 && validNewRun(cards) == true)
		{
			type = 0;
			valid = true;
			name = (char)(97 + meldsSize);
			cards.add(Character.toString(name));	// Add name to front (For GUI)
		}
		else if (newCards.size() >= 3 && validNewSet(cards) == true)
		{
			type = 1;
			valid = true;
			name = (char)(97 + meldsSize);
		}
		else
			valid = false;
	}
	
	/**
	 * Constructor is used to lay off cards to a meld. All error
	 * checking is handled internally and the valid variable is 
	 * set to true if the input added still makes a meld valid.
	 * 
	 * @param newCards
	 * @param meld		Old meld that will have cards added to it
	 */
	public Meld(Vector<Card> newCards, Meld meld)
	{
		// Copy list over
		cards = new Vector<String>();
		Collections.copy(meld.cards, cards);	// ****TO DO**** Make sure this works right
		
		// Add new cards to the list
		for (int i=0; i<newCards.size(); i++)
			cards.add(newCards.get(i).getCard());
		
		// Remove the name for sorting
		char tempName = cards.get(0).charAt(0);
		cards.remove(0);
		sortCards();	// Sort the new list
		
		// Check so see if the list is valid
		if (meld.type == 0 && validNewRun(cards) == true)
		{
			valid = true;
			cards.add(0, Character.toString(tempName));	// Add the name back
			name = tempName;
			type = 0;
		}
		else if (meld.type == 1 && validNewSet(cards) == true)
		{
			valid = true;
			cards.add(0, Character.toString(tempName));	// Add the name back
			name = tempName;
			type = 1;
		}
		else
			valid = false;
	}
	
	/**
	 * Checks to see if a list of cards make a run. A run is a list
	 * of three or more cards of the same suit in sequence.
	 * 
	 *  Example: 3H 4H 5H
	 * 
	 * @param newCards
	 * @return true/false
	 */
	private boolean validNewRun(Vector<String> newCards)
	{
		char testSuit = newCards.get(0).charAt(1);
		
		for (int i=1; i<newCards.size(); i++)
		{
			// If a card has a different suit
			if (newCards.get(i).charAt(1) != testSuit)
				return false;
			
			// Make sure cards are in sequence
			if (Integer.parseInt(newCards.get(i)) != (Integer.parseInt(newCards.get(i-1)) +1))
				return false;
		}
		return true;
	}
	
	/**
	 * Checks to see if a list of cards make a set. A set is a list
	 * of three or four cards of the same rank and different suits.
	 * 
	 * Example: AH AC AD
	 * 
	 * @param newCards = list to be checked
	 * @return	true/false
	 */
	private boolean validNewSet(Vector<String> newCards)
	{
		char testRank = newCards.get(0).charAt(0);
		
		// Check to see if all of the ranks are the same
		for (int i=1; i<newCards.size(); i++)
			if (newCards.get(i).charAt(0) != testRank)
				return false;
		
		return true;
	}
			
	/**
	 * @return the name/ID of the meld
	 */
	public char getName()
	{
		return name;
	}
	
	/**
	 * Sorts a list of cards by rank. 
	 */
	private void sortCards()
	{
		Collections.sort(cards, new Comparator<String>()
		{
			@Override
			public int compare (String s1, String s2)
			{
				int s1Val = Integer.parseInt(s1);
				int s2Val = Integer.parseInt(s2);
				
				if (s1Val > s2Val)
					return 1;
				else if (s1Val < s2Val)
					return -1;
				else 
					return 0;
			}
		});
	}
	
	
	
	
	
	public void updateGUIMelds()
	{
		
	}
	
	
		
}
