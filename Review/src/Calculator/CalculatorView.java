package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CalculatorView extends JFrame {
    private JTextField txtDisplay;
    private JButton[] buttons;
    private String[] buttonLabels = {
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "0", "C", "=", "+"
    };

    public CalculatorView() {
        setTitle("Máy tính bỏ túi");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Màn hình hiển thị
        txtDisplay = new JTextField();
        txtDisplay.setFont(new Font("Arial", Font.BOLD, 24));
        txtDisplay.setHorizontalAlignment(JTextField.RIGHT);
        txtDisplay.setEditable(false);
        txtDisplay.setBackground(Color.WHITE);
        add(txtDisplay, BorderLayout.NORTH);

        // Bàn phím số và phép tính
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(4, 4, 5, 5)); // 4 hàng, 4 cột, khoảng cách 5px
        
        buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 18));
            
            // Tô màu cho nút C và = để nổi bật
            if (buttonLabels[i].equals("C")) buttons[i].setBackground(Color.RED);
            else if (buttonLabels[i].equals("=")) buttons[i].setBackground(Color.CYAN);
            else buttons[i].setBackground(Color.LIGHT_GRAY);
            
            panelButtons.add(buttons[i]);
        }

        add(panelButtons, BorderLayout.CENTER);
    }

    // Hàm lấy nội dung trên màn hình
    public String getDisplayText() {
        return txtDisplay.getText();
    }

    // Hàm set nội dung lên màn hình
    public void setDisplayText(String text) {
        txtDisplay.setText(text);
    }

    // Gán sự kiện lắng nghe cho tất cả các nút
    public void addCalculationListener(ActionListener listener) {
        for (JButton btn : buttons) {
            btn.addActionListener(listener);
        }
    }
}