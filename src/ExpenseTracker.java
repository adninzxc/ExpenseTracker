import java.util.List;
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
                case "summary" -> System.out.printf(Locale.US, "Total expenses: $%.2f%n",
                        TransactionStorage.summary());
                case "month" -> {
                    System.out.print("Enter month (1-12): ");

                    try {
                        int month = Integer.parseInt(scanner.nextLine().trim());
                        if (month < 1 || month > 12) {
                            System.out.println("Invalid month. Please enter a number between 1 and 12.");
                        } else {
                            List<TransactionStorage.Transaction> monthlyTransactions =
                                    TransactionStorage.getTransactionsByMonth(month);

                            if (monthlyTransactions.isEmpty()) {
                                System.out.println("No transactions found for month " + month);
                            } else {
                                System.out.println("\nTransactions for month " + month + ":");
                                System.out.println("#ID - Date       - Description - Amount");
                                for (TransactionStorage.Transaction transaction : monthlyTransactions) {
                                    System.out.println(transaction);
                                }
                                System.out.println(String.format(Locale.US,
                                        "Total for month %d: $%.2f", month, TransactionStorage.summaryByMonth(month)));
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
                case "export" -> {
                    System.out.print("Enter filename for CSV export (or press Enter for default 'expenses.csv'): ");
                    String exportFilename = scanner.nextLine().trim();
                    if (exportFilename.isEmpty()) {
                        exportFilename = "expenses.csv";
                    }
                    if (!exportFilename.toLowerCase().endsWith(".csv")) {
                        exportFilename += ".csv";
                    }
                    TransactionStorage.exportTransactionsToCSV(exportFilename);
                }
                default -> {

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
        System.out.println("  month   - View expenses for a specific month of the current year");
        System.out.println("  export  - Export all expenses to a CSV file");
        System.out.println("  help    - Show this help message");
        System.out.println("  exit    - Quit the application");
    }
}