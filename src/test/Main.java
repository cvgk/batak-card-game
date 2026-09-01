/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

import java.util.*;
public class Main
{
	public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in);
		int teklif = 4;
        Player[] players = null;
        for(int game=0;game<5;game++){
        int x=0,y=0;
        DeckOfCards cards = new DeckOfCards();
        players =new Player[] {new Player(1),new Player(2),new Player(3),new Player(4)};
        while(x<13) {
            for (int i = 0; i < players.length; i++) {
                players[i].cards[x] = cards.cards[y++];
            }
            x++;
        }
        for(int playerNo=0;playerNo<players.length;playerNo++)
           Arrays.sort(players[playerNo].cards);
        for (int playerNo = 0; playerNo < 4; playerNo++) {
            players[playerNo].print();

        }

        
        int i=0,j=0,k=game%4;
        while(i<4){
            k=k%4;
            System.out.print(players[k] + " teklifiniz:");
            String t = scanner.nextLine();
            if (!t.equals("pas")) {
                int a = Integer.parseInt(t);
                if (a <= 4 || 13 < a||a<=teklif) {
                    System.out.println("geçersiz teklif");
                    continue;
                }
                else {
                    if (teklif < a)
                        teklif = a;
                        j=k;
                        players[k].teklifYapıldımı = true;
                        Player.count++;
                }
            }
            i++;
            k++;
        }
        out:
        do{
        for(int playerNo=0;playerNo<players.length;playerNo++)
        {
			if(Player.count==0) break out;
            if(players[playerNo].teklifYapıldımı)
            {
                if(tr(players)) break out;
                System.out.print(players[playerNo]+" lütfen teklif yapınız:");
                String   s = scanner.nextLine();
                
                if(!s.equals("pas"))
                {
                       int yeniTeklif = Integer.parseInt(s);
                       while(yeniTeklif<=teklif){
                       System.out.print(players[playerNo]+" lütfen teklif yapınız");
                       s=scanner.nextLine();
                       yeniTeklif = Integer.valueOf(s);
                       }
                       teklif = yeniTeklif;
					   j = playerNo;
                }
                else{
                    players[playerNo].teklifYapıldımı = false;
                    Player.count--;
                }
                
                    
            }
        }
        }while(Player.count>=0);
        System.out.println("teklif "+teklif);
        System.out.println("oyuna player "+(j+1)+" baslayacak ve kozu seçecek");
        System.out.print("koz: ");
        String koz = scanner.nextLine();
        Player.koz = koz;
        players[j].oyunaBaslayan = true;
        int count =0;
        i=0;
        while(count<13){
            int c=0;
            Card[] inTheMiddle = new Card[4];
            HashMap<Card,Player> map = new HashMap<>();

            for(int playerNo=j;playerNo<j+4;playerNo++)
            {

                if(c==0){
                    List<Card> listOfChosen = players[playerNo%4].cZero();
                    System.out.println(players[playerNo%4]+":"+listOfChosen);
                    System.out.print("choose a card: ");
                    int selection = Integer.parseInt(scanner.nextLine().trim());
                    Card currentCard = listOfChosen.get(selection-1);
                    Player.current = currentCard;
                    Player.takes = currentCard;
                    map.put(currentCard,players[playerNo%4]);
                    inTheMiddle[c] = currentCard;
                    players[playerNo%4].removeCZero();
                    players[playerNo%4].clear();

                }else if(c==1){
                    List<Card> listOfChosen = players[playerNo%4].cOne();
                    System.out.println(players[playerNo%4]+":"+listOfChosen);
                    System.out.print("choose a card: ");
                    int selection = Integer.parseInt(scanner.nextLine().trim());
                    Card currentCard = listOfChosen.get(selection-1);
                    map.put(currentCard,players[playerNo%4]);
                    Player.current = currentCard;
                    inTheMiddle[c] = currentCard;
                    players[playerNo%4].critics();
                    players[playerNo%4].removeCZero();
                    players[playerNo%4].clear();
                    System.out.println("current "+Player.current);
                    System.out.println("takes "+Player.takes);
                }else{
                    List<Card> listOfChosen = players[playerNo%4].dOne(inTheMiddle);
                    System.out.println(players[playerNo%4]+":"+listOfChosen);
                    System.out.print("choose a card: ");
                    int selection = Integer.parseInt(scanner.nextLine().trim());
                    Card currentCard = listOfChosen.get(selection-1);
                    map.put(currentCard,players[playerNo%4]);
                    Player.current = currentCard;
                    inTheMiddle[c] = currentCard;
                    players[playerNo%4].dcritics();
                    players[playerNo%4].removeCZero();
                    players[playerNo%4].clear();
                    System.out.println("current "+Player.current);
                    System.out.println("takes "+Player.takes);
                }
                c++;

            }
            i = j+rtu(Player.takes,inTheMiddle);
            j=i;
            count++;
            Player player =map.get(Player.takes);
            int score = player.getScore() +1;
            player.setScore(score);
            System.out.println("------------------------------------------------");
            map.clear();

        }
        }
		for(Player player:players){
            if(player.oyunaBaslayan)
            {
                int score = player.getScore()>= teklif ? player.getScore() : -teklif;
                player.setScore(score);
            }else{
                int score = player.getScore() == 0 ? -teklif : player.getScore();
                player.setScore(score);
            }
        }
        for(Player player:players)
            System.out.println(player+": "+player.getScore());
	}
	public static int rtu(Card takes,Card[] cards){
        for(int i=0;i<cards.length;i++)
        {
            if(cards[i]==takes){
                return i;
            }
        }
        return -1;
    }
    static boolean tr(Player[] players)
    {
        int count = 0;
        for(Player player:players){
            if(player.teklifYapıldımı) count++;
        }
        if(count==1)  return true;
        return false;
    }
}
class Card implements Comparable<Card>{
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
class DeckOfCards {
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
class Player {
    private int playerNo;
    public Card[] cards = new Card[13];
    static String koz= "";
    private List<Card> list=new ArrayList<>();
    static Card current,takes;
    static boolean kozCiktimi;
    private int score;
    boolean teklifYapıldımı;
	boolean oyunaBaslayan;
    static int count;
    public Player() { }
    public Player(int playerNo){
        this.playerNo = playerNo;

    }
    public void setScore(int score) { this.score = score;}
    public int getScore() { return score; }
    public void setPlayerNo(int playerNo){ this.playerNo = playerNo;}
    public int getPlayerNo(){ return playerNo;}
    public void print(){
        System.out.print(this+": ");
        for(int i=0;i< cards.length;i++)
        {
            if(i==0){
                System.out.print("["+cards[i]+",");
            }else if(i== cards.length-1){
                System.out.print(cards[i]+"]");
            }else{
                System.out.print(cards[i]+",");
            }
        }
        System.out.println();
    }
    public List<Card> cZero(){
        if(cAllKoz()&&!kozCiktimi){
            list.addAll(Arrays.asList(cards));
            kozCiktimi = true;
        }else if(kozCiktimi){
            list.addAll(Arrays.asList(cards));
        }
        else {
            for (int i = 0; i < cards.length; i++) {
                if (!cards[i].getSuite().equals(koz)) {
                    list.add(cards[i]);
                }
            }
        }
        return list;
    }
    public List<Card> cOne(){

            cOneBigger();
            cOneSmaller();
            cOneKoz();
            cOneFree();

        return list;
    }
    //section 2
    public void critics(){
        if((takes.val1==current.val1&&takes.val2 < current.val2)){
            takes = current;
        }
        else if((current.getSuite().equals(koz))&&(!takes.getSuite().equals(koz))
                ){
            takes = current;
        }
    }
    public void dcritics(){
        if(takes.getSuite().equals(koz)&&current.getSuite().equals(koz)) {
            if ((takes.val1 == current.val1 && takes.val2 < current.val2)) {
                takes = current;
            }else if(!takes.getSuite().equals(koz)&&current.getSuite().equals(koz))
            {
                takes = current;
            }
        }else if(takes.val1== current.val1&&takes.val2<current.val2){
            takes = current;
        } else if (takes.val1!=current.val1) {
            if(current.getSuite().equals(koz)&&!takes.getSuite().equals(koz)){
                takes = current;
            }

        }
    }

    public void cOneFree()
    {
        if(list.isEmpty()) {
            for (int i = 0; i < cards.length; i++) {
                if (!cards[i].getSuite().equals(koz) || cards[i].val1 != current.val1) {
                    list.add(cards[i]);
                }
            }
        }

    }
    public void cOneKoz(){
        if(list.isEmpty()) {
            for (int i = 0; i < cards.length; i++) {
                if (cards[i].getSuite().equals(koz)) {
                    list.add(cards[i]);
                }
            }
            kozCiktimi = true;
        }

    }
    public boolean cAllKoz(){
        for(int i=0;i< cards.length;i++)
        {
            if(!cards[i].getSuite().equals(koz)){
                return false;
            }
        }
        return true;
    }
    public void cOneBigger() {
        if(list.isEmpty()) {
            for (int i = 0; i < cards.length; i++) {
                if (cards[i].val1 == current.val1 && current.val2 < cards[i].val2) {
                    list.add(cards[i]);
                }
            }
        }
    }
    public void cOneSmaller() {
        if(list.isEmpty()) {
            for (int i = 0; i < cards.length; i++) {
                if (cards[i].val1 == current.val1 && current.val2 > cards[i].val2) {
                    list.add(cards[i]);
                }
            }
        }
    }
    public boolean isKozOrtada(Card[] inTheMiddle)
    {
        for(int i=0;i<inTheMiddle.length;i++){
            if(inTheMiddle[i]!=null) {
                if (koz.equals(inTheMiddle[i].getSuite())) {
                    return true;
                }
            }
        }
        return false;
    }
    public void eOneBiggerSmaller(Card card)
    {
        if(list.isEmpty())
        {
            for(int i=0;i<cards.length;i++)
            {
                if(cards[i].val1==card.val1&&cards[i].val2<card.val2){
                    list.add(cards[i]);
                }
            }
            for(int i=0;i<cards.length;i++)
            {
                if(cards[i].val1==card.val1&&cards[i].val2>card.val2){
                    list.add(cards[i]);
                }
            }
        }
    }

    public List<Card> dOne(Card[] cv){
        if(kozCiktimi){
            if(isKozOrtada(cv)){
              Card card = cv[0];
              if(card.getSuite().equals(koz)){
                  System.out.println(12);
                  eOneBigger(takes);
                  eOnesmaller(takes);
                  cOneKoz();
                  cOneFree();
              }else{
              System.out.println(1);
              eOneBiggerSmaller(card);
              eOneBigger(takes);
              eOnesmaller(takes);
              cOneKoz();
              cOneFree();
              }
            }else{
                System.out.println(2);
                dOneBigger();
                dOneSmaller();
                cOneKoz();
                cOneFree();

            }
        }else{
            System.out.println(3);
            dOneBigger();
            dOneSmaller();
            cOneKoz();
            cOneFree();
        }
        return list;
    }
    public void eOneBigger(Card card){
        if(list.isEmpty()) {
            for (int i = 0; i < cards.length; i++) {
                if (card.val1 == cards[i].val1 && card.val2 < cards[i].val2) {
                    list.add(cards[i]);
                }
            }
        }
    }
    public void eOnesmaller(Card card){
        if(list.isEmpty()) {
            for (int i = 0; i < cards.length; i++) {
                if (card.val1 == cards[i].val1 && card.val2 > cards[i].val2) {
                    list.add(cards[i]);
                }
            }
        }
    }
    public void dOneBigger(){
        if(list.isEmpty()){
            for(int i=0;i< cards.length;i++){
                if(cards[i].val1==takes.val1&&takes.val2<cards[i].val2){
                    list.add(cards[i]);
                }
            }
        }
    }
    public void dOneSmaller(){
        if(list.isEmpty()){
            for(int i=0;i< cards.length;i++)
            {
                if(cards[i].val1==takes.val1&&takes.val2>cards[i].val2)
                {
                    list.add(cards[i]);
                }
            }
        }
    }
    public void removeCZero(){
        int length = cards.length-1;
        Card[] c = new Card[length];
        int x=0, y=0;
        while(x < length){
            // != yerine equals() kullan
            if(!cards[y].equals(current)){  // ← Düzeltme!
                c[x++] = cards[y];
            }
            y++;
        }
        cards = c;
    }
    public void clear() { list.clear();}

    public String toString() { return "player "+ playerNo; }
}