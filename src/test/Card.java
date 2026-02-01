package test;


public class Card implements Comparable<Card>{
    private String suite;
    private String rank;
    int val1,val2;

    public Card() { }

    public Card(String suite,String rank){
        this.rank = rank;
        this.suite = suite;

        switch (suite){
            case "clubs": val1 = 1; break;
            case "diamonds": val1 = 2; break;
            case "hearts": val1= 3; break;
            case "spades": val1= 4; break;
            default: val1= -1;
        }

        switch (rank){
            case "two": val2 = 2; break;
            case "three": val2= 3; break;
            case "four": val2=4; break;
            case "five": val2=5; break;
            case "six": val2 = 6; break;
            case "seven": val2= 7; break;
            case "eight": val2=8; break;
            case "nine": val2=9; break;
            case "ten": val2 = 10; break;
            case "jack": val2= 11; break;
            case "queen": val2=12; break;
            case "king": val2=13; break;
            case "ace": val2= 14; break;
            default: val2=-1;
        }
    }

    public void setSuite(String suite)
    {
        this.suite = suite;
    }
    public String getSuite() { return suite;}

    public void setRank(String rank){
        this.rank = rank;
    }
    public String getRank() { return rank;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Card other = (Card) obj;
        return this.suite.equals(other.suite) && this.rank.equals(other.rank);
    }

    @Override
    public int hashCode() {
        return suite.hashCode() * 31 + rank.hashCode();
    }


    public String toString()
    {
        return rank + " of "+ suite;
    }

    @Override
    public int compareTo(Card card) {
        if((this.val1==card.val1))
            return this.val2-card.val2;
        return this.val1-card.val1;

    }
}
