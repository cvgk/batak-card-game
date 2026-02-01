package test;
import java.util.*;
public class Test {
    public static void main(String[] args)
    {
        DeckOfCards cards = new DeckOfCards();
        Player[] players = {new Player(1),new Player(2),
                new Player(3),new Player(4)};
        int x=0,y=0;
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

        int teklif = 4;
        Scanner scanner = new Scanner(System.in);
        int i=0,j=0;
        while(i<4){
            System.out.print(players[i] + " teklifiniz:");
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
                        j=i;
                }
            }
            i++;
        }


        System.out.println("teklif "+teklif);
        System.out.println("oyuna player "+(j+1)+" baslayacak ve kozu seçecek");
        System.out.print("koz: ");
        String koz = scanner.nextLine();
        Player.koz = koz;

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
                    int selection = scanner.nextInt();
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
                    int selection = scanner.nextInt();
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
                    int selection = scanner.nextInt();
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
}
