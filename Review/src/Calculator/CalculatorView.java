package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CalculatorView extends JFrame {
    private JLabel lblHistory;
    private JLabel lblDisplay;
    private JButton[] buttons;
    
    // Cập nhật danh sách nút: Thêm (, ), Del (Xóa lùi)
    private String[] buttonLabels = {
        "(", ")", "Del", "C",  // Hàng 1
        "7", "8", "9", "/",    // Hàng 2
        "4", "5", "6", "*",    // Hàng 3
        "1", "2", "3", "-",    // Hàng 4
        "0", ".", "=", "+"     // Hàng 5
    };

    public CalculatorView() {
        setTitle("Máy tính học sinh");
        setSize(340, 500); // Tăng chiều cao để chứa thêm nút
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- MÀN HÌNH ---
        JPanel pnlScreen = new JPanel(new GridLayout(2, 1));
        pnlScreen.setBackground(Color.WHITE);
        pnlScreen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlScreen.setPreferredSize(new Dimension(300, 100));

        lblHistory = new JLabel("");
        lblHistory.setFont(new Font("Arial", Font.PLAIN, 18));
        lblHistory.setForeground(Color.GRAY);
        
        lblDisplay = new JLabel("0");
        lblDisplay.setFont(new Font("Arial", Font.BOLD, 40));
        lblDisplay.setHorizontalAlignment(SwingConstants.RIGHT);

        pnlScreen.add(lblHistory);
        pnlScreen.add(lblDisplay);
        
        JPanel containerScreen = new JPanel(new BorderLayout());
        containerScreen.add(pnlScreen, BorderLayout.CENTER);
        containerScreen.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        add(containerScreen, BorderLayout.NORTH);

        // --- BÀN PHÍM ---
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(5, 4, 8, 8)); // 5 dòng, 4 cột
        panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 18));
            buttons[i].setFocusPainted(false);
            
            String label = buttonLabels[i];
            if (label.equals("C")) {
                buttons[i].setBackground(new Color(255, 100, 100));
                buttons[i].setForeground(Color.WHITE);
            } else if (label.equals("=")) {
                buttons[i].setBackground(new Color(0, 150, 255));
                buttons[i].setForeground(Color.WHITE);
            } else if (label.equals("Del")) {
                buttons[i].setBackground(new Color(255, 180, 100)); // Màu cam cho nút xóa
            } else if ("+-*/()".contains(label)) {
                buttons[i].setBackground(new Color(230, 230, 230));
            } else {
                buttons[i].setBackground(Color.WHITE);
            }
            
            panelButtons.add(buttons[i]);
        }

        add(panelButtons, BorderLayout.CENTER);
    }

    public String getDisplayText() { return lblDisplay.getText(); }
    public void setDisplayText(String text) { lblDisplay.setText(text); }
    public void setHistoryText(String text) { lblHistory.setText(text); }
    
    public void addCalculationListener(ActionListener listener) {
        for (JButton btn : buttons) btn.addActionListener(listener);
    }
}