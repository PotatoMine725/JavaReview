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
                // Xử lý Reset khi vừa ra kết quả
                if (daRaKetQua) {
                    // Nếu nhập toán tử -> Giữ kết quả cũ để tính tiếp (VD: 5 + )
                    // Nếu nhập số/hàm/hằng -> Reset biểu thức (VD: bấm 2 -> màn hình là 2)
                    if (command.matches("[0-9]") || command.equals("π") || command.equals("e") || command.equals("√") || command.equals("(")) {
                        bieuThuc = ""; 
                    }
                    daRaKetQua = false;
                }

                // --- LOGIC CHẶN NHIỀU DẤU CHẤM (Giữ nguyên từ lần trước) ---
                if (command.equals(".")) {
                    boolean hasDot = false;
                    for (int i = bieuThuc.length() - 1; i >= 0; i--) {
                        char c = bieuThuc.charAt(i);
                        if (!Character.isDigit(c) && c != '.') {
                            break; 
                        }
                        if (c == '.') {
                            hasDot = true;
                            break;
                        }
                    }
                    if (hasDot) return; // Nếu đã có chấm thì bỏ qua
                }
                
                String textToAdd = command;
                if (command.equals("√")) textToAdd = "sqrt";
                
                // --- LOGIC MỚI: XỬ LÝ SỐ 0 Ở ĐẦU ---
                // Chỉ thay thế số 0 nếu nhập: Số, Hằng số, Hàm (sqrt), Ngoặc mở
                // Giữ lại số 0 (Append) nếu nhập: Toán tử (+ - * / // ^), Dấu chấm, Ngoặc đóng
                
                boolean isOperator = command.matches("[+\\-*/^]") || command.equals("//");
                boolean isDot = command.equals(".");
                boolean isCloser = command.equals(")");
                
                if (bieuThuc.equals("0") && !isOperator && !isDot && !isCloser) {
                    bieuThuc = textToAdd; // Thay thế (VD: 0 -> 5, 0 -> sqrt)
                } else {
                    bieuThuc += textToAdd; // Nối thêm (VD: 0 -> 0+, 0 -> 0^, 0 -> 0.)
                }
                view.setDisplayText(bieuThuc);
            }
        }
    }
}