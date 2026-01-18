package Calculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorController {
    private CalculatorView view;
    private CalculatorModel model;

    private double soDau = 0;
    private String toanTu = "";
    private boolean startNewNumber = true; // Cờ để kiểm tra khi nào nhập số mới sau khi bấm phép tính

    public CalculatorController(CalculatorView view, CalculatorModel model) {
        this.view = view;
        this.model = model;

        // Gắn sự kiện cho các nút trong View
        this.view.addCalculationListener(new CalculatorListener());
    }

    class CalculatorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            // Nếu người dùng bấm số
            if (command.matches("[0-9]")) {
                if (startNewNumber) {
                    view.setDisplayText(command);
                    startNewNumber = false;
                } else {
                    view.setDisplayText(view.getDisplayText() + command);
                }
            } 
            // Nếu người dùng bấm nút xóa (C)
            else if (command.equals("C")) {
                view.setDisplayText("");
                soDau = 0;
                toanTu = "";
                startNewNumber = true;
            } 
            // Nếu người dùng bấm dấu bằng (=)
            else if (command.equals("=")) {
                if (toanTu.isEmpty()) return;
                
                try {
                    double soSau = Double.parseDouble(view.getDisplayText());
                    double ketQua = model.calculate(soDau, soSau, toanTu);
                    
                    // Kiểm tra nếu kết quả là số nguyên thì hiển thị kiểu int cho đẹp
                    if (ketQua == (long) ketQua) {
                        view.setDisplayText(String.format("%d", (long) ketQua));
                    } else {
                        view.setDisplayText(String.valueOf(ketQua));
                    }
                    
                    // Reset để chuẩn bị cho phép tính tiếp theo dựa trên kết quả này
                    startNewNumber = true;
                    toanTu = "";
                } catch (ArithmeticException ex) {
                    view.setDisplayText("Lỗi");
                    startNewNumber = true;
                }
            } 
            // Nếu người dùng bấm các toán tử (+, -, *, /)
            else {
                if (!view.getDisplayText().isEmpty()) {
                    soDau = Double.parseDouble(view.getDisplayText());
                    toanTu = command;
                    startNewNumber = true;
                }
            }
        }
    }
}