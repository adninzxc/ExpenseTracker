import java.util.Locale;
import java.util.Scanner;

public class ExpenseTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean interactive = true;
        String filename = "transactions.txt";
        TransactionStorage.loadTransactionsFromFile(filename);
        System.out.println("Welcome to ExpenseTracker! Type 'help' for available commands.");

        while (interactive) {
            System.out.print("\nexpense-tracker command: ");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "exit", "quit" -> {
                    System.out.println("Goodbye!");
                    interactive = false;
                }
                case "help" -> showHelp();
                case "add" -> TransactionManager.addExpense(scanner, filename);
                case "update" -> TransactionManager.updateExpense(scanner, filename);
                case "delete" -> TransactionManager.deleteExpense(scanner, filename);
                case "list" -> TransactionManager.listTransactions();
                case "summary" -> System.out.printf(Locale.US, "Total expenses: $%.2f%n", TransactionStorage.summary());                default -> {
                    if (!input.isEmpty()) {
                        System.out.println("Unknown command. Type 'help' for available commands.");
                    }
                }
            }
        }
        scanner.close();
    }

    private static void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  add     - Add an expense with a description and amount");
        System.out.println("  update  - Update an expense by ID");
        System.out.println("  delete  - Delete an expense by ID");
        System.out.println("  list    - List all expenses");
        System.out.println("  summary - Show the total sum of all expenses");
        System.out.println("  help    - Show this help message");
        System.out.println("  exit    - Quit the application");
    }
}