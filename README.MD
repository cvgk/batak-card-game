# 🎴 Batak Card Game

Traditional Turkish Batak card game implemented in Java with Swing GUI and intelligent AI opponents.

![Java](https://img.shields.io/badge/Java-8-orange)
![License](https://img.shields.io/badge/license-MIT-blue)

## 📖 About

Batak is a popular Turkish trick-taking card game for 4 players. This implementation features:

- ✅ Complete Batak game rules
- ✅ Java Swing graphical user interface
- ✅ Smart AI opponents with realistic bidding
- ✅ Trump card selection system
- ✅ Score tracking
- ✅ Comprehensive unit tests with JUnit

## 🎮 How to Play

### Rules
1. 4 players receive 13 cards each from a standard 52-card deck
2. Players bid on how many tricks they can win (minimum 5)
3. Highest bidder chooses the trump suit
4. Players must follow suit if possible
5. Trump cards beat non-trump cards
6. Player who wins trick leads next trick
7. First to reach their bid wins the round

### Controls
- Click on cards to play them
- Follow the on-screen instructions
- AI opponents play automatically

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- JUnit 4.13.2 (for running tests)

### Running the Game

**GUI Version:**
```bash
javac BatakGameUI.java Card.java Player.java DeckOfCards.java
java BatakGameUI
```

**Console Version:**
```bash
javac Test.java Card.java Player.java DeckOfCards.java
java Test
```

### Running Tests
```bash
javac -cp junit-4.13.2.jar:. BatakGameTest.java
java -cp junit-4.13.2.jar:. org.junit.runner.JUnitCore BatakGameTest
```

## 📁 Project Structure
```
batak-card-game/
├── Card.java              # Card representation with suit and rank
├── DeckOfCards.java       # 52-card deck with shuffling
├── Player.java            # Player logic and valid move checking
├── Test.java              # Console-based game implementation
├── BatakGameUI.java       # Swing GUI game interface
└── BatakGameTest.java     # JUnit unit tests
```

## 🧠 AI Features

- Intelligent bidding based on hand strength
- Evaluates card distribution and high cards
- Adjusts bid based on trump potential
- Automatic trump suit selection
- Strategic card playing

## 🎯 Future Improvements

- [ ] Card play animations
- [ ] Sound effects
- [ ] Multiple difficulty levels
- [ ] Network multiplayer
- [ ] Game history and statistics
- [ ] Custom card designs
- [ ] Save/load game state

## 🧪 Testing

The project includes comprehensive unit tests covering:
- Card validation and comparison
- Trump card rules
- Valid move checking
- Trick-taking logic
- Score calculation

Run all tests: `14 passing tests ✅`

## 📸 Screenshots

_Coming soon_

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest features
- Submit pull requests

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

**cvgk** - [GitHub Profile](https://github.com/cvgk)

---

⭐ If you like this project, give it a star!