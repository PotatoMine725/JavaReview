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
                    // Xóa thông minh cho các hàm dài
                    if (bieuThuc.endsWith("sqrt")) {
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 4);
                    } else if (bieuThuc.endsWith("sin") || bieuThuc.endsWith("cos") || 
                               bieuThuc.endsWith("tan") || bieuThuc.endsWith("log")) {
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 3);
                    } else if (bieuThuc.endsWith("ln") || bieuThuc.endsWith("//")) {
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
                    
                    if (Double.isInfinite(ketQua) || Double.isNaN(ketQua)) {
                        view.setDisplayText("Lỗi");
                        bieuThuc = ""; 
                    } else {
                        String ketQuaString = df.format(ketQua);
                        view.setDisplayText(ketQuaString);
                        bieuThuc = ketQuaString;
                        daRaKetQua = true;
                    }
                } catch (Exception ex) {
                    view.setDisplayText("Lỗi");
                    bieuThuc = "";
                }
            }
            else {
                // Nhập liệu
                if (daRaKetQua) {
                    // Nếu nhập số, hằng, hoặc HÀM mới -> Reset
                    if (command.matches("[0-9]") || command.matches("π|e|√|\\(|sin|cos|tan|log|ln")) {
                        bieuThuc = ""; 
                    }
                    daRaKetQua = false;
                }

                if (command.equals(".")) {
                    boolean hasDot = false;
                    for (int i = bieuThuc.length() - 1; i >= 0; i--) {
                        char c = bieuThuc.charAt(i);
                        if (!Character.isDigit(c) && c != '.') break; 
                        if (c == '.') { hasDot = true; break; }
                    }
                    if (hasDot) return;
                }
                
                // Mapping ký tự đặc biệt sang text cho Model
                String textToAdd = command;
                if (command.equals("√")) textToAdd = "sqrt";
                // Các nút sin, cos, tan, log, ln giữ nguyên text
                
                boolean isOperator = command.matches("[+\\-*/^]") || command.equals("//");
                boolean isDot = command.equals(".");
                boolean isCloser = command.equals(")");
                
                if (bieuThuc.equals("0") && !isOperator && !isDot && !isCloser) {
                    bieuThuc = textToAdd; 
                } else {
                    bieuThuc += textToAdd;
                }
                view.setDisplayText(bieuThuc);
            }
        }
    }
}