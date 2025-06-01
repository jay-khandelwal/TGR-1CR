package LLD.BowlingGame.V2;

import java.util.*;

// Game configuration interface
interface GameConfig {
    int getTotalPins();
    int getMaxFrames();
}

// Standard bowling configuration
class StandardConfig implements GameConfig {
    @Override
    public int getTotalPins() {
        return 10;
    }

    @Override
    public int getMaxFrames() {
        return 10;
    }
}

// Player class
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

// Frame class (replaces Round and Chance)
class Frame {
    private List<Integer> rolls;
    private boolean isLastFrame;
    private GameConfig config;

    public Frame(GameConfig config, boolean isLastFrame) {
        this.config = config;
        this.isLastFrame = isLastFrame;
        this.rolls = new ArrayList<>();
    }

    public void play(Player player) {
        System.out.println("Frame for " + player.getName());
        int remainingPins = config.getTotalPins();

        // First roll
        int firstRoll = getValidRoll(player, remainingPins);
        rolls.add(firstRoll);
        remainingPins -= firstRoll;

        // Second roll if not a strike
        if (firstRoll < config.getTotalPins()) {
            int secondRoll = getValidRoll(player, remainingPins);
            rolls.add(secondRoll);
            remainingPins -= secondRoll;
        }

        // Bonus rolls for last frame
        if (isLastFrame && (isStrike() || isSpare())) {
            int bonusRoll1 = getValidRoll(player, config.getTotalPins());
            rolls.add(bonusRoll1);
            if (isStrike()) { // Extra bonus roll for strike
                int bonusRoll2 = getValidRoll(player, config.getTotalPins());
                rolls.add(bonusRoll2);
            }
        }
    }

    private int getValidRoll(Player player, int maxPins) {
        while (true) {
            try {
                System.out.println(player.getName() + ", enter pins (0-" + maxPins + "): ");
                int pins = InputHelper.SCANNER.nextInt();
                if (pins >= 0 && pins <= maxPins) {
                    return pins;
                }
                System.out.println("Invalid input. Enter 0 to " + maxPins);
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                InputHelper.SCANNER.next(); // Clear invalid input
            }
        }
    }

    public boolean isStrike() {
        return rolls.size() > 0 && rolls.get(0) == config.getTotalPins();
    }

    public boolean isSpare() {
        return rolls.size() > 1 && !isStrike() && (rolls.get(0) + rolls.get(1) == config.getTotalPins());
    }

    public List<Integer> getRolls() {
        return rolls;
    }
}

// Scoreboard class
class ScoreBoard {
    private final Map<Player, List<Frame>> playerFrames = new HashMap<>();

    public void addFrame(Player player, Frame frame) {
        playerFrames.computeIfAbsent(player, k -> new ArrayList<>()).add(frame);
    }

    public int calculateScore(Player player) {
        List<Frame> frames = playerFrames.getOrDefault(player, new ArrayList<>());
        int totalScore = 0;

        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            List<Integer> rolls = frame.getRolls();
            int frameScore = rolls.stream().mapToInt(Integer::intValue).sum();

            // Strike bonus: next two rolls
            if (frame.isStrike() && i < frames.size() - 1) {
                Frame nextFrame = frames.get(i + 1);
                List<Integer> nextRolls = nextFrame.getRolls();
                frameScore += nextRolls.get(0);
                if (nextRolls.size() > 1) {
                    frameScore += nextRolls.get(1);
                } else if (i + 1 < frames.size() - 1) {
                    frameScore += frames.get(i + 2).getRolls().get(0);
                }
            }
            // Spare bonus: next roll
            else if (frame.isSpare() && i < frames.size() - 1) {
                frameScore += frames.get(i + 1).getRolls().get(0);
            }

            totalScore += frameScore;
        }
        return totalScore;
    }

    public void displayScores() {
        System.out.println("\nFinal Scores:");
        for (Player player : playerFrames.keySet()) {
            System.out.println(player.getName() + ": " + calculateScore(player));
        }
        System.out.println();
    }
}

// Game class
class Game {
    private List<Player> players;
    private GameConfig config;
    private ScoreBoard scoreboard;

    public Game(List<Player> players, GameConfig config) {
        this.players = players;
        this.config = config;
        this.scoreboard = new ScoreBoard();
    }

    public void start() {
        for (int frameNum = 1; frameNum <= config.getMaxFrames(); frameNum++) {
            System.out.println("\nFrame " + frameNum);
            boolean isLastFrame = (frameNum == config.getMaxFrames());
            for (Player player : players) {
                Frame frame = new Frame(config, isLastFrame);
                frame.play(player);
                scoreboard.addFrame(player, frame);
            }
        }
        scoreboard.displayScores();
    }

    public static class GameBuilder {
        private final List<Player> players = new ArrayList<>();
        private GameConfig config = new StandardConfig();

        public GameBuilder addPlayer(Player player) {
            players.add(player);
            return this;
        }

        public Game build() {
            return new Game(players, config);
        }
    }
}

// Main class
public class BowlingGameSystem {
    public static void main(String[] args) {
        Game.GameBuilder builder = new Game.GameBuilder();
        builder.addPlayer(new Player("Alice"))
               .addPlayer(new Player("Bob"));
        Game game = builder.build();
        game.start();
        InputHelper.SCANNER.close();
    }
}