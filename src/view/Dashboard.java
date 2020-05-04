package view;

import util.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.SQLException;

public class Dashboard {
    private final JFrame frame;

    private JTable table;
    private DefaultTableModel model;

    public Dashboard() {
        frame = new JFrame("Dashboard");
        frame.setSize(1080,720);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton addProductButton = new JButton("Add Product");
        addProductButton.setBounds(330,50,150, 40);
        addProductButton.addActionListener(actionEvent -> new AddProductDetailsView(model));

        JButton btnDelete = new JButton("Delete selected product");
        btnDelete.setBounds(530,50,300, 40);

        showTableData();

        btnDelete.addActionListener(actionEvent -> {
            int i = table.getSelectedRow();
            Database db = new Database();
            db.query = "DELETE FROM `products` WHERE id=?";

            try {
                db.ps = db.getConnection().prepareStatement(db.query);
                db.ps.setString(1, model.getValueAt(i, 0).toString());
                db.ps.executeUpdate();
                model.fireTableDataChanged();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            db.close();
            model.removeRow(i);
        });

        frame.add(addProductButton);
        frame.add(btnDelete);

        frame.setVisible(true);
    }

    public void showTableData() {
        String[] columnNames = {"ID", "Name", "Man Date", "Exp Date", "Price", "Total Stock"};
        model = new DefaultTableModel() {
            public void fireTableDataChanged() {
                super.fireTableDataChanged();
                setRowCount(0);
                updateTable();
            }

            @Override
            public boolean isCellEditable(int row, int column)
            {
                return column!=0;
            }

            @Override
            public void setValueAt(Object value, int row, int column) {
                super.setValueAt(value, row, column);
                Database db = new Database();
                db.query = "UPDATE `products` SET name=?, man_date=?, exp_date=?, price=?, total_stock=? WHERE id=?";

                try {
                    db.ps = db.getConnection().prepareStatement(db.query);
                    db.ps.setString(1, String.valueOf(getValueAt(row, 1).toString()));
                    db.ps.setDate(2, Date.valueOf(getValueAt(row, 2).toString()));
                    db.ps.setDate(3, Date.valueOf(getValueAt(row, 3).toString()));
                    db.ps.setFloat(4, Float.parseFloat(getValueAt(row, 4).toString()));
                    db.ps.setInt(5, Integer.parseInt(getValueAt(row, 5).toString()));
                    db.ps.setInt(6, Integer.parseInt(getValueAt(row, 0).toString()));
                    db.ps.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                db.close();
            }
        };

        model.setColumnIdentifiers(columnNames);

        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(50,100,frame.getWidth() - 100, frame.getHeight() - 200);

        updateTable();

        frame.add(scroll);
    }

    public void updateTable() {
        try {
            Database db = new Database();
            db.query = "SELECT * FROM `products`";
            db.ps = db.getConnection().prepareStatement(db.query);
            db.rs = db.ps.executeQuery();

            while (db.rs.next()) {
                model.addRow(new Object[]{
                        db.rs.getInt("id"),
                        db.rs.getString("name"),
                        db.rs.getString("man_date"),
                        db.rs.getString("exp_date"),
                        db.rs.getString("price"),
                        db.rs.getString("total_stock")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
