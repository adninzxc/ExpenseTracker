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
                case "budget" -> {
                    System.out.println("\nBudget Options:");
                    System.out.println("1 - Set overall budget");
                    System.out.println("2 - Set monthly budget");
                    System.out.println("3 - View monthly budgets");
                    System.out.print("Select an option (1-3): ");

                    String option = scanner.nextLine().trim();
                    switch (option) {
                        case "1" -> TransactionManager.setBudget(scanner);
                        case "2" -> TransactionManager.setMonthlyBudget(scanner);
                        case "3" -> TransactionManager.showMonthlyBudgets();
                        default -> System.out.println("Invalid option.");
                    }
                }
                case "summary" -> {
                    System.out.printf(Locale.US, "Total expenses: $%.2f%n", TransactionStorage.summary());

                    double budget = TransactionStorage.getBudget();
                    if (budget > 0) {
                        System.out.printf(Locale.US, "Budget limit: $%.2f%n", budget);
                        double remaining = budget - TransactionStorage.summary();
                        if (remaining >= 0) {
                            System.out.printf(Locale.US, "Remaining budget: $%.2f%n", remaining);
                        } else {
                            System.out.printf(Locale.US, "Over budget by: $%.2f%n", Math.abs(remaining));
                        }
                    }

                    String warning = TransactionStorage.warning();
                    if (!warning.isEmpty()) {
                        System.out.println(warning);
                    }
                }
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

                                double monthlyTotal = TransactionStorage.summaryByMonth(month);
                                System.out.printf(Locale.US, "Total for month %d: $%.2f%n", month, monthlyTotal);

                                if (TransactionStorage.hasMonthlyBudget(month)) {
                                    double budget = TransactionStorage.getMonthlyBudget(month);
                                    System.out.printf(Locale.US, "Budget for month %d: $%.2f%n", month, budget);

                                    double remaining = budget - monthlyTotal;
                                    if (remaining >= 0) {
                                        System.out.printf(Locale.US, "Remaining budget: $%.2f%n", remaining);
                                    } else {
                                        System.out.printf(Locale.US, "Over budget by: $%.2f%n", Math.abs(remaining));
                                        System.out.println(TransactionStorage.monthlyWarning(month));
                                    }
                                } else {
                                    System.out.println("No budget set for this month.");
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
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
        System.out.println("  budget  - Budget management (overall and monthly)");
        System.out.println("  summary - Show the total sum of all expenses");
        System.out.println("  month   - View expenses for a specific month of the current year");
        System.out.println("  help    - Show this help message");
        System.out.println("  exit    - Quit the application");
    }
}