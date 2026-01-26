package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CalculatorView extends JFrame {
    private JLabel lblHistory;
    private JLabel lblDisplay;
    private JButton btnMode; 
    private JButton[] buttons;
    
    // Cập nhật: Thay nút "e" bằng "x" ở góc dưới cùng bên phải
    private String[] buttonLabels = {
        "sin", "cos", "tan", "log", "ln",
        "√",   "^",   "(",   ")",   "C",
        "7",   "8",   "9",   "/",   "Del",
        "4",   "5",   "6",   "*",   "Ans",
        "1",   "2",   "3",   "-",   "π",
        "0",   ".",   "=",   "+",   "x"  // Thay e bằng x
    };

    public CalculatorView() {
        setTitle("Máy tính Bỏ túi");
        setSize(420, 630);
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

        // --- TOOLBAR ---
        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlToolbar.setBackground(new Color(245, 245, 245));
        
        btnMode = new JButton("Rad");
        btnMode.setFont(new Font("Arial", Font.BOLD, 12));
        btnMode.setBackground(Color.LIGHT_GRAY);
        btnMode.setFocusPainted(false);
        btnMode.setPreferredSize(new Dimension(60, 25));
        
        pnlToolbar.add(new JLabel("Mode: "));
        pnlToolbar.add(btnMode);
        
        JPanel pnlBody = new JPanel(new BorderLayout());
        pnlBody.add(pnlToolbar, BorderLayout.NORTH);
        
        // --- BUTTONS ---
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(6, 5, 8, 8));
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
                buttons[i].setBackground(new Color(255, 160, 0));
                buttons[i].setForeground(Color.WHITE);
            } else if ("0123456789.".contains(label)) {
                buttons[i].setBackground(Color.WHITE);
                buttons[i].setFont(new Font("Arial", Font.BOLD, 22));
            } else if (label.equals("Ans")) {
                buttons[i].setBackground(new Color(100, 200, 100)); 
                buttons[i].setForeground(Color.WHITE);
            } else if (label.equals("x")) {
                buttons[i].setBackground(new Color(255, 100, 255)); // Màu tím cho x
                buttons[i].setForeground(Color.WHITE);
                buttons[i].setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 20));
            } else if (label.matches("sin|cos|tan|log|ln|√|\\^|\\(|\\)|π")) {
                 buttons[i].setBackground(new Color(220, 240, 255));
            } else {
                buttons[i].setBackground(new Color(230, 230, 230));
            }
            
            panelButtons.add(buttons[i]);
        }
        
        pnlBody.add(panelButtons, BorderLayout.CENTER);
        add(pnlBody, BorderLayout.CENTER);
    }

    public String getDisplayText() { return lblDisplay.getText(); }
    public void setDisplayText(String text) { lblDisplay.setText(text); }
    public void setHistoryText(String text) { lblHistory.setText(text); }
    
    public void updateModeButton(boolean isDegree) {
        if (isDegree) {
            btnMode.setText("Deg");
            btnMode.setBackground(new Color(255, 200, 100));
        } else {
            btnMode.setText("Rad");
            btnMode.setBackground(Color.LIGHT_GRAY);
        }
    }

    public void addCalculationListener(ActionListener listener) {
        for (JButton btn : buttons) btn.addActionListener(listener);
    }
    public void addModeListener(ActionListener listener) {
        btnMode.addActionListener(listener);
    }
}