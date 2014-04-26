/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		April 2014
 * 		CS 342 Software Design
 * 
 * A card is a 2 character string with index 0 representing the rank of the
 * card and index 1 representing the suit of the card.
 * ------------------------------------------------------------------------*/

public class Card
{ 
	private String card;
	
	public Card()
	{
		card = null;
	}
		
	public Card(String card)
	{
		this.card = card;
	}
	
	public void setCard(String s)
	{
		card = s;
	}
	
	public String getCard()
	{
		return card;
	}
	
	public char getRank()
	{
		return card.charAt(0);
	}
	
	public char getSuit()
	{
		return card.charAt(1);
	}   
	
	public int getNumericRank() 
	{
		char rank = getRank();
		switch (rank) 
		{
			case 'K': return 13;
			case 'Q': return 12;
			case 'J': return 11;
			case 'T': return 10;
			case 'A': return 1;
			default: return Character.getNumericValue(rank);
		}
	}
}

