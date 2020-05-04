package util;

import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;

public class FormValidator {

    public static boolean validateField(JTextField f, String errormsg)
    {
        if (f.getText().equals("")) {
            failedMessage(f, errormsg);
            return false;
        }

        return true;
    }

    public static boolean validateDate(JDatePickerImpl f, String errormsg)
    {
        if (f.getModel().getValue() == null) {
            failedMessage(errormsg);
            return false;
        }

        return true;
    }

    public static boolean validateInteger(JTextField f, String errormsg)
    {
        try
        {
            int i = Integer.parseInt(f.getText());

            if (i > 0)
                return true;
        }
        catch (Exception e) {
            failedMessage(f, errormsg);
        }

        return false;
    }

    public static boolean validateFloat(JTextField f, String errormsg)
    {
        try
        {
            float i = Float.parseFloat(f.getText());

            if (i > 0)
                return true;
        }
        catch (Exception e) {
            failedMessage(f, errormsg);
        }

        return false;
    }

    public static void failedMessage(String errormsg)
    {
        JOptionPane.showMessageDialog(null, errormsg);
    }

    public static void failedMessage(JTextField f, String errormsg)
    {
        JOptionPane.showMessageDialog(null, errormsg);
        f.requestFocus();
    }
}
