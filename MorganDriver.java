import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

public class MorganDriver {
    public static void main(String[] args) {
        String fileName = "boards/valid10.dat";
        try {
            CircuitBoard myBoard = new CircuitBoard(fileName);
            System.out.println("Board is valid");
            System.out.println(myBoard.toString());

        } catch (FileNotFoundException e) {
            System.out.println("File is not found");
        } catch (InvalidFileFormatException e) {
            System.out.println("File is not in correct format. Please fix and try again.");
        } catch (NoSuchElementException e) {
            System.out.println("File is not in correct format. Please fix and try again.");
        }
    }
}
