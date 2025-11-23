package org.openjfx.EECS_3311_Project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.openjfx.EECS_3311_Project.Mediator;
import org.openjfx.EECS_3311_Project.model.AccountRole;

import java.util.List;
import java.util.UUID;

public class RolesPageController {

    @FXML
    private VBox rolesList;

    @FXML
    public void initialize() {
        loadRoles();
    }

    private void loadRoles() {
        rolesList.getChildren().clear();

        List<AccountRole> roles = Mediator.getInstance()
                .getAccountRoleManager()
                .getAccountRoles();

        for (AccountRole role : roles) {
            rolesList.getChildren().add(createRoleRow(role));
        }
    }

    private HBox createRoleRow(AccountRole role) {

        HBox row = new HBox(20);

        TextField nameField = new TextField(role.getRoleName());
        TextField rateField = new TextField(Double.toString(role.getHourlyRate()));

        Button saveBtn = new Button("Save");
        Button deleteBtn = new Button("Delete");

        saveBtn.setOnAction(e -> {
            role.setRoleName(nameField.getText());
            role.setHourlyRate(Double.parseDouble(rateField.getText()));

            Mediator.getInstance().getAccountRoleManager().upsertAccountRole(role);
            loadRoles();
        });

        deleteBtn.setOnAction(e -> {
            Mediator.getInstance().getAccountRoleManager().removeAccountRole(role);
            loadRoles();
        });

        row.getChildren().addAll(nameField, rateField, saveBtn, deleteBtn);
        return row;
    }

    @FXML
    private void onAddRole() {
        AccountRole newRole = new AccountRole(
        		"New Role",                    
                UUID.randomUUID().toString(),  
                0.0
        );

        Mediator.getInstance().getAccountRoleManager().upsertAccountRole(newRole);
        loadRoles();
    }
}
