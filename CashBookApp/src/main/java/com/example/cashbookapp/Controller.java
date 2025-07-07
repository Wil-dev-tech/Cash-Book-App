package com.example.cashbookapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Controller {
    @FXML private TableView<Transaction> table;
    @FXML private TableColumn<Transaction, String> dateCol;
    @FXML private TableColumn<Transaction, String> categoryCol;
    @FXML private TableColumn<Transaction, String> orgCol;
    @FXML private TableColumn<Transaction, String> particularsCol;
    @FXML private TableColumn<Transaction, Double> amountCol;
    @FXML private TableColumn<Transaction, String> typeCol;
    @FXML private Label balanceLabel;

    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private final CashBalance balance = new CashBalance();

    @FXML
    public void initialize() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        orgCol.setCellValueFactory(new PropertyValueFactory<>("organization"));
        particularsCol.setCellValueFactory(new PropertyValueFactory<>("particulars"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        table.setItems(transactions);
        updateBalanceLabel();
    }

    @FXML
    private void onAdd() {
        TransactionFormDialog dialog = new TransactionFormDialog();
        dialog.showAndWait().ifPresent(tx -> {
            TransactionManager.addTransaction(transactions, tx, balance);
            updateBalanceLabel();
        });
    }

    @FXML
    private void onEdit() {
        Transaction selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        TransactionFormDialog dialog = new TransactionFormDialog(selected);
        dialog.showAndWait().ifPresent(tx -> {
            TransactionManager.editTransaction(transactions, selected, tx, balance);
            updateBalanceLabel();
        });
    }

    @FXML
    private void onDelete() {
        Transaction selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        TransactionManager.deleteTransaction(transactions, selected, balance);
        updateBalanceLabel();
    }

    @FXML
    private void onClearAll() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Clear all transactions?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(button -> {
            if (button == ButtonType.YES) {
                TransactionManager.clearTransactions(transactions, balance);
                updateBalanceLabel();
            }
        });
    }

    @FXML
    private void onSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Transactions");
        fileChooser.setInitialFileName("cashbook.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Date,Category,Organization,Particulars,Amount,Type");
                writer.newLine();
                for (Transaction tx : transactions) {
                    writer.write(String.format("%s,%s,%s,%s,%.2f,%s",
                            tx.getDate(),
                            tx.getCategory(),
                            tx.getOrganization(),
                            tx.getParticulars(),
                            tx.getAmount(),
                            tx.getType()
                    ));
                    writer.newLine();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transactions saved successfully.");
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save transactions.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void updateBalanceLabel() {
        balanceLabel.setText(String.format("Balance: $%.2f", balance.value));
    }
}
