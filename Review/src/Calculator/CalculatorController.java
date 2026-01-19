package Calculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class CalculatorController {
    private CalculatorView view;
    private CalculatorModel model;
    
    private String bieuThuc = "";
    private boolean daRaKetQua = false;
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

            if (command.equals("C")) {
                bieuThuc = "";
                view.setDisplayText("0");
                view.setHistoryText("");
            }
            else if (command.equals("Del")) {
                if (!bieuThuc.isEmpty()) {
                    // Xóa thông minh: nếu cuối là sqrt hoặc // thì xóa cả cụm
                    if (bieuThuc.endsWith("sqrt")) {
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 4);
                    } else if (bieuThuc.endsWith("//")) {
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 2);
                    } else {
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 1);
                    }
                    view.setDisplayText(bieuThuc.isEmpty() ? "0" : bieuThuc);
                }
            }
            else if (command.equals("=")) {
                try {
                    view.setHistoryText(bieuThuc + " =");
                    double ketQua = model.calculate(bieuThuc);
                    
                    // Làm đẹp kết quả: 5.0 -> 5
                    String ketQuaString = df.format(ketQua);
                    view.setDisplayText(ketQuaString);
                    
                    bieuThuc = ketQuaString;
                    daRaKetQua = true;
                } catch (Exception ex) {
                    view.setDisplayText("Lỗi");
                    bieuThuc = "";
                }
            }
            else {
                // Nhập liệu
                if (daRaKetQua) {
                    // Nếu nhập số hoặc hàm mới -> Reset. Nếu nhập toán tử -> Giữ kết quả để tính tiếp
                    if (command.matches("[0-9]") || command.equals("π") || command.equals("e") || command.equals("√") || command.equals("(")) {
                        bieuThuc = ""; 
                    }
                    daRaKetQua = false;
                }
                
                // Chuyển đổi ký tự hiển thị sang ký tự Model hiểu
                String textToAdd = command;
                if (command.equals("√")) textToAdd = "sqrt";
                
                if (bieuThuc.equals("0") && !command.equals(".")) {
                    bieuThuc = textToAdd;
                } else {
                    bieuThuc += textToAdd;
                }
                view.setDisplayText(bieuThuc);
            }
        }
    }
}