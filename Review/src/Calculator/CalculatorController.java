package Calculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class CalculatorController {
    private CalculatorView view;
    private CalculatorModel model;
    
    // Biến lưu trữ biểu thức đang nhập (VD: "2 + ( 3 * 5 )")
    private String bieuThuc = "";
    private boolean daRaKetQua = false; // Cờ kiểm tra xem vừa tính xong chưa

    private DecimalFormat df = new DecimalFormat("#.##########");

    public CalculatorController(CalculatorView view, CalculatorModel model) {
        this.view = view;
        this.model = model;
        this.view.addCalculationListener(new CalculatorListener());
    }

    class CalculatorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            // 1. Nút XÓA HẾT (C)
            if (command.equals("C")) {
                bieuThuc = "";
                view.setDisplayText("0");
                view.setHistoryText("");
            }
            // 2. Nút XÓA 1 KÝ TỰ (Del)
            else if (command.equals("Del")) {
                if (!bieuThuc.isEmpty()) {
                    bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 1);
                    view.setDisplayText(bieuThuc.isEmpty() ? "0" : bieuThuc);
                }
            }
            // 3. Nút BẰNG (=) - Gọi Model tính toán
            else if (command.equals("=")) {
                try {
                    // Hiển thị biểu thức đầy đủ lên dòng history
                    view.setHistoryText(bieuThuc + " =");
                    
                    double ketQua = model.calculate(bieuThuc);
                    String ketQuaString = df.format(ketQua);
                    
                    view.setDisplayText(ketQuaString);
                    
                    // Lưu kết quả làm đầu vào cho phép tính tiếp theo
                    bieuThuc = ketQuaString;
                    daRaKetQua = true;
                    
                } catch (Exception ex) {
                    view.setDisplayText("Lỗi biểu thức");
                    bieuThuc = "";
                }
            }
            // 4. Các nút SỐ và TOÁN TỬ (+, -, *, /, (, ))
            else {
                // Nếu vừa ra kết quả mà người dùng bấm số -> Reset biểu thức mới
                // Nếu vừa ra kết quả mà bấm toán tử -> Dùng kết quả cũ tính tiếp
                if (daRaKetQua) {
                    if (command.matches("[0-9]")) {
                        bieuThuc = ""; 
                    }
                    daRaKetQua = false;
                }
                
                // Tránh số 0 ở đầu (trừ khi là số thập phân 0.)
                if (bieuThuc.equals("0") && !command.equals(".")) {
                    bieuThuc = command;
                } else {
                    bieuThuc += command;
                }
                
                view.setDisplayText(bieuThuc);
            }
        }
    }
}