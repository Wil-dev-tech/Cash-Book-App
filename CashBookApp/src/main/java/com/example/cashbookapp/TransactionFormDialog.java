package com.example.cashbookapp;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class TransactionFormDialog extends Dialog<Transaction> {
    private final TextField dateField = new TextField();
    private final TextField categoryField = new TextField();
    private final TextField orgField = new TextField();
    private final TextField particularsField = new TextField();
    private final TextField amountField = new TextField();
    private final ComboBox<String> typeCombo = new ComboBox<>();

    public TransactionFormDialog() {
        this(null);
    }

    public TransactionFormDialog(Transaction tx) {
        setTitle(tx == null ? "Add Transaction" : "Edit Transaction");
        setHeaderText(null);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        typeCombo.getItems().addAll("C", "D");

        grid.add(new Label("Date (MM/DD/YYYY):"), 0, 0);
        grid.add(dateField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(new Label("Organization:"), 0, 2);
        grid.add(orgField, 1, 2);
        grid.add(new Label("Particulars:"), 0, 3);
        grid.add(particularsField, 1, 3);
        grid.add(new Label("Amount:"), 0, 4);
        grid.add(amountField, 1, 4);
        grid.add(new Label("Type (C/D):"), 0, 5);
        grid.add(typeCombo, 1, 5);

        if (tx != null) {
            dateField.setText(tx.date);
            categoryField.setText(tx.category);
            orgField.setText(tx.organization);
            particularsField.setText(tx.particulars);
            amountField.setText(String.valueOf(tx.amount));
            typeCombo.setValue(tx.type);
        }

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    return new Transaction(
                            dateField.getText(),
                            categoryField.getText(),
                            orgField.getText(),
                            particularsField.getText(),
                            Double.parseDouble(amountField.getText()),
                            typeCombo.getValue()
                    );
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid input. Please check all fields.").showAndWait();
                    return null;
                }
            }
            return null;
        });
    }
}
