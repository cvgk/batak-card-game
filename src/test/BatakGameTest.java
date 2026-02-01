package test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Batak oyunu için JUnit testleri
 *
 * Kullanım:
 * 1. JUnit kütüphanesini projenize ekleyin (Maven/Gradle veya manuel)
 * 2. Bu dosyayı projenize ekleyin
 * 3. IDE'nizde "Run Tests" deyin veya komut satırında çalıştırın
 */
public class BatakGameTest {

    private Player player;

    @Before
    public void setUp() {
        // Her testten önce çalışır
        player = new Player(1);
        Player.kozCiktimi = false;
        Player.koz = "hearts"; // Kupa
    }

    // ==================== TEST 1: Koz Çıkmadan Koz Oynama ====================
    @Test
    public void testCZero_KozCikmadan_KozOynayamaz() {
        // Hazırlık
        player.cards = new Card[]{
                new Card("spades", "seven"),    // Maça 7
                new Card("spades", "ten"),      // Maça 10
                new Card("hearts", "ace"),      // Kupa As (KOZ)
                new Card("clubs", "nine")       // Sinek 9
        };
        Player.kozCiktimi = false;
        Player.koz = "hearts";

        // Çalıştır
        List<Card> validCards = player.cZero();

        // Kontrol et
        assertEquals("Koz çıkmadan 3 kart oynayabilmeli", 3, validCards.size());

        // Koz olmamalı
        for (Card card : validCards) {
            assertFalse("Koz kart olmamalı", card.getSuite().equals("hearts"));
        }
    }

    // ==================== TEST 2: Sadece Koz Varsa Mecburen Oyna ====================
    @Test
    public void testCOne_SadeceKoz_MecburenOyna() {
        // Hazırlık - Oyuncunun tüm kartları koz
        player.cards = new Card[]{
                new Card("hearts", "seven"),
                new Card("hearts", "ten"),
                new Card("hearts", "ace"),
                new Card("hearts", "queen")
        };
        Player.kozCiktimi = false;
        Player.koz = "hearts";

        // Çalıştır - cZero boş dönmeli
        List<Card> cZeroResult = player.cZero();
        assertEquals("cZero boş dönmeli (sadece koz var)", 0, cZeroResult.size());

        // cOne tüm kartları dönmeli
        player.clear();
        List<Card> cOneResult = player.cOne();
        assertEquals("cOne tüm kartları dönmeli", 4, cOneResult.size());
    }

    // ==================== TEST 3: Koz Çıktıktan Sonra Serbest ====================
    @Test
    public void testCZero_KozCiktiktan_HerSeyOynayabilir() {
        // Hazırlık
        player.cards = new Card[]{
                new Card("spades", "seven"),
                new Card("hearts", "ten"),
                new Card("clubs", "ace")
        };
        Player.kozCiktimi = true; // Koz çıktı
        Player.koz = "hearts";

        // Çalıştır
        List<Card> validCards = player.cZero();

        // Kontrol et
        assertEquals("Koz çıktıktan sonra tüm kartlar oynanabilir", 3, validCards.size());
    }

    // ==================== TEST 4: Büyük Kart Varsa Oyna ====================
    @Test
    public void testCOne_BuyukKartVarsa() {
        // Hazırlık
        player.cards = new Card[]{
                new Card("spades", "ten"),      // Maça 10 (BÜYÜK)
                new Card("spades", "five"),     // Maça 5
                new Card("hearts", "ace"),      // Kupa As
                new Card("clubs", "nine")       // Sinek 9
        };
        Player.current = new Card("spades", "seven"); // İlk kart Maça 7
        Player.koz = "hearts";

        // Çalıştır
        player.clear();
        List<Card> validCards = player.cOne();

        // Kontrol et
        assertTrue("Liste boş olmamalı", validCards.size() > 0);

        // İlk dönen kart Maça 10 olmalı (büyük)
        boolean hasBigger = false;
        for (Card card : validCards) {
            if (card.getSuite().equals("spades") && card.val2 > 7) {
                hasBigger = true;
                break;
            }
        }
        assertTrue("Büyük kart (Maça 10) listede olmalı", hasBigger);
    }

    // ==================== TEST 5: Büyük Yoksa Küçük Oyna ====================
    @Test
    public void testCOne_BuyukYoksa_KucukOyna() {
        // Hazırlık
        player.cards = new Card[]{
                new Card("spades", "five"),     // Maça 5 (KÜÇÜK)
                new Card("spades", "three"),    // Maça 3 (KÜÇÜK)
                new Card("hearts", "ace"),      // Kupa As
                new Card("clubs", "nine")       // Sinek 9
        };
        Player.current = new Card("spades", "ten"); // İlk kart Maça 10
        Player.koz = "hearts";

        // Çalıştır
        player.clear();
        List<Card> validCards = player.cOne();

        // Kontrol et
        assertTrue("Liste boş olmamalı", validCards.size() > 0);

        // Sadece Maça kartları olmalı (küçükler)
        for (Card card : validCards) {
            if (!card.getSuite().equals("hearts")) { // Koz değilse
                assertEquals("Aynı tipten (Maça) olmalı", "spades", card.getSuite());
                assertTrue("Küçük olmalı", card.val2 < 10);
            }
        }
    }

    // ==================== TEST 6: Aynı Tip Yoksa Koz At ====================
    @Test
    public void testCOne_AyniTipYoksa_KozAt() {
        // Hazırlık - Oyuncuda Maça yok
        player.cards = new Card[]{
                new Card("hearts", "ace"),      // Kupa As (KOZ)
                new Card("hearts", "ten"),      // Kupa 10 (KOZ)
                new Card("clubs", "nine"),      // Sinek 9
                new Card("diamonds", "king")    // Karo Papaz
        };
        Player.current = new Card("spades", "seven"); // İlk kart Maça
        Player.koz = "hearts";
        Player.kozCiktimi = true; // Koz çıktı

        // Çalıştır
        player.clear();
        List<Card> validCards = player.cOne();

        // Kontrol et
        assertTrue("Liste boş olmamalı", validCards.size() > 0);

        // Koz kartları olmalı
        boolean hasKoz = false;
        for (Card card : validCards) {
            if (card.getSuite().equals("hearts")) {
                hasKoz = true;
                break;
            }
        }
        assertTrue("Koz kartları listede olmalı", hasKoz);
    }

    // ==================== TEST 7: El Alma - Aynı Tip En Büyük ====================
    @Test
    public void testCritics_AyniTip_EnBuyukKazanir() {
        // Hazırlık
        Player.takes = new Card("spades", "seven");  // Maça 7 önde
        Player.current = new Card("spades", "ten");  // Maça 10 geldi

        // Çalıştır
        player.critics();

        // Kontrol et
        assertEquals("Maça 10 almalı", "ten", Player.takes.getRank());
        assertEquals("Maça olmalı", "spades", Player.takes.getSuite());
    }

    // ==================== TEST 8: El Alma - Koz Kazanır ====================
    @Test
    public void testCritics_Koz_Kazanir() {
        // Hazırlık
        Player.takes = new Card("spades", "ace");    // Maça As önde
        Player.current = new Card("hearts", "two");  // Kupa 2 geldi (KOZ)
        Player.koz = "hearts";

        // Çalıştır
        player.critics();

        // Kontrol et
        assertEquals("Kupa 2 almalı (koz)", "two", Player.takes.getRank());
        assertEquals("Kupa olmalı", "hearts", Player.takes.getSuite());
    }

    // ==================== TEST 9: El Alma - En Büyük Koz Kazanır ====================
    @Test
    public void testDCritics_EnBuyukKoz_Kazanir() {
        // Hazırlık
        Player.takes = new Card("hearts", "seven");  // Kupa 7 önde
        Player.current = new Card("hearts", "ace");  // Kupa As geldi
        Player.koz = "hearts";

        // Çalıştır
        player.dcritics();

        // Kontrol et
        assertEquals("Kupa As almalı", "ace", Player.takes.getRank());
    }

    // ==================== TEST 10: Kart Karşılaştırma ====================
    @Test
    public void testCardComparison_Sorting() {
        // Hazırlık
        Card[] cards = {
                new Card("hearts", "seven"),
                new Card("hearts", "ace"),
                new Card("clubs", "queen"),
                new Card("hearts", "two")
        };

        // Sırala
        java.util.Arrays.sort(cards);

        // Kontrol et - clubs < hearts, ve içinde küçükten büyüğe
        assertEquals("İlk kart Sinek Kız olmalı", "clubs", cards[0].getSuite());
        assertEquals("İkinci kart Kupa 2 olmalı", "two", cards[1].getRank());
        assertEquals("Son kart Kupa As olmalı", "ace", cards[3].getRank());
    }

    // ==================== TEST 11: RemoveCard Fonksiyonu ====================
    @Test
    public void testRemoveCard() {
        // Hazırlık
        player.cards = new Card[]{
                new Card("spades", "seven"),
                new Card("hearts", "ten"),
                new Card("clubs", "ace")
        };
        Player.current = new Card("hearts", "ten");

        // Çalıştır
        int initialSize = player.cards.length;
        player.removeCZero();

        // Kontrol et
        assertEquals("Bir kart azalmalı", initialSize - 1, player.cards.length);

        // Kupa 10 olmamalı
        for (Card card : player.cards) {
            assertFalse("Çıkarılan kart olmamalı",
                    card.getSuite().equals("hearts") && card.getRank().equals("ten"));
        }
    }

    // ==================== TEST 12: cAllKoz Kontrol ====================
    @Test
    public void testCAllKoz_TumKartlarKoz() {
        // Hazırlık - Tüm kartlar koz
        player.cards = new Card[]{
                new Card("hearts", "seven"),
                new Card("hearts", "ten"),
                new Card("hearts", "ace")
        };
        Player.koz = "hearts";

        // Kontrol et
        assertTrue("Tüm kartlar koz", player.cAllKoz());
    }

    @Test
    public void testCAllKoz_KozOlmayanVar() {
        // Hazırlık - Koz olmayan var
        player.cards = new Card[]{
                new Card("hearts", "seven"),
                new Card("spades", "ten"),      // Koz değil
                new Card("hearts", "ace")
        };
        Player.koz = "hearts";

        // Kontrol et
        assertFalse("Koz olmayan var", player.cAllKoz());
    }

    // ==================== TEST 13: Deck Oluşturma ====================
    @Test
    public void testDeckCreation() {
        DeckOfCards deck = new DeckOfCards();

        // Kontrol et
        assertEquals("52 kart olmalı", 52, deck.cards.length);

        // Null kontrolü
        for (Card card : deck.cards) {
            assertNotNull("Kart null olmamalı", card);
            assertNotNull("Takım null olmamalı", card.getSuite());
            assertNotNull("Değer null olmamalı", card.getRank());
        }
    }

    // ==================== TEST 14: Player Score ====================
    @Test
    public void testPlayerScore() {
        Player p = new Player(1);

        assertEquals("Başlangıç skoru 0", 0, p.getScore());

        p.setScore(5);
        assertEquals("Skor 5 olmalı", 5, p.getScore());

        p.setScore(p.getScore() + 1);
        assertEquals("Skor 6 olmalı", 6, p.getScore());
    }
}
