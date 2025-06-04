package LLD.ChessGame.V1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

enum Color {WHITE, BLACK}
enum GameState {RUNNING, OVER}
enum ValidationEnum {PASSED, STALEMATE, OWN_KING_IN_CHECK, OPPONENT_IN_CHECK, CHECK_N_MATE}

class ValidateGameDAO {
    private String message;
    private ValidationEnum validationState;
    
    public ValidateGameDAO(ValidationEnum state, String message) {
        this.validationState = state;
        this.message = message;
    }
    
    public String getMessage() { return message; }
    public ValidationEnum getValidationState() { return validationState; }
}

class Move {
    private Position from;
    private Position to;
    private Piece piece;
    private Piece capturedPiece;
    
    public Move(Position from, Position to, Piece piece, Piece capturedPiece) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.capturedPiece = capturedPiece;
    }
    
    public Position getFrom() { return from; }
    public Position getTo() { return to; }
    public Piece getPiece() { return piece; }
    public Piece getCapturedPiece() { return capturedPiece; }
}

abstract class Piece {
    private String name;
    private Position position;
    private Color color;
    private Board board;
    private boolean hasMoved = false;

    public Piece(String name, Position position, Color color, Board board) {
        this.name = name;
        this.position = position;
        this.color = color;
        this.board = board;
    }

    abstract List<Position> getValidPositions();

    public String getName() { return name; }
    public Position getPosition() { return position; }
    public Color getColor() { return color; }
    public Board getBoard() { return board; }
    public boolean hasMoved() { return hasMoved; }
    
    public void setPosition(Position position) { 
        this.position = position; 
        this.hasMoved = true;
    }
    
    public void setBoard(Board board) { this.board = board; }
}

class King extends Piece {
    public King(Position position, Color color, Board board) {
        super("King", position, color, board);
    }

    @Override
    public List<Position> getValidPositions() {
        List<Position> positions = new ArrayList<>();
        Position currentPos = getPosition();
        
        // King moves one square in any direction
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
        
        for (int[] dir : directions) {
            Position newPos = new Position(currentPos.getRow() + dir[0], currentPos.getCol() + dir[1]);
            if (getBoard().isValid(newPos)) {
                if (getBoard().isEmpty(newPos) || getBoard().hasEnemyAt(newPos, getColor())) {
                    positions.add(newPos);
                }
            }
        }
        
        return positions;
    }
}

class Queen extends Piece {
    public Queen(Position position, Color color, Board board) {
        super("Queen", position, color, board);
    }

    @Override
    public List<Position> getValidPositions() {
        List<Position> positions = new ArrayList<>();
        Position currentPos = getPosition();
        
        // Queen combines rook and bishop moves
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
        
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                Position newPos = new Position(currentPos.getRow() + dir[0]*i, currentPos.getCol() + dir[1]*i);
                if (!getBoard().isValid(newPos)) break;
                
                if (getBoard().isEmpty(newPos)) {
                    positions.add(newPos);
                } else if (getBoard().hasEnemyAt(newPos, getColor())) {
                    positions.add(newPos);
                    break;
                } else {
                    break; // Own piece
                }
            }
        }
        
        return positions;
    }
}

class Rook extends Piece {
    public Rook(Position position, Color color, Board board) {
        super("Rook", position, color, board);
    }

    @Override
    public List<Position> getValidPositions() {
        List<Position> positions = new ArrayList<>();
        Position currentPos = getPosition();
        
        // Rook moves horizontally and vertically
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                Position newPos = new Position(currentPos.getRow() + dir[0]*i, currentPos.getCol() + dir[1]*i);
                if (!getBoard().isValid(newPos)) break;
                
                if (getBoard().isEmpty(newPos)) {
                    positions.add(newPos);
                } else if (getBoard().hasEnemyAt(newPos, getColor())) {
                    positions.add(newPos);
                    break;
                } else {
                    break; // Own piece
                }
            }
        }
        
        return positions;
    }
}

class Bishop extends Piece {
    public Bishop(Position position, Color color, Board board) {
        super("Bishop", position, color, board);
    }

    @Override
    public List<Position> getValidPositions() {
        List<Position> positions = new ArrayList<>();
        Position currentPos = getPosition();
        
        // Bishop moves diagonally
        int[][] directions = {{-1,-1}, {-1,1}, {1,-1}, {1,1}};
        
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                Position newPos = new Position(currentPos.getRow() + dir[0]*i, currentPos.getCol() + dir[1]*i);
                if (!getBoard().isValid(newPos)) break;
                
                if (getBoard().isEmpty(newPos)) {
                    positions.add(newPos);
                } else if (getBoard().hasEnemyAt(newPos, getColor())) {
                    positions.add(newPos);
                    break;
                } else {
                    break; // Own piece
                }
            }
        }
        
        return positions;
    }
}

class Knight extends Piece {
    public Knight(Position position, Color color, Board board) {
        super("Knight", position, color, board);
    }

    @Override
    public List<Position> getValidPositions() {
        List<Position> positions = new ArrayList<>();
        Position currentPos = getPosition();
        
        // Knight moves in L-shape
        int[][] moves = {{-2,-1}, {-2,1}, {-1,-2}, {-1,2}, {1,-2}, {1,2}, {2,-1}, {2,1}};
        
        for (int[] move : moves) {
            Position newPos = new Position(currentPos.getRow() + move[0], currentPos.getCol() + move[1]);
            if (getBoard().isValid(newPos)) {
                if (getBoard().isEmpty(newPos) || getBoard().hasEnemyAt(newPos, getColor())) {
                    positions.add(newPos);
                }
            }
        }
        
        return positions;
    }
}

class Pawn extends Piece {
    public Pawn(Position position, Color color, Board board) {
        super("Pawn", position, color, board);
    }

    @Override
    public List<Position> getValidPositions() {
        List<Position> positions = new ArrayList<>();
        Position pos = getPosition();
        int direction = this.getColor() == Color.WHITE ? 1 : -1;
        boolean isInitialPos = (getColor() == Color.WHITE && pos.getRow() == 1) || 
                              (getColor() == Color.BLACK && pos.getRow() == 6);

        // Forward moves
        Position oneForward = new Position(pos.getRow() + direction, pos.getCol());
        if (getBoard().isValid(oneForward) && getBoard().isEmpty(oneForward)) {
            positions.add(oneForward);
            
            // Two squares forward from initial position
            if (isInitialPos) {
                Position twoForward = new Position(pos.getRow() + direction*2, pos.getCol());
                if (getBoard().isValid(twoForward) && getBoard().isEmpty(twoForward)) {
                    positions.add(twoForward);
                }
            }
        }
        
        // Diagonal captures
        Position leftDiagonal = new Position(pos.getRow() + direction, pos.getCol() - 1);
        if (getBoard().isValid(leftDiagonal) && getBoard().hasEnemyAt(leftDiagonal, getColor())) {
            positions.add(leftDiagonal);
        }
        
        Position rightDiagonal = new Position(pos.getRow() + direction, pos.getCol() + 1);
        if (getBoard().isValid(rightDiagonal) && getBoard().hasEnemyAt(rightDiagonal, getColor())) {
            positions.add(rightDiagonal);
        }
        
        return positions;
    }
}

class Position {
    private int row, col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }
    
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}

interface Board {
    int getRowCount();
    int getColCount();
    boolean hasEnemyAt(Position position, Color color);
    boolean isEmpty(Position position);
    boolean isValid(Position position);
    Piece getPiece(Position pos);
    boolean makeMove(Move move);
    void undoMove(Move move);
    King getKing(Color color);
    List<Piece> getAllPieces(Color color);
    void printBoard();
}

class ChessBoard implements Board {
    private Piece[][] board;
    private final int ROWS = 8;
    private final int COLS = 8;
    
    public ChessBoard() {
        board = new Piece[ROWS][COLS];
        initializeBoard();
    }
    
    private void initializeBoard() {
        // Initialize white pieces
        board[0][0] = new Rook(new Position(0, 0), Color.WHITE, this);
        board[0][1] = new Knight(new Position(0, 1), Color.WHITE, this);
        board[0][2] = new Bishop(new Position(0, 2), Color.WHITE, this);
        board[0][3] = new Queen(new Position(0, 3), Color.WHITE, this);
        board[0][4] = new King(new Position(0, 4), Color.WHITE, this);
        board[0][5] = new Bishop(new Position(0, 5), Color.WHITE, this);
        board[0][6] = new Knight(new Position(0, 6), Color.WHITE, this);
        board[0][7] = new Rook(new Position(0, 7), Color.WHITE, this);
        
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(new Position(1, i), Color.WHITE, this);
        }
        
        // Initialize black pieces
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(new Position(6, i), Color.BLACK, this);
        }
        
        board[7][0] = new Rook(new Position(7, 0), Color.BLACK, this);
        board[7][1] = new Knight(new Position(7, 1), Color.BLACK, this);
        board[7][2] = new Bishop(new Position(7, 2), Color.BLACK, this);
        board[7][3] = new Queen(new Position(7, 3), Color.BLACK, this);
        board[7][4] = new King(new Position(7, 4), Color.BLACK, this);
        board[7][5] = new Bishop(new Position(7, 5), Color.BLACK, this);
        board[7][6] = new Knight(new Position(7, 6), Color.BLACK, this);
        board[7][7] = new Rook(new Position(7, 7), Color.BLACK, this);
    }

    @Override
    public int getRowCount() { return ROWS; }

    @Override
    public int getColCount() { return COLS; }

    @Override
    public boolean hasEnemyAt(Position position, Color color) {
        if (!isValid(position)) return false;
        Piece piece = getPiece(position);
        return piece != null && piece.getColor() != color;
    }

    @Override
    public boolean isEmpty(Position position) {
        if (!isValid(position)) return false;
        return board[position.getRow()][position.getCol()] == null;
    }

    @Override
    public boolean isValid(Position position) {
        return position.getRow() >= 0 && position.getRow() < ROWS && 
               position.getCol() >= 0 && position.getCol() < COLS;
    }

    @Override
    public Piece getPiece(Position pos) {
        if (!isValid(pos)) return null;
        return board[pos.getRow()][pos.getCol()];
    }

    @Override
    public boolean makeMove(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        
        if (!isValid(from) || !isValid(to)) return false;
        
        Piece piece = getPiece(from);
        if (piece == null) return false;
        
        // Execute move
        board[from.getRow()][from.getCol()] = null;
        board[to.getRow()][to.getCol()] = piece;
        piece.setPosition(to);
        
        return true;
    }

    @Override
    public void undoMove(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Piece piece = move.getPiece();
        Piece capturedPiece = move.getCapturedPiece();
        
        // Undo move
        board[to.getRow()][to.getCol()] = capturedPiece;
        board[from.getRow()][from.getCol()] = piece;
        piece.setPosition(from);
    }

    @Override
    public King getKing(Color color) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Piece piece = board[i][j];
                if (piece instanceof King && piece.getColor() == color) {
                    return (King) piece;
                }
            }
        }
        return null;
    }

    @Override
    public List<Piece> getAllPieces(Color color) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.getColor() == color) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    @Override
    public void printBoard() {
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int i = 7; i >= 0; i--) {
            System.out.print(i + " ");
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece == null) {
                    System.out.print(". ");
                } else {
                    char symbol = getPieceSymbol(piece);
                    System.out.print(symbol + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private char getPieceSymbol(Piece piece) {
        char symbol = '?';
        String name = piece.getName().toLowerCase();
        switch (name) {
            case "king": symbol = 'K'; break;
            case "queen": symbol = 'Q'; break;
            case "rook": symbol = 'R'; break;
            case "bishop": symbol = 'B'; break;
            case "knight": symbol = 'N'; break;
            case "pawn": symbol = 'P'; break;
        }
        return piece.getColor() == Color.WHITE ? symbol : Character.toLowerCase(symbol);
    }
}

class Player {
    private final Color color;
    private final String name;
    private List<Piece> capturedPieces;
    private PlayerStrategy playStrategy;

    public Player(String name, Color color, PlayerStrategy playStrategy) {
        this.name = name;
        this.color = color;
        this.playStrategy = playStrategy;
        this.capturedPieces = new ArrayList<>();
    }

    public Color getColor() { return color; }
    public String getName() { return name; }
    public List<Piece> getCapturedPieces() { return capturedPieces; }
    
    public Move makeAMove(Board board) {
        return playStrategy.makeAMove(this, board);
    }
    
    public void addCapturedPiece(Piece piece) {
        capturedPieces.add(piece);
    }
}

interface PlayerStrategy {
    Move makeAMove(Player player, Board board);
}

class ConsolePlayer implements PlayerStrategy {
    private Scanner sc = new Scanner(System.in);

    @Override
    public Move makeAMove(Player player, Board board) {
        Position fromPos, toPos;
        Piece piece;

        System.out.println("\n" + player.getName() + "'s turn (" + player.getColor() + ")");
        board.printBoard();

        while (true) {
            System.out.print("Enter piece current row (0-7): ");
            int row = sc.nextInt();
            System.out.print("Enter piece current col (0-7): ");
            int col = sc.nextInt();
            fromPos = new Position(row, col);

            if (board.isEmpty(fromPos)) {
                System.out.println("No piece at this position, retry...");
                continue;
            }
            
            piece = board.getPiece(fromPos);
            if (piece == null || piece.getColor() != player.getColor()) {
                System.out.println("Chosen invalid piece, retry...");
            } else break;
        }

        List<Position> validPositions = piece.getValidPositions();
        System.out.println("Valid moves: " + validPositions);

        while (true) {
            System.out.print("Enter target row (0-7): ");
            int row = sc.nextInt();
            System.out.print("Enter target col (0-7): ");
            int col = sc.nextInt();
            toPos = new Position(row, col);

            if (!validPositions.contains(toPos)) {
                System.out.println("Invalid target position, retry...");
            } else break;
        }

        Piece capturedPiece = board.getPiece(toPos);
        return new Move(fromPos, toPos, piece, capturedPiece);
    }
}

class EasyAIPlayer implements PlayerStrategy {
    @Override
    public Move makeAMove(Player player, Board board) {
        List<Piece> pieces = board.getAllPieces(player.getColor());
        for (Piece piece : pieces) {
            List<Position> validMoves = piece.getValidPositions();
            if (!validMoves.isEmpty()) {
                Position to = validMoves.get(0);
                Piece capturedPiece = board.getPiece(to);
                return new Move(piece.getPosition(), to, piece, capturedPiece);
            }
        }
        return null; // No valid moves
    }
}

class PlayerFactory {
    public static Player makePlayer(String name, String type, Color color) {
        switch (type.toLowerCase()) {
            case "human":
                return new Player(name, color, new ConsolePlayer());
            case "easy-ai":
                return new Player(name, color, new EasyAIPlayer());
            default:
                return new Player(name, color, new ConsolePlayer());
        }
    }
}

class Game {
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private GameState state;
    private List<BoardChangeRule> boardChangeRules;
    private List<GameStateValidator> gameStateCheckRules;

    public Game() {
        this.board = new ChessBoard();
        this.whitePlayer = PlayerFactory.makePlayer("White Player", "human", Color.WHITE);
        this.blackPlayer = PlayerFactory.makePlayer("Black Player", "human", Color.BLACK);
        this.currentPlayer = whitePlayer;
        this.state = GameState.RUNNING;
        this.boardChangeRules = new ArrayList<>();
        this.gameStateCheckRules = Arrays.asList(new CheckForCheck());
    }

    public void start() {
        System.out.println("Chess Game Started!");
        
        while (state != GameState.OVER) {
            Move move = currentPlayer.makeAMove(board);
            if (move == null) {
                System.out.println("No valid moves available!");
                state = GameState.OVER;
                break;
            }

            boolean wrongTurn = false;

            for (GameStateValidator rule : gameStateCheckRules) {
                ValidateGameDAO res = rule.evaluate(move, currentPlayer, board);
                if (res.getValidationState() != ValidationEnum.PASSED) {
                    switch (res.getValidationState()) {
                        case OWN_KING_IN_CHECK:
                            wrongTurn = true;
                            System.out.println("Can't make this move, own king would be in check.");
                            break;
                        case OPPONENT_IN_CHECK:
                            System.out.println("Great move, opponent is in check.");
                            break;
                        case CHECK_N_MATE:
                            System.out.println(currentPlayer.getName() + " Wins, checkmate!");
                            state = GameState.OVER;
                            break;
                        case STALEMATE:
                            state = GameState.OVER;
                            System.out.println("No valid moves available, Game Draw (Stalemate)");
                            break;
                    }

                    if (state == GameState.OVER || wrongTurn) break;
                }
            }

            if (wrongTurn) continue;

            // Execute the move
            board.makeMove(move);
            if (move.getCapturedPiece() != null) {
                currentPlayer.addCapturedPiece(move.getCapturedPiece());
            }

            for (BoardChangeRule rule : boardChangeRules) {
                rule.apply(currentPlayer, board);
            }

            switchPlayer();
        }
        
        System.out.println("Game Over!");
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer == whitePlayer ? blackPlayer : whitePlayer;
    }
}

interface BoardChangeRule {
    boolean apply(Player player, Board board);
}

interface GameStateValidator {
    ValidateGameDAO evaluate(Move move, Player player, Board board);
}

class CheckForCheck implements GameStateValidator {
    
    @Override
    public ValidateGameDAO evaluate(Move move, Player player, Board board) {
        // Temporarily make the move
        board.makeMove(move);
        
        // Check if own king is in check after the move
        if (isKingInCheck(player.getColor(), board)) {
            board.undoMove(move);
            return new ValidateGameDAO(ValidationEnum.OWN_KING_IN_CHECK, "Move puts own king in check");
        }
        
        // Check if opponent king is in check
        Color opponentColor = player.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE;
        boolean opponentInCheck = isKingInCheck(opponentColor, board);
        
        if (opponentInCheck) {
            // Check if it's checkmate
            if (isCheckmate(opponentColor, board)) {
                board.undoMove(move);
                return new ValidateGameDAO(ValidationEnum.CHECK_N_MATE, "Checkmate!");
            }
            board.undoMove(move);
            return new ValidateGameDAO(ValidationEnum.OPPONENT_IN_CHECK, "Opponent in check");
        }
        
        board.undoMove(move);
        return new ValidateGameDAO(ValidationEnum.PASSED, "Valid move");
    }
    
    private boolean isKingInCheck(Color kingColor, Board board) {
        King king = board.getKing(kingColor);
        if (king == null) return false;
        
        Color opponentColor = kingColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        List<Piece> opponentPieces = board.getAllPieces(opponentColor);
        
        for (Piece piece : opponentPieces) {
            List<Position> validMoves = piece.getValidPositions();
            if (validMoves.contains(king.getPosition())) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isCheckmate(Color kingColor, Board board) {
        if (!isKingInCheck(kingColor, board)) return false;
        
        List<Piece> pieces = board.getAllPieces(kingColor);
        for (Piece piece : pieces) {
            List<Position> validMoves = piece.getValidPositions();
            for (Position move : validMoves) {
                Move testMove = new Move(piece.getPosition(), move, piece, board.getPiece(move));
                board.makeMove(testMove);
                boolean stillInCheck = isKingInCheck(kingColor, board);
                board.undoMove(testMove);
                
                if (!stillInCheck) {
                    return false; // Found a valid move
                }
            }
        }
        
        return true; // No valid moves found
    }
}

public class ChessGame {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}