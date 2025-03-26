# Expense Tracker

A simple command-line application for tracking personal expenses.

## Features

- Add new expenses with descriptions and amounts
- Update existing expenses
- Delete expenses
- List all expenses
- View total expense summary
- Data persistence through file storage

## Usage

```
Available commands:
  add     - Add an expense with a description and amount
  update  - Update an expense by ID
  delete  - Delete an expense by ID
  list    - List all expenses
  summary - Show the total sum of all expenses
  help    - Show this help message
  exit    - Quit the application
```

## Project Structure

- `ExpenseTracker.java` - Main application with command loop
- `TransactionManager.java` - Handles user interactions and command processing
- `TransactionStorage.java` - Manages transaction data and file persistence

## Acknowledgments

This project was inspired by the [Expense Tracker project idea](https://roadmap.sh/projects/expense-tracker) from roadmap.sh.
