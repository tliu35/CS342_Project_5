package Rummy;

public class Card{ 
	private int rank, suit;
	
    private static String[] suits = { "C","D","H","S"};
	private static String[] ranks= { "2","3","4","5","6","7","8","9","T","J","Q","K","A"};

    public Card(int suit, int rank)
    {

        this.rank=rank;

        this.suit=suit;
    
    }

    public @Override String toString()
    {

          return ranks[rank] + suits[suit];
    }

    public int getRank() {

         return rank;

    }

    public int getSuit() {

        return suit;

    }
    
}//end of class

