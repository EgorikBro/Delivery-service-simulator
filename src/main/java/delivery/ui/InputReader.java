package delivery.ui;

import java.util.Scanner;

public class InputReader {

    private final Scanner scanner = new Scanner(System.in);

    public String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Значение не может быть пустым, попробуйте ещё раз");
        }
    }

    public int readInt(String prompt) {
        while (true) {
            String line = readText(prompt);
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            String line = readText(prompt).replace(',', '.');
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Введите число, например 199.90");
            }
        }
    }
}
