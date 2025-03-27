import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TransactionManager {

    static void addExpense(Scanner scanner, String filename) {
        System.out.print("Enter expense description: ");
        String description = scanner.nextLine();

        double amount = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter expense amount: ");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                scanner.nextLine();
                validInput = true;
            } else {
                System.out.println("Invalid amount. Please enter a valid number.");
                scanner.nextLine();
            }
        }

        int transactionId = getNextTransactionId();
        Date currentDate = new Date();

        TransactionStorage.addTransaction(new TransactionStorage.Transaction(
                transactionId, currentDate, description, amount
        ));

        TransactionStorage.saveTransactionsToFile(filename);
        System.out.println("Transaction added with ID: " + transactionId);

        String warning = TransactionStorage.warning();
        if (!warning.isEmpty()) {
            System.out.println(warning);
        }
    }

    static void updateExpense(Scanner scanner, String filename) {
        System.out.print("Enter the ID of the expense you want to update: ");

        int expenseId;
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid expense ID: ");
            scanner.next();
        }
        expenseId = scanner.nextInt();
        scanner.nextLine();

        int index = TransactionStorage.findTransactionIndexById(expenseId);

        if (index == -1) {
            System.out.println("Expense ID not found.");
            return;
        }

        System.out.print("Enter new description: ");
        String newDescription = scanner.nextLine();

        double newAmount = 0;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Enter new amount: ");
            if (scanner.hasNextDouble()) {
                newAmount = scanner.nextDouble();
                scanner.nextLine();
                validAmount = true;
            } else {
                System.out.println("Invalid amount. Please enter a valid number.");
                scanner.next();
            }
        }

        Date currentDate = new Date();
        TransactionStorage.updateTransaction(index,
                new TransactionStorage.Transaction(expenseId, currentDate, newDescription, newAmount)
        );

        TransactionStorage.saveTransactionsToFile(filename);

        System.out.println("Expense updated successfully!");

        String warning = TransactionStorage.warning();
        if (!warning.isEmpty()) {
            System.out.println(warning);
        }
    }

    static void deleteExpense(Scanner scanner, String filename) {
        System.out.print("Enter the ID of the expense you want to delete: ");

        int expenseId;
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid expense ID: ");
            scanner.next();
        }
        expenseId = scanner.nextInt();
        scanner.nextLine();

        int index = TransactionStorage.findTransactionIndexById(expenseId);

        if (index == -1) {
            System.out.println("Expense ID not found.");
            return;
        }

        TransactionStorage.removeTransaction(index);
        TransactionStorage.saveTransactionsToFile(filename);
        System.out.println("Expense deleted successfully!");
    }

    static void listTransactions() {
        List<TransactionStorage.Transaction> transactions = TransactionStorage.getAllTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }


        System.out.println("#ID - Date       - Description - Amount");
        for (TransactionStorage.Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public static int getNextTransactionId() {
        List<TransactionStorage.Transaction> transactions = TransactionStorage.getAllTransactions();

        if (transactions.isEmpty()) {
            return 1;
        }

        int maxId = 0;
        for (TransactionStorage.Transaction transaction : transactions) {
            if (transaction.id() > maxId) {
                maxId = transaction.id();
            }
        }
        return maxId + 1;
    }

    public static void setBudget(Scanner scanner) {
        System.out.print("Enter budget limit: ");

        double limit = 0.0;
        boolean validInput = false;

        while (!validInput) {
            if (scanner.hasNextDouble()) {
                limit = scanner.nextDouble();
                scanner.nextLine();
                validInput = true;
            } else {
                System.out.println("Invalid amount. Please enter a valid number.");
                scanner.next();
                scanner.nextLine();
            }
        }

        TransactionStorage.setBudget(limit);
        System.out.printf("Budget set to $%.2f%n", limit);

        String warning = TransactionStorage.warning();
        if (!warning.isEmpty()) {
            System.out.println(warning);
        }
    }
}