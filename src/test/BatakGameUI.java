package test;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// Ana oyun penceresi
public class BatakGameUI extends JFrame {
    private DeckOfCards deck;
    private Player[] players;
    private JPanel mainPanel;
    private JPanel playerHandPanel;
    private JPanel middlePanel;
    private JPanel infoPanel;
    private JLabel infoLabel;
    private JLabel scoreLabel;
    private List<CardButton> playerCardButtons;
    private List<CardPanel> middleCards;
    private int currentPlayerIndex = 0;
    private int roundCount = 0;
    private Card[] inTheMiddle = new Card[4];
    private HashMap<Card, Player> cardPlayerMap = new HashMap<Card, Player>();
    private int cardsPlayedInRound = 0;
    private int startingPlayer = 0;
    private int teklif = 4;
    private String koz = "";

    public BatakGameUI() {
        setTitle("Batak Oyunu");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initializeGame();
        createUI();

        setLocationRelativeTo(null);
        setVisible(true);

        // Oyunu başlat
        startBidding();
    }

    private void initializeGame() {
        deck = new DeckOfCards();
        players = new Player[]{new Player(1), new Player(2), new Player(3), new Player(4)};
        playerCardButtons = new ArrayList<CardButton>();
        middleCards = new ArrayList<CardPanel>();

        // Kartları dağıt
        int x = 0, y = 0;
        while (x < 13) {
            for (int i = 0; i < players.length; i++) {
                players[i].cards[x] = deck.cards[y++];
            }
            x++;
        }

        // Kartları sırala
        for (int playerNo = 0; playerNo < players.length; playerNo++) {
            Arrays.sort(players[playerNo].cards);
        }
    }

    private void createUI() {
        // Üst panel - Bilgi paneli
        infoPanel = new JPanel();
        infoPanel.setBackground(new Color(34, 139, 34));
        infoPanel.setPreferredSize(new Dimension(1200, 60));
        infoLabel = new JLabel("Batak Oyunu - İhale Başlıyor", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        infoLabel.setForeground(Color.WHITE);
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setForeground(Color.WHITE);
        infoPanel.setLayout(new GridLayout(2, 1));
        infoPanel.add(infoLabel);
        infoPanel.add(scoreLabel);
        add(infoPanel, BorderLayout.NORTH);

        // Orta panel - Oyun masası
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(34, 139, 34));

        // Ortadaki kartlar için panel
        middlePanel = new JPanel(new GridLayout(2, 2, 20, 20));
        middlePanel.setBackground(new Color(34, 139, 34));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        for (int i = 0; i < 4; i++) {
            CardPanel cp = new CardPanel(null);
            middleCards.add(cp);
            middlePanel.add(cp);
        }

        mainPanel.add(middlePanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Alt panel - Oyuncu kartları
        playerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        playerHandPanel.setBackground(new Color(50, 50, 50));
        playerHandPanel.setPreferredSize(new Dimension(1200, 180));
        updatePlayerHand();
        add(playerHandPanel, BorderLayout.SOUTH);
    }

    private void updatePlayerHand() {
        playerHandPanel.removeAll();
        playerCardButtons.clear();

        Player currentPlayer = players[0]; // İnsan oyuncu her zaman Player 1

        for (int i = 0; i < currentPlayer.cards.length; i++) {
            final Card card = currentPlayer.cards[i];
            if (card != null) {
                CardButton cardBtn = new CardButton(card);
                cardBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        playCard(card);
                    }
                });
                playerCardButtons.add(cardBtn);
                playerHandPanel.add(cardBtn);
            }
        }

        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    private void startBidding() {
        final JDialog biddingDialog = new JDialog(this, "İhale", true);
        biddingDialog.setSize(500, 350);
        biddingDialog.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("İhale Başlıyor", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel);

        final JLabel currentPlayerLabel = new JLabel("Sıra: Player 1", SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(currentPlayerLabel);

        final JTextArea bidHistory = new JTextArea();
        bidHistory.setEditable(false);
        bidHistory.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(bidHistory);
        panel.add(scrollPane);

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Teklifiniz (5-13) veya PAS:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(label);

        final JTextField bidField = new JTextField(8);
        bidField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(bidField);
        panel.add(inputPanel);

        final JLabel currentBidLabel = new JLabel("Mevcut en yüksek teklif: " + teklif, SwingConstants.CENTER);
        currentBidLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(currentBidLabel);

        final JButton submitBtn = new JButton("Teklif Ver");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(submitBtn);

        biddingDialog.add(panel, BorderLayout.CENTER);

        final int[] winningPlayer = {-1};
        final int[] playerBidding = {0};

        // Bilgisayar oyuncuları için teklif fonksiyonu
        final Runnable processComputerBids = new Runnable() {
            public void run() {
                while (playerBidding[0] < 4 && playerBidding[0] != 0) {
                    final int currentPlayer = playerBidding[0];

                    // Akıllı AI: Elindeki kartlara göre teklif ver
                    Player aiPlayer = players[currentPlayer];
                    int aiSuggestion = calculateBid(aiPlayer);

                    String bidText = "";
                    Random rand = new Random();

                    // Teklif verilebilir mi kontrol et
                    boolean canBid = aiSuggestion > teklif;

                    if (canBid) {
                        // El ne kadar güçlü?
                        int bidDifference = aiSuggestion - teklif;

                        // Eğer sadece 1 fazlaysa, %50 şans
                        // 2 fazlaysa %70 şans
                        // 3+ fazlaysa %85 şans
                        int bidChance;
                        if (bidDifference == 1) bidChance = 40;      // Sadece 1 fazla = temkinli
                        else if (bidDifference == 2) bidChance = 60; // 2 fazla = biraz emin
                        else bidChance = 75;                         // 3+ fazla = çok emin

                        if (rand.nextInt(100) < bidChance) {
                            teklif = aiSuggestion;
                            winningPlayer[0] = currentPlayer;
                            bidText = "Player " + (currentPlayer + 1) + ": " + aiSuggestion + "\n";
                        } else {
                            bidText = "Player " + (currentPlayer + 1) + ": PAS\n";
                        }
                    } else {
                        // Eli mevcut teklife yetmiyor, pas geç
                        bidText = "Player " + (currentPlayer + 1) + ": PAS\n";
                    }

                    final String finalBidText = bidText;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            bidHistory.append(finalBidText);
                        }
                    });

                    playerBidding[0]++;

                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Tüm teklifler bitti
                if (winningPlayer[0] == -1) winningPlayer[0] = 0;

                final int finalWinner = winningPlayer[0];
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        biddingDialog.dispose();
                        selectKoz(finalWinner);
                    }
                });
            }
        };

        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (playerBidding[0] != 0) return;

                String bid = bidField.getText().trim().toLowerCase();

                if (bid.equals("pas") || bid.equals("")) {
                    bidHistory.append("Player 1: PAS\n");
                    playerBidding[0]++;

                    currentBidLabel.setText("Mevcut en yüksek teklif: " + teklif);
                    bidField.setText("");
                    submitBtn.setEnabled(false);
                    bidField.setEnabled(false);

                    new Thread(processComputerBids).start();

                } else {
                    try {
                        int bidValue = Integer.parseInt(bid);
                        if (bidValue <= 4 || bidValue > 13 || bidValue <= teklif) {
                            JOptionPane.showMessageDialog(biddingDialog,
                                    "Geçersiz teklif! " + (teklif + 1) + " ile 13 arasında olmalı.",
                                    "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        teklif = bidValue;
                        winningPlayer[0] = 0;
                        bidHistory.append("Player 1: " + bidValue + "\n");
                        playerBidding[0]++;

                        currentBidLabel.setText("Mevcut en yüksek teklif: " + teklif);
                        bidField.setText("");
                        submitBtn.setEnabled(false);
                        bidField.setEnabled(false);

                        new Thread(processComputerBids).start();

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(biddingDialog,
                                "Lütfen geçerli bir sayı veya 'pas' girin!",
                                "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        bidField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitBtn.doClick();
            }
        });

        biddingDialog.setLocationRelativeTo(this);
        biddingDialog.setVisible(true);
    }

    private int calculateBid(Player player) {
        // Her takımdan kaç tane olduğunu say
        int[] suitCount = new int[4]; // clubs, diamonds, hearts, spades
        int[] highCards = new int[4];  // As, Papaz, Kız, Vale sayısı
        int totalAces = 0;

        for (int i = 0; i < player.cards.length; i++) {
            Card card = player.cards[i];

            // Takım sayısını artır
            if (card.getSuite().equals("clubs")) suitCount[0]++;
            else if (card.getSuite().equals("diamonds")) suitCount[1]++;
            else if (card.getSuite().equals("hearts")) suitCount[2]++;
            else if (card.getSuite().equals("spades")) suitCount[3]++;

            // Asları say
            if (card.val2 == 14) totalAces++;

            // Yüksek kartları say (Papaz=13, Kız=12, Vale=11)
            if (card.val2 >= 11) {
                if (card.getSuite().equals("clubs")) highCards[0]++;
                else if (card.getSuite().equals("diamonds")) highCards[1]++;
                else if (card.getSuite().equals("hearts")) highCards[2]++;
                else if (card.getSuite().equals("spades")) highCards[3]++;
            }
        }

        // En güçlü takımı bul
        int maxSuitIndex = 0;
        int maxSuitCount = suitCount[0];
        for (int i = 1; i < 4; i++) {
            if (suitCount[i] > maxSuitCount) {
                maxSuitCount = suitCount[i];
                maxSuitIndex = i;
            }
        }

        // El gücünü hesapla - ÇOK DAHA SIKI KRITERLER
        float strength = 0.0f;

        // En güçlü takımdaki kart sayısı (sadece çok fazlaysa puan ver)
        if (maxSuitCount >= 8) strength += 2.0f;        // 6+ kart = çok güçlü
        else if (maxSuitCount >= 6) strength += 1.0f;   // 5 kart = orta
        else if (maxSuitCount >= 5) strength += 0.5f;   // 4 kart = zayıf avantaj
        // 3 veya daha az kart = puan yok

        // O takımdaki yüksek kartlar (daha az ağırlık)
        strength += highCards[maxSuitIndex];

        // As sayısı çok önemli
        strength += totalAces * 2;

        // Strength'e göre DAHA DÜŞÜK teklif hesapla
        // Gerçekçi Batak teklifleri: çoğu 5-7 arası

              // Zayıf el
        if (strength <= 5) return 5;  // Orta-zayıf
        else if (strength <= 8) return 6;  // Orta
        else if (strength <= 10) return 7;  // Orta-iyi

        else return 8;                    // Mükemmel el
    }

    private void selectKoz(int winningPlayer) {
        String[] options = {"Sinek (♣)", "Karo (♦)", "Kupa (♥)", "Maça (♠)"};
        String[] values = {"clubs", "diamonds", "hearts", "spades"};

        int choice = -1;

        // Eğer Player 1 (insan) kazandıysa, koz seçsin
        if (winningPlayer == 0) {
            choice = JOptionPane.showOptionDialog(this,
                    "Tebrikler! İhaleyi kazandınız! Koz seçin:",
                    "Koz Seçimi",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
        } else {
            // Bilgisayar kazandıysa, en güçlü takımı koz yapsın
            Player winner = players[winningPlayer];
            choice = selectBestSuit(winner);

            JOptionPane.showMessageDialog(this,
                    "Player " + (winningPlayer + 1) + " ihaleyi kazandı ve koz olarak " + options[choice] + " seçti!",
                    "İhale Sonucu",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        if (choice >= 0) {
            koz = values[choice];
            Player.koz = koz;
            startingPlayer = winningPlayer;
            currentPlayerIndex = winningPlayer;

            String kozTurkce = options[choice];
            infoLabel.setText("Koz: " + kozTurkce + " | Teklif: " + teklif + " | Sıra: Player " + (currentPlayerIndex + 1));
            updateScoreLabel();

            if (currentPlayerIndex != 0) {
                computerPlay();
            }
        }
    }

    private int selectBestSuit(Player player) {
        // Her takımdan kaç kart olduğunu ve güçlerini hesapla
        int[] suitCount = new int[4];
        int[] suitStrength = new int[4];

        for (int i = 0; i < player.cards.length; i++) {
            Card card = player.cards[i];
            int suitIndex = -1;

            if (card.getSuite().equals("clubs")) suitIndex = 0;
            else if (card.getSuite().equals("diamonds")) suitIndex = 1;
            else if (card.getSuite().equals("hearts")) suitIndex = 2;
            else if (card.getSuite().equals("spades")) suitIndex = 3;

            if (suitIndex >= 0) {
                suitCount[suitIndex]++;
                // Yüksek kartlara ekstra puan
                if (card.val2 >= 11) suitStrength[suitIndex] += 3;
                else if (card.val2 >= 9) suitStrength[suitIndex] += 1;
            }
        }

        // En iyi takımı seç (hem sayı hem güç açısından)
        int bestSuit = 0;
        int bestScore = suitCount[0] * 2 + suitStrength[0];

        for (int i = 1; i < 4; i++) {
            int score = suitCount[i] * 2 + suitStrength[i];
            if (score > bestScore) {
                bestScore = score;
                bestSuit = i;
            }
        }

        return bestSuit;
    }

    private void playCard(Card card) {
        if (currentPlayerIndex != 0) {
            JOptionPane.showMessageDialog(this, "Şu an sizin sıranız değil!");
            return;
        }

        Player currentPlayer = players[currentPlayerIndex];
        List<Card> validCards = getValidCards(currentPlayer, cardsPlayedInRound);

        if (!validCards.contains(card)) {
            JOptionPane.showMessageDialog(this, "Bu kartı oynayamazsınız!");
            return;
        }

        inTheMiddle[cardsPlayedInRound] = card;
        cardPlayerMap.put(card, currentPlayer);

        if (cardsPlayedInRound == 0) {
            Player.current = card;
            Player.takes = card;
        } else {
            Player.current = card;
            if (cardsPlayedInRound == 1) {
                currentPlayer.critics();
            } else {
                currentPlayer.dcritics();
            }
        }

        removeCardFromPlayer(currentPlayer, card);

        middleCards.get(cardsPlayedInRound).setCard(card);
        updatePlayerHand();

        cardsPlayedInRound++;
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;

        infoLabel.setText("Koz: " + getSuiteName(koz) + " | Sıra: Player " + (currentPlayerIndex + 1));

        if (cardsPlayedInRound >= 4) {
            Timer timer = new Timer(1500, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    finishRound();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            Timer timer = new Timer(500, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    computerPlay();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void computerPlay() {
        if (currentPlayerIndex == 0) return;

        Player currentPlayer = players[currentPlayerIndex];
        List<Card> validCards = getValidCards(currentPlayer, cardsPlayedInRound);

        if (validCards.isEmpty()) return;

        Card cardToPlay = validCards.get(0);

        inTheMiddle[cardsPlayedInRound] = cardToPlay;
        cardPlayerMap.put(cardToPlay, currentPlayer);

        if (cardsPlayedInRound == 0) {
            Player.current = cardToPlay;
            Player.takes = cardToPlay;
        } else {
            Player.current = cardToPlay;
            if (cardsPlayedInRound == 1) {
                currentPlayer.critics();
            } else {
                currentPlayer.dcritics();
            }
        }

        removeCardFromPlayer(currentPlayer, cardToPlay);
        middleCards.get(cardsPlayedInRound).setCard(cardToPlay);

        cardsPlayedInRound++;
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;

        infoLabel.setText("Koz: " + getSuiteName(koz) + " | Sıra: Player " + (currentPlayerIndex + 1));

        if (cardsPlayedInRound >= 4) {
            Timer timer = new Timer(1500, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    finishRound();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            Timer timer = new Timer(500, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    computerPlay();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private List<Card> getValidCards(Player player, int position) {
        player.clear();

        if (position == 0) {
            return player.cZero();
        } else if (position == 1) {
            return player.cOne();
        } else {
            return player.dOne(inTheMiddle);
        }
    }

    private void removeCardFromPlayer(Player player, Card card) {
        List<Card> newCards = new ArrayList<Card>();
        for (int i = 0; i < player.cards.length; i++) {
            Card c = player.cards[i];
            if (c != null && c != card) {
                newCards.add(c);
            }
        }
        player.cards = newCards.toArray(new Card[newCards.size()]);
    }

    private void finishRound() {
        Player winner = cardPlayerMap.get(Player.takes);
        winner.setScore(winner.getScore() + 1);

        JOptionPane.showMessageDialog(this,
                winner + " eli aldı!",
                "El Bitti",
                JOptionPane.INFORMATION_MESSAGE);

        for (int i = 0; i < middleCards.size(); i++) {
            middleCards.get(i).setCard(null);
        }

        cardsPlayedInRound = 0;
        inTheMiddle = new Card[4];
        cardPlayerMap.clear();

        for (int i = 0; i < players.length; i++) {
            if (players[i] == winner) {
                startingPlayer = i;
                currentPlayerIndex = i;
                break;
            }
        }

        roundCount++;
        updateScoreLabel();

        if (roundCount >= 13) {
            endGame();
        } else {
            infoLabel.setText("Koz: " + getSuiteName(koz) + " | Sıra: Player " + (currentPlayerIndex + 1));
            if (currentPlayerIndex != 0) {
                computerPlay();
            }
        }
    }

    private void updateScoreLabel() {
        StringBuilder sb = new StringBuilder("Skorlar: ");
        for (int i = 0; i < players.length; i++) {
            sb.append(players[i]).append(": ").append(players[i].getScore()).append("  ");
        }
        scoreLabel.setText(sb.toString());
    }

    private void endGame() {
        StringBuilder result = new StringBuilder("Oyun Bitti!\n\nSonuçlar:\n");
        for (int i = 0; i < players.length; i++) {
            result.append(players[i]).append(": ").append(players[i].getScore()).append(" el\n");
        }

        JOptionPane.showMessageDialog(this, result.toString(), "Oyun Sonu", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private String getSuiteName(String suite) {
        if (suite.equals("clubs")) return "Sinek (♣)";
        if (suite.equals("diamonds")) return "Karo (♦)";
        if (suite.equals("hearts")) return "Kupa (♥)";
        if (suite.equals("spades")) return "Maça (♠)";
        return suite;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BatakGameUI();
            }
        });
    }
}

// Kart görseli için JPanel
class CardPanel extends JPanel {
    private Card card;

    public CardPanel(Card card) {
        this.card = card;
        setPreferredSize(new Dimension(100, 140));
        setBackground(new Color(34, 139, 34));
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }

    public void setCard(Card card) {
        this.card = card;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (card == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);

        Color cardColor = (card.getSuite().equals("hearts") || card.getSuite().equals("diamonds"))
                ? Color.RED : Color.BLACK;
        g2d.setColor(cardColor);

        String rankStr = getRankString(card.getRank());
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString(rankStr, 15, 30);

        String symbol = getSuiteSymbol(card.getSuite());
        g2d.setFont(new Font("Arial", Font.PLAIN, 50));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(symbol)) / 2;
        g2d.drawString(symbol, x, getHeight() / 2 + 15);

        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.drawString(symbol, 15, 50);
    }

    private String getSuiteSymbol(String suite) {
        if (suite.equals("clubs")) return "♣";
        if (suite.equals("diamonds")) return "♦";
        if (suite.equals("hearts")) return "♥";
        if (suite.equals("spades")) return "♠";
        return "";
    }

    private String getRankString(String rank) {
        if (rank.equals("two")) return "2";
        if (rank.equals("three")) return "3";
        if (rank.equals("four")) return "4";
        if (rank.equals("five")) return "5";
        if (rank.equals("six")) return "6";
        if (rank.equals("seven")) return "7";
        if (rank.equals("eight")) return "8";
        if (rank.equals("nine")) return "9";
        if (rank.equals("ten")) return "10";
        if (rank.equals("jack")) return "J";
        if (rank.equals("queen")) return "Q";
        if (rank.equals("king")) return "K";
        if (rank.equals("ace")) return "A";
        return "";
    }
}

// Tıklanabilir kart butonu
class CardButton extends JButton {
    private Card card;

    public CardButton(Card card) {
        this.card = card;
        setPreferredSize(new Dimension(80, 120));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            }

            public void mouseExited(MouseEvent e) {
                setBorder(null);
            }
        });
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 10, 10);

        Color cardColor = (card.getSuite().equals("hearts") || card.getSuite().equals("diamonds"))
                ? Color.RED : Color.BLACK;
        g2d.setColor(cardColor);

        String rankStr = getRankString(card.getRank());
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString(rankStr, 10, 25);

        String symbol = getSuiteSymbol(card.getSuite());
        g2d.setFont(new Font("Arial", Font.PLAIN, 40));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(symbol)) / 2;
        g2d.drawString(symbol, x, getHeight() / 2 + 10);

        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString(symbol, 10, 40);
    }

    private String getSuiteSymbol(String suite) {
        if (suite.equals("clubs")) return "♣";
        if (suite.equals("diamonds")) return "♦";
        if (suite.equals("hearts")) return "♥";
        if (suite.equals("spades")) return "♠";
        return "";
    }

    private String getRankString(String rank) {
        if (rank.equals("two")) return "2";
        if (rank.equals("three")) return "3";
        if (rank.equals("four")) return "4";
        if (rank.equals("five")) return "5";
        if (rank.equals("six")) return "6";
        if (rank.equals("seven")) return "7";
        if (rank.equals("eight")) return "8";
        if (rank.equals("nine")) return "9";
        if (rank.equals("ten")) return "10";
        if (rank.equals("jack")) return "J";
        if (rank.equals("queen")) return "Q";
        if (rank.equals("king")) return "K";
        if (rank.equals("ace")) return "A";
        return "";
    }
}










