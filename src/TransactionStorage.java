import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionStorage {
    private static final List<Transaction> transactions = new ArrayList<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);

    public record Transaction(int id, Date date, String description, double amount) {

        @Override
            public String toString() {
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                return String.format(Locale.US, "# %d - %s - %s -   $%.2f",
                        id, simpleFormat.format(date), description, amount);
            }
        }

    public static List<Transaction> getAllTransactions() {
        return transactions;
    }

    public static void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public static void updateTransaction(int index, Transaction transaction) {
        transactions.set(index, transaction);
    }

    public static void removeTransaction(int index) {
        transactions.remove(index);
    }

    public static int findTransactionIndexById(int id) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).id() == id) {
                return i;
            }
        }
        return -1;
    }

    public static double summary() {
        double total = 0.0;

        for (Transaction transaction : transactions) {
            total += transaction.amount();
        }

        return total;
    }
    public static void loadTransactionsFromFile(String filename) {
        transactions.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    try {
                        line = line.substring(1).trim();
                        String[] parts = line.split(" - ");

                        if (parts.length >= 4) {
                            int id = Integer.parseInt(parts[0].trim());

                            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(parts[1].trim());

                            String description = parts[2].trim();

                            String amountStr = parts[3].trim().replace("$", "").replace(",", ".");
                            double amount = Double.parseDouble(amountStr);

                            transactions.add(new Transaction(id, date, description, amount));
                        }
                    } catch (Exception e) {
                        System.out.println("Warning: Could not parse line: " + line);
                    }
                } else {
                    try {
                        String[] parts = line.split(" \\| ");
                        if (parts.length >= 4) {
                            int id = Integer.parseInt(parts[0].trim());

                            Date date;
                            String dateStr = parts[1].trim();
                            try {
                                date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateStr);
                            } catch (ParseException e) {
                                try {
                                    date = dateFormat.parse(dateStr);
                                } catch (ParseException ex) {
                                    System.out.println("Warning: Could not parse date '" + dateStr + "', using current date");
                                    date = new Date();
                                }
                            }

                            String description = parts[2].trim();

                            String amountStr = parts[3].trim().replace(',', '.');
                            double amount = Double.parseDouble(amountStr);

                            transactions.add(new Transaction(id, date, description, amount));
                        }
                    } catch (Exception e) {
                        System.out.println("Warning: Could not parse line: " + line);
                    }
                }
            }
            System.out.println("Loaded " + transactions.size() + " transactions from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No transaction file found. Starting with empty list.");
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    public static void saveTransactionsToFile(String filename) {
        try (FileWriter fileWriter = new FileWriter(filename);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            for (Transaction transaction : transactions) {
                printWriter.println(transaction.toString());
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static double summaryByMonth(int month) {
        double total = 0.0;
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        for (Transaction transaction : transactions) {
            cal.setTime(transaction.date());
            int transactionMonth = cal.get(Calendar.MONTH) + 1;
            int transactionYear = cal.get(Calendar.YEAR);

            if (transactionMonth == month && transactionYear == currentYear) {
                total += transaction.amount();
            }
        }

        return total;
    }

    public static List<Transaction> getTransactionsByMonth(int month) {
        List<Transaction> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        for (Transaction transaction : transactions) {
            cal.setTime(transaction.date());
            int transactionMonth = cal.get(Calendar.MONTH) + 1;
            int transactionYear = cal.get(Calendar.YEAR);

            if (transactionMonth == month && transactionYear == currentYear) {
                result.add(transaction);
            }
        }

        return result;
    }
}