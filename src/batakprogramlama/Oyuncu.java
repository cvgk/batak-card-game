package batakprogramlama;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects; // Kart silme için eklendi

public class Oyuncu {
    private String isim;
    private List<Kart> el;
    private int aldigiEller;
    private int taahhut = 0; // İhalede söylediği sayı (varsayılan 0)

    public Oyuncu(String isim) {
        this.isim = isim;
        this.el = new ArrayList<>();
        this.aldigiEller = 0;
    }

    // --- GETTER/SETTER Metotları ---
    public String getIsim() { return isim; }
    public List<Kart> getEl() { return new ArrayList<>(el); } // Dışarıya elin kopyasını ver
    public int getAldigiElSayisi() { return aldigiEller; }

    // >>> BU METOT VAR MI? <<<
    public int getTaahhut() { return taahhut; }

    public void setTaahhut(int taahhut) { this.taahhut = taahhut; }


    // --- El Yönetimi Metotları ---
    public void kartAl(Kart kart) {
        if (kart != null) {
            this.el.add(kart);
        }
    }

    public boolean kartOyna(Kart kart) {
        boolean removed = this.el.remove(kart);
        if (!removed) {
            System.err.println("HATA: " + isim + " elinden " + kart + " çıkarılamadı!");
        }
        return removed;
    }

    public void eliniTemizle() {
        this.el.clear();
        this.aldigiEller = 0;
        this.taahhut = 0;
    }

    // --- El Sayacı Metotları ---
    public void aldigiElArtir() {
        this.aldigiEller++;
    }

    public void aldigiElSayisiniSifirla() {
        this.aldigiEller = 0;
    }

    // --- Gösterim Metotları ---
    public void eliniGoster() {
        List<Kart> gosterilecekEl = new ArrayList<>(this.el);
        Collections.sort(gosterilecekEl);

        System.out.println("--- " + this.isim + " ("+ gosterilecekEl.size() +" kart) ---");
        if (gosterilecekEl.isEmpty()) { System.out.println("  (Boş)"); }
        else { for (Kart kart : gosterilecekEl) { System.out.println("  " + kart); } }
        System.out.println("----------------------");
    }

    // >>> BU METOT VAR MI VE ADI DOĞRU MU? <<<
    public void eliniGosterNumarali() {
        List<Kart> gosterilecekEl = new ArrayList<>(this.el);
        Collections.sort(gosterilecekEl);

        System.out.println("--- " + this.isim + " Eli ("+ gosterilecekEl.size() +" kart) ---");
        if (gosterilecekEl.isEmpty()) { System.out.println("  (Boş)"); }
        else {
            for (int i = 0; i < gosterilecekEl.size(); i++) {
                System.out.println("  " + (i + 1) + ": " + gosterilecekEl.get(i));
            }
        }
        System.out.println("----------------------");
    }

    /* Kart sınıfı için ÖRNEK equals ve hashCode (Kart.java içinde olmalı)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kart kart = (Kart) o;
        return tip == kart.tip && deger == kart.deger;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tip, deger);
    }
    */
}


