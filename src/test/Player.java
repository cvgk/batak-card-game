package test;
import java.util.*;
public class Player {
    private int playerNo;
    public Card[] cards = new Card[13];
    static String koz= "";
    private List<Card> list=new ArrayList<>();
    static Card current,takes;
    static boolean kozCiktimi;
    private int score;
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
