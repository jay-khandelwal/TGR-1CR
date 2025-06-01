package LLD.BowlingGame.V1;

import java.util.*;

// https://docs.google.com/document/d/1XDGiJNC9zI9bvK5Vef2Auuw20BnPhrS23bKA2JKfVhU/edit?tab=t.0

interface GameConfig {
    int getTotalPins();
    int getSpareBonusPoints();
    int getStrikeBonusPoints();
}

class StandardConfig implements GameConfig {
    public int getTotalPins() {
        return 10;
    }

    public int getSpareBonusPoints() {
        return 5;
    }

    public int getStrikeBonusPoints() {
        return 10;
    }
}

class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// Shared Scanner instance
class InputHelper {
    public static final Scanner SCANNER = new Scanner(System.in);
}

interface Chance {
    boolean isSpare();
    boolean isStrike();
    int getScore();
    void start(Player player);
}

class OneChance implements Chance {
    private boolean isSpare = false;
    private boolean isStrike = false;
    private int score = 0;
    GameConfig config;

    public OneChance(GameConfig config) {
        this.config = config;
    }

    public boolean isSpare() { return isSpare; }
    public boolean isStrike() { return isStrike; }
    public int getScore() { return score; }

    public void start(Player player) {
        while (true) {
            System.out.println(player.getName() + " turn (1 roll): ");
            int val = InputHelper.SCANNER.nextInt();
            if (val >= 0 && val <= config.getTotalPins()) {
                score += val;
                if (val == config.getTotalPins()) {
                    isStrike = true;
                }
                break;
            } else {
                System.out.println("Invalid value. Enter 0 to " + config.getTotalPins());
            }
        }
    }
}

class TwoChance implements Chance {
    private boolean isSpare = false;
    private boolean isStrike = false;
    private int score = 0;
    GameConfig config;

    public TwoChance(GameConfig config) {
        this.config = config;
    }

    public boolean isSpare() { return isSpare; }
    public boolean isStrike() { return isStrike; }
    public int getScore() { return score; }

    public void start(Player player) {
        int val1, val2 = 0;
        while (true) {
            System.out.println(player.getName() + " turn 1: ");
            val1 = InputHelper.SCANNER.nextInt();
            if (val1 >= 0 && val1 <= config.getTotalPins()) break;
            else System.out.println("Invalid input.");
        }
        score += val1;
        if (val1 == config.getTotalPins()) {
            isStrike = true;
            return;
        }

        while (true) {
            System.out.println(player.getName() + " turn 2: ");
            val2 = InputHelper.SCANNER.nextInt();
            if (val2 >= 0 && val1 + val2 <= config.getTotalPins()) break;
            else System.out.println("Invalid input.");
        }
        score += val2;
        if (val1 + val2 == config.getTotalPins()) {
            isSpare = true;
        }
    }
}

interface RoundStrategy {
    List<Chance> play(Player player, GameConfig config);
}

class NormalRoundStrategy implements RoundStrategy {
    public List<Chance> play(Player player, GameConfig config) {
        List<Chance> chances = new ArrayList<>();
        Chance chance = new TwoChance(config);
        chance.start(player);
        chances.add(chance);
        return chances;
    }
}

class LastRoundRoundStrategy implements RoundStrategy {
    public List<Chance> play(Player player, GameConfig config) {
        List<Chance> chances = new ArrayList<>();
        Chance firstChance = new TwoChance(config);
        firstChance.start(player);
        chances.add(firstChance);

        if (firstChance.isStrike() || firstChance.isSpare()) {
            Chance bonus1 = new OneChance(config);
            bonus1.start(player);
            chances.add(bonus1);

            Chance bonus2 = new OneChance(config);
            bonus2.start(player);
            chances.add(bonus2);
        }

        return chances;
    }
}

class Turn {
    RoundStrategy strategy;
    GameConfig config;
    List<Chance> chances;

    public Turn(RoundStrategy strategy, GameConfig config) {
        this.strategy = strategy;
        this.config = config;
        this.chances = new ArrayList<>();
    }

    public void start(Player player) {
        chances = strategy.play(player, config);
    }

    public int getScore() {
        int score = 0;
        for (Chance c : chances) {
            score += c.getScore();
            if (c.isSpare()) {
                score += config.getSpareBonusPoints();
            } else if (c.isStrike()) {
                score += config.getStrikeBonusPoints();
            }
        }
        return score;
    }
}

class Round {
    Map<Player, Turn> record;
    RoundStrategy strategy;
    GameConfig config;
    List<Player> players;

    public Round(List<Player> players, GameConfig config, RoundStrategy strategy) {
        this.players = players;
        this.config = config;
        this.strategy = strategy;
        record = new HashMap<>();
    }

    public void start(ScoreBoard scoreboard) {
        for (Player p : players) {
            Turn t = new Turn(strategy, config);
            t.start(p);
            record.put(p, t);
            scoreboard.updateScore(p, t.getScore());
        }
    }
}

class ScoreBoard {
    private final Map<Player, Integer> playerScores = new HashMap<>();

    public void updateScore(Player player, int score) {
        playerScores.put(player, playerScores.getOrDefault(player, 0) + score);
    }

    public void displayScores() {
        System.out.println("\nCurrent Scores:");
        for (Map.Entry<Player, Integer> entry : playerScores.entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
        }
        System.out.println();
    }
}

class Game {
    List<RoundStrategy> strategies;
    List<Player> players;
    GameConfig config;

    public Game(List<Player> players, List<RoundStrategy> strategies, GameConfig config) {
        this.players = players;
        this.strategies = strategies;
        this.config = config;
    }

    public void start() {
        ScoreBoard scoreboard = new ScoreBoard();
        int roundNo = 1;
        for (RoundStrategy strategy : strategies) {
            System.out.println("\nRound " + roundNo++);
            Round round = new Round(players, config, strategy);
            round.start(scoreboard);
            scoreboard.displayScores();
        }
    }

    public static class GameBuilder {
        private final List<Player> players = new ArrayList<>();
        private final List<RoundStrategy> strategies = new ArrayList<>();
        private GameConfig config = new StandardConfig();

        public GameBuilder addPlayer(Player p) {
            players.add(p);
            return this;
        }

        public GameBuilder addRound(RoundStrategy t) {
            strategies.add(t);
            return this;
        }

        public Game build() {
            return new Game(players, strategies, config);
        }
    }
}

public class BowlingsGameSystem {
    public static void main(String[] args) {
        Game.GameBuilder builder = new Game.GameBuilder();

        // Add players
        builder.addPlayer(new Player("Alice"));
        builder.addPlayer(new Player("Bob"));

        // Add 9 normal rounds
        for (int i = 0; i < 2; i++) {
            builder.addRound(new NormalRoundStrategy());
        }

        // Add final round
        builder.addRound(new LastRoundRoundStrategy());

        // Build and start game
        Game game = builder.build();
        game.start();

        InputHelper.SCANNER.close(); // Close only after game ends
    }
}
