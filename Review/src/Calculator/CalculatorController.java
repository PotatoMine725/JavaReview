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
                    
                    // [CẬP NHẬT] Kiểm tra kết quả vô cực hoặc NaN
                    if (Double.isInfinite(ketQua) || Double.isNaN(ketQua)) {
                        view.setDisplayText("Lỗi");
                        bieuThuc = ""; // Reset
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
                if (daRaKetQua) {
                    if (command.matches("[0-9]") || command.equals("π") || command.equals("e") || command.equals("√") || command.equals("(")) {
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
                
                String textToAdd = command;
                if (command.equals("√")) textToAdd = "sqrt";
                
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