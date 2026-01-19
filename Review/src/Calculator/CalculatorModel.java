package Calculator;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {

    public double calculate(String bieuThuc) throws Exception {
        // --- BƯỚC 1: XỬ LÝ NHÂN NGẦM ---
        String processed = bieuThuc;
        processed = processed.replaceAll("(?<=\\d)(?=[(πes])", "*"); // Số -> (
        processed = processed.replaceAll("(?<=\\))(?=[\\d(πes])", "*"); // ) -> Số
        processed = processed.replaceAll("(?<=[πe])(?=[\\d(πes])", "*"); // Hằng -> Số

        // --- BƯỚC 2: THAY THẾ HẰNG SỐ ---
        processed = processed.replace("π", String.valueOf(Math.PI));
        processed = processed.replace("e", String.valueOf(Math.E));

        // --- BƯỚC 3: CHUẨN HÓA TOÁN TỬ ---
        processed = processed.replace("sqrt", " sqrt ")
                             .replace("^", " ^ ")
                             .replace("(", " ( ")
                             .replace(")", " ) ")
                             .replace("+", " + ")
                             .replace("-", " - ")
                             .replace("*", " * ")
                             .replace("/", " / "); 
        
        processed = processed.replace("/  /", "//"); 
        processed = processed.replaceAll("\\s+", " ").trim();

        return danhGiaBieuThuc(processed);
    }

    private double danhGiaBieuThuc(String bieuThuc) throws Exception {
        String[] rawTokens = bieuThuc.split(" ");
        List<String> tokens = new ArrayList<>();
        
        // --- BƯỚC 4: XỬ LÝ SỐ ÂM ---
        for (int i = 0; i < rawTokens.length; i++) {
            String t = rawTokens[i];
            if (t.equals("-")) {
                boolean isNegativeSign = false;
                if (i == 0) isNegativeSign = true; 
                else {
                    String prev = rawTokens[i-1];
                    if (laToanTu(prev) || prev.equals("(") || prev.equals("sqrt")) {
                        isNegativeSign = true;
                    }
                }
                tokens.add(isNegativeSign ? "NEG" : "-");
            } else {
                tokens.add(t);
            }
        }

        // --- BƯỚC 5: SHUNTING-YARD ---
        Stack<Double> nganXepSo = new Stack<>();
        Stack<String> nganXepToanTu = new Stack<>();

        for (String pt : tokens) {
            if (pt.isEmpty()) continue;

            if (laSo(pt)) {
                nganXepSo.push(Double.parseDouble(pt));
            } 
            else if (pt.equals("(")) {
                nganXepToanTu.push(pt);
            } 
            else if (pt.equals(")")) {
                while (!nganXepToanTu.isEmpty() && !nganXepToanTu.peek().equals("(")) {
                    thucHienPhepTinh(nganXepSo, nganXepToanTu);
                }
                if (!nganXepToanTu.isEmpty()) nganXepToanTu.pop(); 
                
                if (!nganXepToanTu.isEmpty() && nganXepToanTu.peek().equals("sqrt")) {
                    thucHienPhepTinh(nganXepSo, nganXepToanTu);
                }
            } 
            else if (laToanTu(pt) || pt.equals("NEG") || pt.equals("sqrt")) {
                while (!nganXepToanTu.isEmpty() && 
                       doUuTien(pt) <= doUuTien(nganXepToanTu.peek())) {
                    if ((pt.equals("^") || pt.equals("NEG")) && pt.equals(nganXepToanTu.peek())) break;
                    if (nganXepToanTu.peek().equals("(")) break;
                    thucHienPhepTinh(nganXepSo, nganXepToanTu);
                }
                nganXepToanTu.push(pt);
            }
        }

        while (!nganXepToanTu.isEmpty()) {
            thucHienPhepTinh(nganXepSo, nganXepToanTu);
        }

        if (nganXepSo.isEmpty()) return 0;
        return nganXepSo.pop();
    }

    private void thucHienPhepTinh(Stack<Double> so, Stack<String> toanTu) {
        String op = toanTu.pop();
        
        if (op.equals("sqrt")) {
            if (so.isEmpty()) throw new ArithmeticException("Lỗi cú pháp");
            double val = so.pop();
            // [CẬP NHẬT] Kiểm tra số âm
            if (val < 0) throw new ArithmeticException("Căn số âm");
            so.push(Math.sqrt(val));
            return;
        }
        if (op.equals("NEG")) {
            if (so.isEmpty()) throw new ArithmeticException("Lỗi dấu âm");
            so.push(-so.pop());
            return;
        }

        if (so.size() < 2) throw new ArithmeticException("Lỗi toán hạng");
        double soSau = so.pop();
        double soDau = so.pop();

        switch (op) {
            case "+": so.push(soDau + soSau); break;
            case "-": so.push(soDau - soSau); break;
            case "*": so.push(soDau * soSau); break;
            case "/": 
                if (soSau == 0) throw new ArithmeticException("Chia 0"); 
                so.push(soDau / soSau); break;
            case "//": 
                if (soSau == 0) throw new ArithmeticException("Chia 0");
                // [CẬP NHẬT] Dùng Math.floor để tránh tràn số long
                so.push(Math.floor(soDau / soSau)); break;
            case "^": so.push(Math.pow(soDau, soSau)); break;
        }
    }

    private boolean laSo(String s) {
        try { Double.parseDouble(s); return true; } catch (Exception e) { return false; }
    }

    private boolean laToanTu(String s) {
        return s.matches("sqrt|\\/\\/|\\^|[+\\-*/]|NEG");
    }

    private int doUuTien(String op) {
        if (op.equals("sqrt")) return 5;
        if (op.equals("NEG")) return 4;
        if (op.equals("^")) return 3;
        if (op.equals("*") || op.equals("/") || op.equals("//")) return 2;
        if (op.equals("+") || op.equals("-")) return 1;
        return 0;
    }
}