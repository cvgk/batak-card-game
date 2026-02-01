package batakprogramlama;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deste {
    private List<Kart> kartlar;

    public Deste() {
        kartlar = new ArrayList<>();
        // Tip ve Deger enum'larının tanımlı olduğunu varsayıyoruz
        for (Tip tip : Tip.values()) {
            for (Deger deger : Deger.values()) {
                kartlar.add(new Kart(tip, deger));
            }
        }
        karistir();
    }

    public void karistir() {
        Collections.shuffle(kartlar, new Random());
    }

    // Tüm desteyi liste olarak almak için (basit dağıtım için)
    public List<Kart> getTumKartlar() {
        return new ArrayList<>(kartlar); // Güvenlik için kopyasını döndür
    }

    // Veya tek tek kart çekmek için
    public Kart kartCek() {
        if (kartlar.isEmpty()) {
            return null; // Deste boş
        }
        return kartlar.remove(0); // Listenin başından bir kart çeker
    }

    public boolean bosMu() {
        return kartlar.isEmpty();
    }

    public int kalanKartSayisi() {
        return kartlar.size();
    }
}