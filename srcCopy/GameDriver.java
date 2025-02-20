import java.util.Scanner;

public class GameDriver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("******************** Welcome to Uno Game! ********************");
        System.out.print("Please Enter The Number of Players: ");
        int players = scanner.nextInt();
        Game g = new ClassicUno(players);
        g.play();
        System.out.println("******************** End of Game! ********************");
    }

}