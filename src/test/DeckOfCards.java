package test;
import java.util.*;
public class DeckOfCards {
    public static String[] suites = {"clubs","diamonds","hearts","spades"};
    public static String[] ranks ={"two","three","four","five","six","seven",
            "eight","nine","ten","jack","queen","king","ace"};

    public Card[] cards = new Card[52];

    public DeckOfCards(){
        int count =0;
        for(int i=0;i<suites.length;i++)
        {
            for(int j=0;j<ranks.length;j++)
            {
                cards[count++] = new Card(suites[i],ranks[j]);
            }
        }
        shuffle();

    }
    public void shuffle()
    {
        Random random = new Random();
        for(int i=0;i< cards.length;i++){
            int x = random.nextInt(cards.length);
            Card card = cards[x];
            cards[x] = cards[i];
            cards[i] = card;

        }
    }
}
