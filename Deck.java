package Rummy;
import java.util.ArrayList;
import java.util.Random;

public class Deck {
	private ArrayList<Card> cards;


	Deck()
	{
		cards = new ArrayList<Card>();
		int ind1, ind2;
		Random generator = new Random();
		Card temp;

		for (int a=0; a<=3; a++)
		{
			for (int b=0; b<=12; b++)
			 {
			   cards.add(new Card(a,b));

			 }
		}

		System.out.println("The deck is being shuffled...");
		for(int i = 0; i< 100; i++)
		{
			ind1 = generator.nextInt( cards.size() - 1 );
			ind2 = generator.nextInt( cards.size() - 1 );

			temp = (Card) cards.get( ind2 );
			cards.set( ind2 , cards.get( ind1 ) );
			cards.set( ind1, temp );
		}

	}

	public Card drawFromDeck()
	{
		return cards.remove(0);
	}

	public int getTotalCards()
	{
		return cards.size();
	}
} 