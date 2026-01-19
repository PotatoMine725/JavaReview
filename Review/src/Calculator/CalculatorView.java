package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CalculatorView extends JFrame {
    private JLabel lblHistory;
    private JLabel lblDisplay;
    private JButton[] buttons;
    
    // Cập nhật danh sách nút: Thêm √, ^, //, π, e
    // Bố cục 5 cột x 5 hàng
    private String[] buttonLabels = {
        "√",   "^",   "(",   ")",   "C",
        "7",   "8",   "9",   "/",   "Del",
        "4",   "5",   "6",   "*",   "//",
        "1",   "2",   "3",   "-",   "π",
        "0",   ".",   "=",   "+",   "e"
    };

    public CalculatorView() {
        setTitle("Máy tính Khoa học");
        setSize(400, 550); // Tăng kích thước bề ngang và dọc
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- MÀN HÌNH ---
        JPanel pnlScreen = new JPanel(new GridLayout(2, 1));
        pnlScreen.setBackground(Color.WHITE);
        pnlScreen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlScreen.setPreferredSize(new Dimension(300, 120));

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
        panelButtons.setLayout(new GridLayout(5, 5, 8, 8)); // 5 dòng, 5 cột
        panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 18));
            buttons[i].setFocusPainted(false);
            
            String label = buttonLabels[i];
            
            // Tô màu các nhóm nút
            if (label.equals("C")) {
                buttons[i].setBackground(new Color(255, 100, 100)); // Đỏ
                buttons[i].setForeground(Color.WHITE);
            } else if (label.equals("=")) {
                buttons[i].setBackground(new Color(0, 150, 255)); // Xanh dương
                buttons[i].setForeground(Color.WHITE);
            } else if (label.equals("Del")) {
                buttons[i].setBackground(new Color(255, 160, 0)); // Cam
                buttons[i].setForeground(Color.WHITE);
            } else if ("0123456789.".contains(label)) {
                buttons[i].setBackground(Color.WHITE); // Số màu trắng
                buttons[i].setFont(new Font("Arial", Font.BOLD, 22)); // Số to hơn chút
            } else {
                buttons[i].setBackground(new Color(230, 230, 230)); // Các nút chức năng màu xám nhẹ
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