/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		April 2014
 * 		CS 342 Software Design
 * 
 * ------------------------------------------------------------------------*/
import java.util.*;

public class GameHandle 
{
	private Vector<Player> players;
	private int turn;
	private Deck deck;
	
	public GameHandle(int nPlayers)
	{
		players = new Vector<Player>();
		deck = new Deck();
		turn = 0;
		
		for (int i=0; i<nPlayers; i++)
			players.add(new Player());
		
		deck.distribute(players);
		sortlist(players.get(0).getHand());
		
	}
	
	
	/**
	 * Sorts a list of cards by rank. 
	 * 
	 * @param cards
	 */
	public void sortlist(Vector<Card> cards)
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
	
	

}
