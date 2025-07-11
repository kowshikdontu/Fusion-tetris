import java.util.Scanner;

public class GameController {
    private GameBoard board;
    public GameController() { board = new GameBoard(); }

    //    public void startGame() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Fusion Tetris Started!");
//        while (true) {
//            board.printBoard();
//            System.out.print("Enter block type to drop (fire, water, earth, air, nopower) or 'exit': ");
//            String input = scanner.nextLine().trim().toLowerCase();
//            scanner.next();
//            System.out.println("enter the column: ");
//            int n = scanner.nextInt();
//
//
//            if (input.equals("exit")) break;
//            dropBlock(input,n);
//        }
//        scanner.close();
//        System.out.println("Game Over.");
//    }
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Fusion Tetris Started!");

        while (true) {
            System.out.print("Enter block type to drop (fire, water, earth, air, nopower) or 'exit': ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) break;

            System.out.print("Enter the column (0-9): ");
            int col;
            try {
                col = Integer.parseInt(scanner.nextLine().trim());
                if (col < 0 || col > 9) {
                    System.out.println("Invalid column. Try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            dropBlock(input, col);
        }

        scanner.close();
        System.out.println("Game Over.");
    }
    public GameBoard getBoard() {
        return board;
    }

    public void updateBoard() {
        board.updateBoard();
    }

    public void dropBlock(String type, int col) {

        Block block = BlockFactory.createBlock(type, new Position(0, col));
        if (board.dropBlock(block, col)) {
            board.printBoard();
            board.updateBoard();
        } else {
            System.out.println("Column is full. Try another column.");
        }
    }

}