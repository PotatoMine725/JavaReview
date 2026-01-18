package Calculator;

import javax.swing.SwingUtilities;

public class CalculatorApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorView view = new CalculatorView();
            CalculatorModel model = new CalculatorModel();
            
            // Khởi tạo controller để kết nối View và Model
            new CalculatorController(view, model);
            
            view.setVisible(true);
        });
    }
}