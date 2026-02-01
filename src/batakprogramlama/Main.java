package batakprogramlama;
import java.util.*; // List, Map, Scanner, ArrayList, HashMap, Collections için

public class Main {

    // =======================================================================
    // === YARDIMCI METOTLAR =================================================
    // =======================================================================

    /**
     * O anki oyuncu için geçerli (oynanabilir) kartların listesini döndürür.
     * Batak kurallarını uygular.
     * EN GÜNCEL VERSİYON (RENK TAKİP EDERKEN KOZ OYNANMIŞSA BÜYÜTME ZORUNLULUĞU KALKMASI KURALI DAHİL)
     *
     * @param oyuncu             Sırası gelen oyuncu.
     * @param istenenRenkYerdeki O elde takip edilmesi gereken renk (ilk kartın rengi), null ise oyuncu eli başlatıyordur.
     * @param koz                Oyunun kozu.
     * @param mevcutElKartlari   O ana kadar o elde yere atılmış kartlar (Oyuncu Index -> Kart).
     * @param kozCiktiMi         Bu turda daha önce koz oynanıp oynanmadığı.
     * @return Oyuncunun o an oynayabileceği geçerli kartların listesi.
     */
    public static List<Kart> getGecerliKartlar(Oyuncu oyuncu, Tip istenenRenkYerdeki, Tip koz,
                                               Map<Integer, Kart> mevcutElKartlari, boolean kozCiktiMi) {
        List<Kart> eli = oyuncu.getEl();
        List<Kart> gecerliKartlar = new ArrayList<>();

        // --- Durum 1: Oyuncu Eli Başlatıyor (istenenRenkYerdeki == null) ---
        if (istenenRenkYerdeki == null) {
            if (!kozCiktiMi) { // Koz henüz ÇIKMAMIŞSA
                List<Kart> kozOlmayanKartlarElde = new ArrayList<>();
                boolean sadeceKozVarElde = true;
                for (Kart k : eli) {
                    if (k.getTip() != koz) {
                        kozOlmayanKartlarElde.add(k);
                        sadeceKozVarElde = false;
                    }
                }
                if (!eli.isEmpty() && !sadeceKozVarElde) {
                    gecerliKartlar.addAll(kozOlmayanKartlarElde);
                } else {
                    gecerliKartlar.addAll(eli);
                }
            } else { // Koz zaten ÇIKMIŞSA
                gecerliKartlar.addAll(eli);
            }
            return gecerliKartlar;
        }

        // --- Durum 2: Oyuncu Renk Takip Ediyor (istenenRenkYerdeki != null) ---
        List<Kart> istenenRenkKartlariElde = new ArrayList<>();
        for (Kart k : eli) {
            if (k.getTip() == istenenRenkYerdeki) {
                istenenRenkKartlariElde.add(k);
            }
        }

        // --- Alt Durum 2.1: Elinde İstenen Renkten Kart VAR ---
        if (!istenenRenkKartlariElde.isEmpty()) {
            // Bu elde (mevcutElKartlari içinde) daha önce koz oynanmış mı diye bak.
            boolean buElKozIleKesilmis = false;
            for (Kart oynananKart : mevcutElKartlari.values()) {
                if (oynananKart != null && oynananKart.getTip() == koz) {
                    buElKozIleKesilmis = true;
                    break;
                }
            }

            // Eğer bu el daha önce bir koz ile kesilmişse VE istenen renk koz değilse,
            // oyuncu sadece rengi takip etmek zorundadır, büyütme zorunluluğu kalkar.
            if (buElKozIleKesilmis && istenenRenkYerdeki != koz) {
                gecerliKartlar.addAll(istenenRenkKartlariElde); // Elindeki TÜM istenen renk kartları geçerli
            }
            // Eğer istenen renk KOZ ise VEYA bu el henüz koz ile kesilmemişse,
            // "büyütme zorunluluğu" kuralı geçerlidir.
            else {
                Kart enYuksekOynananIstenenBuElde = null;
                for (Kart oynananKart : mevcutElKartlari.values()) {
                    if (oynananKart != null && oynananKart.getTip() == istenenRenkYerdeki) {
                        if (enYuksekOynananIstenenBuElde == null || oynananKart.compareTo(enYuksekOynananIstenenBuElde) > 0) {
                            enYuksekOynananIstenenBuElde = oynananKart;
                        }
                    }
                }

                List<Kart> dahaYuksekIstenenElde = new ArrayList<>();
                if (enYuksekOynananIstenenBuElde != null) {
                    for (Kart eldekiKart : istenenRenkKartlariElde) {
                        if (eldekiKart.compareTo(enYuksekOynananIstenenBuElde) > 0) {
                            dahaYuksekIstenenElde.add(eldekiKart);
                        }
                    }
                } else { // Yere henüz istenen renkten atılmamış
                    dahaYuksekIstenenElde.addAll(istenenRenkKartlariElde);
                }

                if (!dahaYuksekIstenenElde.isEmpty()) {
                    gecerliKartlar.addAll(dahaYuksekIstenenElde); // Büyütmek zorundasın
                } else {
                    gecerliKartlar.addAll(istenenRenkKartlariElde); // Büyütemiyorsan, o renkten herhangi birini at
                }
            }
            return gecerliKartlar;
        }
        // === Alt Durum 2.2: Elinde İstenen Renkten YOK ===
        else { // Oyuncunun elinde istenen renk yok.
            List<Kart> oyuncununKozlari = new ArrayList<>();
            List<Kart> oyuncununDigerRenkKartlari = new ArrayList<>();

            for (Kart k : eli) {
                if (k.getTip() == koz) {
                    oyuncununKozlari.add(k);
                } else {
                    oyuncununDigerRenkKartlari.add(k);
                }
            }

            // Kural: İstenen renk yoksa, elinde koz varsa KOZ OYNAMAK ZORUNDASIN.
            if (!oyuncununKozlari.isEmpty()) { // Elinde KOZ VAR
                Kart enYuksekOynananKozBuElde = null;
                for (Kart oynananKart : mevcutElKartlari.values()) {
                    if (oynananKart != null && oynananKart.getTip() == koz) {
                        if (enYuksekOynananKozBuElde == null || oynananKart.compareTo(enYuksekOynananKozBuElde) > 0) {
                            enYuksekOynananKozBuElde = oynananKart;
                        }
                    }
                }

                if (enYuksekOynananKozBuElde != null) { // Bu elde daha önce koz oynanmış
                    List<Kart> dahaYuksekKozElde = new ArrayList<>();
                    for (Kart eldekiKoz : oyuncununKozlari) {
                        if (eldekiKoz.compareTo(enYuksekOynananKozBuElde) > 0) {
                            dahaYuksekKozElde.add(eldekiKoz);
                        }
                    }
                    if (!dahaYuksekKozElde.isEmpty()) {
                        gecerliKartlar.addAll(dahaYuksekKozElde);
                    } else {
                        gecerliKartlar.addAll(oyuncununKozlari);
                    }
                } else { // Bu elde daha önce koz oynanmamış
                    gecerliKartlar.addAll(oyuncununKozlari);
                }
            } else { // Elinde KOZ YOK (istenen renk de yoktu)
                // Kural: Elindeki herhangi bir kartı (boşa) atabilirsin.
                // Yani oyuncununDigerRenkKartlari geçerlidir.
                if(!oyuncununDigerRenkKartlari.isEmpty()){
                    gecerliKartlar.addAll(oyuncununDigerRenkKartlari);
                } else if (!eli.isEmpty()) { // Diğer renk kartı da yok ama el hala doluysa (bu imkansız gibi, el sadece istenen renk olmalıydı)
                    System.err.println("UYARI (getGecerliKartlar): İstenen renk yok, koz yok, diğer renk yok ama el dolu? Bu durum olmamalı. Tüm el geçerli.");
                    gecerliKartlar.addAll(eli); // En son çare, tüm eli ver.
                }
                // Eli tamamen boşsa gecerliKartlar da boş kalır.
            }

            if (gecerliKartlar.isEmpty() && !eli.isEmpty()) {
                System.err.println("UYARI (getGecerliKartlar Son Çare - Alt Durum 2.2): Geçerli kart bulunamadı!");
                gecerliKartlar.addAll(eli);
            }
            return gecerliKartlar;
        }
    }


    /**
     * Oynanan kartlara göre eli kimin kazandığını belirler.
     */
    public static int belirleElKazanan(Map<Integer, Kart> oynananKartlar, Tip istenenRenkYerdeki, Tip koz, int eliBaslayanIndex) {
        int kazananIndex = -1;
        Kart enYuksekKoz = null;
        int kozAtanIndex = -1;
        for (Map.Entry<Integer, Kart> entry : oynananKartlar.entrySet()) {
            Kart kart = entry.getValue();
            if (kart != null && kart.getTip() == koz) {
                if (enYuksekKoz == null || kart.compareTo(enYuksekKoz) > 0) {
                    enYuksekKoz = kart;
                    kozAtanIndex = entry.getKey();
                }
            }
        }

        if (enYuksekKoz != null) {
            kazananIndex = kozAtanIndex;
        } else if (istenenRenkYerdeki != null) {
            Kart enYuksekIstenen = null;
            int istenenAtanIndex = -1;
            for (Map.Entry<Integer, Kart> entry : oynananKartlar.entrySet()) {
                Kart kart = entry.getValue();
                if (kart != null && kart.getTip() == istenenRenkYerdeki) {
                    if (enYuksekIstenen == null || kart.compareTo(enYuksekIstenen) > 0) {
                        enYuksekIstenen = kart;
                        istenenAtanIndex = entry.getKey();
                    }
                }
            }
            if (enYuksekIstenen != null) {
                kazananIndex = istenenAtanIndex;
            } else {
                System.err.println("UYARI (belirleElKazanan): Koz yok, istenen renk de oynanmamış! Eli başlatan (" + eliBaslayanIndex + ") alıyor.");
                kazananIndex = eliBaslayanIndex;
            }
        } else {
            System.err.println("UYARI (belirleElKazanan): İstenen renk null! Eli başlatan (" + eliBaslayanIndex + ") alıyor.");
            kazananIndex = eliBaslayanIndex;
        }

        if (kazananIndex == -1 && !oynananKartlar.isEmpty() && oynananKartlar.values().stream().anyMatch(Objects::nonNull) && eliBaslayanIndex < oyuncular.length) {
            System.err.println("UYARI (belirleElKazanan): Kazanan atanamadı! Eli başlatan (" + eliBaslayanIndex + ") alıyor.");
            kazananIndex = eliBaslayanIndex;
        } else if (kazananIndex == -1 && (oynananKartlar.isEmpty() || oynananKartlar.values().stream().allMatch(Objects::isNull))) {
            System.err.println("UYARI (belirleElKazanan): Oynanan kart yok veya tümü null, kazanan belirlenemedi! Eli başlatan (" + eliBaslayanIndex + ") alıyor.");
            kazananIndex = eliBaslayanIndex;
        }
        return kazananIndex;
    }

    // =======================================================================
    // === MAIN METODU =======================================================
    // =======================================================================
    public static Oyuncu[] oyuncular;

    public static void main(String[] args) {

        System.out.println("--- Batak Oyunu Başlatılıyor ---");
        Scanner scanner = new Scanner(System.in);

        Deste deste = new Deste();
        System.out.println("Deste oluşturuldu ve karıştırıldı.");
        oyuncular = new Oyuncu[]{
                new Oyuncu("Ahmet"), new Oyuncu("Mehmet"), new Oyuncu("Veli"), new Oyuncu("Ayşe")
        };
        System.out.println(oyuncular.length + " oyuncu oluşturuldu.");
        List<Kart> tumKartlar = deste.getTumKartlar();
        int kartDagitimIndex = 0;
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < oyuncular.length; j++) {
                if (kartDagitimIndex < tumKartlar.size()) {
                    oyuncular[j].kartAl(tumKartlar.get(kartDagitimIndex++));
                }
            }
        }
        System.out.println("Kart dağıtımı tamamlandı.");
        System.out.println("\n--- Oyuncu Elleri ---");
        for (Oyuncu oyuncu : oyuncular) {
            oyuncu.eliniGosterNumarali();
        }

        System.out.println("\n===================================");
        System.out.println("--- İHALE AŞAMASI ---");
        final int MIN_IHALE = 5; final int ZORUNLU_IHALE = 4; final int MAKSIMUM_IHALE = 13;
        System.out.println("Min: 5, Max: 13, Zorunlu: 4");
        System.out.println("===================================");
        int mevcutIhale = MIN_IHALE - 1; int ihaleSahibiIndex = -1; Tip koz = null;
        boolean[] pasGecenler = new boolean[oyuncular.length];
        int aktifOyuncuIndex = 0; int konusabilecekOyuncuSayisi = oyuncular.length;
        while (konusabilecekOyuncuSayisi > 1 || (konusabilecekOyuncuSayisi == 1 && ihaleSahibiIndex == -1 && !pasGecenler[aktifOyuncuIndex])) {
            while (pasGecenler[aktifOyuncuIndex]) { aktifOyuncuIndex = (aktifOyuncuIndex + 1) % oyuncular.length; }
            Oyuncu aktifOyuncu = oyuncular[aktifOyuncuIndex];
            System.out.println("\n---------------------------------");
            System.out.println("Sıra Oyuncu '" + aktifOyuncu.getIsim() + "'");
            aktifOyuncu.eliniGosterNumarali();
            System.out.println("Mevcut İhale: " + (mevcutIhale < MIN_IHALE ? "Yok" : mevcutIhale));
            System.out.print("Teklifiniz ('Pas' veya " + Math.max(MIN_IHALE, mevcutIhale + 1) + "-" + MAKSIMUM_IHALE + "): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Pas")) {
                pasGecenler[aktifOyuncuIndex] = true; konusabilecekOyuncuSayisi--;
                System.out.println("'" + aktifOyuncu.getIsim() + "' pas geçti.");
            } else {
                try {
                    int teklif = Integer.parseInt(input);
                    int minGereken = Math.max(MIN_IHALE, (mevcutIhale < MIN_IHALE ? MIN_IHALE : mevcutIhale + 1));
                    if (teklif >= minGereken && teklif <= MAKSIMUM_IHALE) {
                        mevcutIhale = teklif; ihaleSahibiIndex = aktifOyuncuIndex;
                        System.out.println("'" + aktifOyuncu.getIsim() + "' ihaleyi " + mevcutIhale + " yaptı!");
                    } else {
                        if (teklif > MAKSIMUM_IHALE) System.out.println("!!! Geçersiz Teklif! Max "+ MAKSIMUM_IHALE);
                        else if (teklif < minGereken) System.out.println("!!! Geçersiz Teklif! Min "+ minGereken);
                        else System.out.println("!!! Geçersiz Teklif!");
                        continue;
                    }
                } catch (NumberFormatException e) { System.out.println("!!! Geçersiz Giriş!"); continue; }
            }
            aktifOyuncuIndex = (aktifOyuncuIndex + 1) % oyuncular.length;
            if (konusabilecekOyuncuSayisi == 1 && ihaleSahibiIndex != -1) {
                boolean onlyBidderLeft = true;
                for(int k=0; k<pasGecenler.length; k++){ if(!pasGecenler[k] && k != ihaleSahibiIndex) { onlyBidderLeft = false; break; } }
                if(onlyBidderLeft) break;
            }
        }
        System.out.println("\n===================================");
        System.out.println("--- İHALE SONU ---");
        if (ihaleSahibiIndex == -1) {
            ihaleSahibiIndex = 0; mevcutIhale = ZORUNLU_IHALE;
            System.out.println("!!! Herkes pas geçti! İhale " + mevcutIhale + " ile zorunlu olarak '" + oyuncular[ihaleSahibiIndex].getIsim() + "' oyuncusuna kaldı!");
        }
        Oyuncu ihaleyiAlan = oyuncular[ihaleSahibiIndex];
        ihaleyiAlan.setTaahhut(mevcutIhale);
        System.out.println("İhaleci: '" + ihaleyiAlan.getIsim() + "', Taahhüt: " + ihaleyiAlan.getTaahhut());
        while (koz == null) {
            System.out.println("\n'" + ihaleyiAlan.getIsim() + "', elini gör ve kozu belirle:");
            ihaleyiAlan.eliniGosterNumarali();
            System.out.print("Kozu girin (KUPA, KARO, MACA, SINEK): ");
            String kozInput = scanner.nextLine().trim().toUpperCase();
            try { koz = Tip.valueOf(kozInput); System.out.println(">>> Koz '" + koz + "' olarak belirlendi. <<<"); }
            catch (IllegalArgumentException e) { System.out.println("!!! Geçersiz koz tipi!"); }
        }

        System.out.println("\n===================================");
        System.out.println("--- OYUN OYNAMA AŞAMASI ---");
        System.out.println("--- Koz: " + koz + " ---");
        System.out.println("===================================");
        for (Oyuncu oyuncu : oyuncular) { oyuncu.aldigiElSayisiniSifirla(); }
        int eliBaslayanIndex = ihaleSahibiIndex;
        Map<Integer, Kart> mevcutElKartlari = new HashMap<>();
        boolean kozCiktiMi = false;

        for (int elNo = 1; elNo <= 13; elNo++) {
            System.out.println("\n┌--------------------- El " + elNo + " ---------------------┐");
            if(kozCiktiMi) System.out.println("   (Kozlar serbest)"); else System.out.println("   (Henüz koz çıkmadı)");
            mevcutElKartlari.clear();
            Tip istenenRenkYerdeki = null;
            int kartOynayanOyuncuSiraNoMainLoop = eliBaslayanIndex;

            for (int i = 0; i < oyuncular.length; i++) {
                Oyuncu aktifOyuncu = oyuncular[kartOynayanOyuncuSiraNoMainLoop];
                System.out.println("\n Sıra Oyuncu: " + aktifOyuncu.getIsim() + " (Taahhüt: " + aktifOyuncu.getTaahhut() + ", Aldığı: " + aktifOyuncu.getAldigiElSayisi() + ")");
                List<Kart> oyuncuEli = aktifOyuncu.getEl();

                if (oyuncuEli.isEmpty()) {
                    System.out.println(" -> Elinde kart kalmadı, pas geçiyor.");
                    mevcutElKartlari.put(kartOynayanOyuncuSiraNoMainLoop, null);
                    kartOynayanOyuncuSiraNoMainLoop = (kartOynayanOyuncuSiraNoMainLoop + 1) % oyuncular.length;
                    continue;
                }

                // getGecerliKartlar çağrısı güncellendi (elNo ve i kaldırıldı)
                List<Kart> gecerliKartlar = getGecerliKartlar(aktifOyuncu, istenenRenkYerdeki, koz, mevcutElKartlari, kozCiktiMi);

                Kart secilenKart = null;
                while (secilenKart == null) {
                    System.out.println(" Elinizdeki Kartlar:");
                    List<Kart> siraliEl = new ArrayList<>(oyuncuEli);
                    Collections.sort(siraliEl);
                    for(int idx = 0; idx < siraliEl.size(); idx++){
                        String isaret = gecerliKartlar.contains(siraliEl.get(idx)) ? " *" : "  ";
                        System.out.println(isaret + (idx + 1) + ": " + siraliEl.get(idx));
                    }

                    if (gecerliKartlar.isEmpty() && !siraliEl.isEmpty()) {
                        System.err.println("UYARI: " + aktifOyuncu.getIsim() + " için oynanacak geçerli kart bulunamadı! (getGecerliKartlar metodunu kontrol edin)");
                        if (!siraliEl.isEmpty()) { secilenKart = siraliEl.get(0); System.out.println("HATA durumu: Geçici olarak ilk kart seçildi: "+ secilenKart); }
                        else break;
                    } else if (siraliEl.isEmpty()){
                        System.out.println(" -> El boş, kart seçilemiyor."); break;
                    } else {
                        System.out.print(" Oynamak istediğiniz kartın numarasını girin (* işaretliler geçerlidir): ");
                        try {
                            int secimNumarasi = Integer.parseInt(scanner.nextLine());
                            int secimIndex = secimNumarasi - 1;
                            if (secimIndex >= 0 && secimIndex < siraliEl.size()) {
                                Kart potansiyelKart = siraliEl.get(secimIndex);
                                if (gecerliKartlar.contains(potansiyelKart)) {
                                    secilenKart = potansiyelKart;
                                    System.out.println(" -> Seçilen: " + secilenKart);
                                } else { System.out.println("!!! Geçersiz Hamle! Lütfen '*' ile işaretli kartlardan seçin."); }
                            } else { System.out.println("!!! Geçersiz numara."); }
                        } catch (NumberFormatException e) { System.out.println("!!! Lütfen sadece sayı girin."); }
                    }
                }

                if (secilenKart == null) {
                    System.err.println("UYARI: Kart seçilemedi, oyuncu atlanıyor.");
                    mevcutElKartlari.put(kartOynayanOyuncuSiraNoMainLoop, null);
                    kartOynayanOyuncuSiraNoMainLoop = (kartOynayanOyuncuSiraNoMainLoop + 1) % oyuncular.length;
                    continue;
                }

                mevcutElKartlari.put(kartOynayanOyuncuSiraNoMainLoop, secilenKart);
                aktifOyuncu.kartOyna(secilenKart);
                System.out.println("'" + aktifOyuncu.getIsim() + "' oynadı: " + secilenKart);

                if (!kozCiktiMi && secilenKart.getTip() == koz) {
                    kozCiktiMi = true;
                    System.out.println("   >>> Koz Çıktı! <<<");
                }
                if (i == 0) {
                    istenenRenkYerdeki = secilenKart.getTip();
                    System.out.println("   (İstenen renk: " + istenenRenkYerdeki + ")");
                }
                kartOynayanOyuncuSiraNoMainLoop = (kartOynayanOyuncuSiraNoMainLoop + 1) % oyuncular.length;
            }

            if (!mevcutElKartlari.isEmpty() && mevcutElKartlari.values().stream().anyMatch(Objects::nonNull)) {
                int elKazananIndex = belirleElKazanan(mevcutElKartlari, istenenRenkYerdeki, koz, eliBaslayanIndex);
                if (elKazananIndex != -1) {
                    Oyuncu elKazanan = oyuncular[elKazananIndex];
                    System.out.println("\n>>> El " + elNo + " kazananı: " + elKazanan.getIsim() + " (" + (mevcutElKartlari.get(elKazananIndex) != null ? mevcutElKartlari.get(elKazananIndex) : "Kart Yok") + ")");
                    elKazanan.aldigiElArtir();
                    eliBaslayanIndex = elKazananIndex;
                } else { System.err.println("UYARI: El " + elNo + " kazananı belirlenemedi!"); }
            } else { System.err.println("UYARI: El " + elNo + "'de kart oynanmadı!"); }
            System.out.println("└----------------------------------------------------┘");
        }

        System.out.println("\n===================================");
        System.out.println("--- OYUN TURU SONU ---");
        System.out.println("İhaleci: " + oyuncular[ihaleSahibiIndex].getIsim() + ", Taahhüt: " + oyuncular[ihaleSahibiIndex].getTaahhut());
        System.out.println("Alınan Eller:");
        int toplamAlinanEl = 0;
        for (Oyuncu oyuncu : oyuncular) {
            int alinan = oyuncu.getAldigiElSayisi();
            System.out.println("  " + oyuncu.getIsim() + ": " + alinan);
            toplamAlinanEl += alinan;
        }
        System.out.println("Toplam Alınan El: " + toplamAlinanEl + (toplamAlinanEl != 13 ? " (HATA!)" : ""));
        System.out.println("===================================");
        System.out.println("\n--- PUANLAMA ---");
        Oyuncu ihaleci = oyuncular[ihaleSahibiIndex];
        if (ihaleci.getAldigiElSayisi() >= ihaleci.getTaahhut()) {
            System.out.println(ihaleci.getIsim() + " ihaleyi aldı! Puan: " + ihaleci.getTaahhut());
        } else {
            System.out.println(ihaleci.getIsim() + " battı! Puan: -" + ihaleci.getTaahhut());
        }

        scanner.close();
        System.out.println("\n--- Oyun Bitti ---");
    }
}








