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
                    // Xóa Ans (3 ký tự)
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
                    view.setHistoryText(bieuThuc + " =");
                    double ketQua = model.calculate(bieuThuc);
                    
                    if (Double.isInfinite(ketQua) || Double.isNaN(ketQua)) {
                        view.setDisplayText("Lỗi");
                        bieuThuc = ""; 
                    } else {
                        String ketQuaString = df.format(ketQua);
                        view.setDisplayText(ketQuaString);
                        
                        // Cập nhật Ans trong Model
                        model.setAns(ketQua);
                        
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
                    // Nếu bấm Ans sau khi ra kết quả -> Bắt đầu phép tính mới với Ans
                    if (command.equals("Ans")) {
                        bieuThuc = "";
                    }
                    // Nếu nhập số, hằng, hàm -> Reset
                    else if (command.matches("[0-9]") || command.matches("π|e|√|\\(|sin|cos|tan|log|ln")) {
                        bieuThuc = ""; 
                    }
                    // Nếu nhập toán tử -> Giữ kết quả cũ
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
                // Ans, sin, cos... giữ nguyên
                
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