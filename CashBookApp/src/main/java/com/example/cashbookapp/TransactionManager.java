package com.example.cashbookapp;

import javafx.collections.ObservableList;

public class TransactionManager {

    public static void addTransaction(ObservableList<Transaction> transactions, Transaction tx, CashBalance balance) {
        transactions.add(tx);
        adjustBalance(tx, false, balance);
    }

    public static void editTransaction(ObservableList<Transaction> transactions, Transaction oldTx, Transaction newTx, CashBalance balance) {
        adjustBalance(oldTx, true, balance); // revert
        transactions.set(transactions.indexOf(oldTx), newTx);
        adjustBalance(newTx, false, balance); // apply
    }

    public static void deleteTransaction(ObservableList<Transaction> transactions, Transaction tx, CashBalance balance) {
        transactions.remove(tx);
        adjustBalance(tx, true, balance);
    }

    public static void clearTransactions(ObservableList<Transaction> transactions, CashBalance balance) {
        transactions.clear();
        balance.value = 000.00;
    }

    private static void adjustBalance(Transaction tx, boolean revert, CashBalance balance) {
        if (tx.getType().equals("C")) {
            balance.value += revert ? tx.getAmount() : -tx.getAmount();
        } else {
            balance.value += revert ? -tx.getAmount() : tx.getAmount();
        }
    }
}
