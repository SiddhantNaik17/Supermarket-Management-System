package view;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import util.Database;
import util.FormValidator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddProductDetailsView {
    private final JTextField prodNameField, prodPriceField, totalStockField;
    private final JDatePickerImpl manDatePicker, expDatePicker;

    public AddProductDetailsView(DefaultTableModel model) {
        JFrame frame = new JFrame();

        JLabel prodNameLabel = new JLabel("Product Name: ");
        prodNameLabel.setBounds(50, 100, 200, 30);
        prodNameField = new JTextField();
        prodNameField.setBounds(170, 100, 200, 30);

        JLabel manDateLabel = new JLabel("Manufacturing Date: ");
        manDateLabel.setBounds(50, 150, 200, 30);
        SqlDateModel manDateModel = new SqlDateModel();
        JDatePanelImpl manDatePanel = new JDatePanelImpl(manDateModel);
        manDatePicker = new JDatePickerImpl(manDatePanel);
        manDatePicker.setBounds(170, 150, 200, 30);

        JLabel expDateLabel = new JLabel("Expiry Date: ");
        expDateLabel.setBounds(50, 200, 200, 30);
        SqlDateModel expDateModel = new SqlDateModel();
        JDatePanelImpl expDatePanel = new JDatePanelImpl(expDateModel);
        expDatePicker = new JDatePickerImpl(expDatePanel);
        expDatePicker.setBounds(170, 200, 200, 30);

        JLabel prodPriceLabel = new JLabel("Price: ");
        prodPriceLabel.setBounds(50, 250, 200, 30);
        prodPriceField = new JTextField();
        prodPriceField.setBounds(170, 250, 200, 30);

        JLabel totalStockLabel = new JLabel("Total Stock: ");
        totalStockLabel.setBounds(50, 300, 200, 30);
        totalStockField = new JTextField();
        totalStockField.setBounds(170, 300, 200, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(250, 400, 100, 30);
        submitButton.addActionListener(actionEvent -> {
            Database db = new Database();
            db.query = "INSERT INTO `products` (name, man_date, exp_date, price, total_stock)" +
                    "VALUES (?, ?, ?, ?, ?)";

            if (!validateFields())
                return;

            try {
                db.ps = db.getConnection().prepareStatement(db.query);
                db.ps.setString(1, String.valueOf(prodNameField.getText()));
                db.ps.setDate(2, (Date) manDatePicker.getModel().getValue());
                db.ps.setDate(3, (Date) expDatePicker.getModel().getValue());
                db.ps.setFloat(4, Float.parseFloat(prodPriceField.getText()));
                db.ps.setInt(5, Integer.parseInt(totalStockField.getText()));
                db.ps.executeUpdate();
                model.fireTableDataChanged();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            db.close();
            frame.dispose();
        });

        frame.add(prodNameLabel);
        frame.add(prodNameField);
        frame.add(manDateLabel);
        frame.add(manDatePicker);
        frame.add(expDateLabel);
        frame.add(expDatePicker);
        frame.add(prodPriceLabel);
        frame.add(prodPriceField);
        frame.add(totalStockLabel);
        frame.add(totalStockField);
        frame.add(submitButton);

        frame.setSize(400,500);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public boolean validateFields()
    {
        if (!FormValidator.validateField(prodNameField, "Please enter the product name"))
            return false;
        else if (!FormValidator.validateDate(manDatePicker, "Please enter the product manufacturing date"))
            return false;
        else if (!FormValidator.validateDate(expDatePicker, "Please enter the product expiry date"))
            return false;
        else if (!FormValidator.validateFloat(prodPriceField, "Please enter the product price"))
            return false;
        else return FormValidator.validateInteger(totalStockField, "Please enter the total product stocks");
    }
}
