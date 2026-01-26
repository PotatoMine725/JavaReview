package Calculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class CalculatorController {
    private CalculatorView view;
    private CalculatorModel model;
    
    private String bieuThuc = "";
    private boolean daRaKetQua = false;
    private boolean isDegree = false;
    private DecimalFormat df = new DecimalFormat("#.##########");

    public CalculatorController(CalculatorView view, CalculatorModel model) {
        this.view = view;
        this.model = model;
        this.view.addCalculationListener(new CalculatorListener());
        this.view.addModeListener(e -> {
            isDegree = !isDegree;
            model.setDegreeMode(isDegree);
            view.updateModeButton(isDegree);
        });
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
                    if (bieuThuc.endsWith("Ans") || bieuThuc.endsWith("sin") || 
                        bieuThuc.endsWith("cos") || bieuThuc.endsWith("tan") || 
                        bieuThuc.endsWith("log")) {
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 3);
                    } else if (bieuThuc.endsWith("sqrt")) {
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 4);
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
                    // --- CASE 1: GIẢI PHƯƠNG TRÌNH (Chứa x) ---
                    if (bieuThuc.contains("x")) {
                        view.setHistoryText("Solve: " + bieuThuc + " = 0");
                        
                        // Gọi hàm mới solveEquation (trả về String)
                        String ketQuaString = model.solveEquation(bieuThuc);
                        
                        view.setDisplayText(ketQuaString);
                        // Không lưu Ans khi giải phương trình
                        daRaKetQua = true;
                    } 
                    // --- CASE 2: TÍNH TOÁN THƯỜNG ---
                    else {
                        view.setHistoryText(bieuThuc + " =");
                        double ketQua = model.calculate(bieuThuc);
                        
                        if (Double.isInfinite(ketQua) || Double.isNaN(ketQua)) {
                            view.setDisplayText("Lỗi");
                            bieuThuc = ""; 
                        } else {
                            String ketQuaString = df.format(ketQua);
                            view.setDisplayText(ketQuaString);
                            model.setAns(ketQua);
                            bieuThuc = ketQuaString;
                            daRaKetQua = true;
                        }
                    }
                } catch (Exception ex) {
                    view.setDisplayText(ex.getMessage() != null ? ex.getMessage() : "Lỗi");
                    // bieuThuc = ""; // Có thể giữ lại biểu thức để user sửa
                }
            }
            else {
                // ... (Logic nhập liệu giữ nguyên như cũ) ...
                if (daRaKetQua) {
                    if (command.equals("Ans")) bieuThuc = "Ans";
                    else if (command.matches("[0-9]") || command.matches("π|e|√|\\(|sin|cos|tan|log|ln|x")) bieuThuc = ""; 
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