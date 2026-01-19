package Calculator;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {

    public double calculate(String bieuThuc) throws Exception {
        // --- BƯỚC 1: XỬ LÝ NHÂN NGẦM (Implicit Multiplication) ---
        String processed = bieuThuc;
        
        // 1. Số đi liền với: (, π, e, s (sqrt) -> Thêm *
        // VD: 2( -> 2*(, 2π -> 2*π
        processed = processed.replaceAll("(?<=\\d)(?=[(πes])", "*");

        // 2. Ngoặc đóng đi liền với: Số, (, π, e, s -> Thêm * // VD: )2 -> )*2 (FIXED), )( -> )*(
        processed = processed.replaceAll("(?<=\\))(?=[\\d(πes])", "*");

        // 3. Hằng số (π, e) đi liền với: Số, (, π, e, s -> Thêm *
        // VD: π2 -> π*2
        processed = processed.replaceAll("(?<=[πe])(?=[\\d(πes])", "*");

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
        
        // Khôi phục lại toán tử chia nguyên // (do bị tách ở trên)
        processed = processed.replace("/  /", "//"); 
        
        // Xóa khoảng trắng thừa
        processed = processed.replaceAll("\\s+", " ").trim();

        return danhGiaBieuThuc(processed);
    }

    private double danhGiaBieuThuc(String bieuThuc) throws Exception {
        String[] rawTokens = bieuThuc.split(" ");
        List<String> tokens = new ArrayList<>();
        
        // --- BƯỚC 4: XỬ LÝ SỐ ÂM (UNARY MINUS) ---
        for (int i = 0; i < rawTokens.length; i++) {
            String t = rawTokens[i];
            if (t.equals("-")) {
                boolean isNegativeSign = false;
                if (i == 0) {
                    isNegativeSign = true; 
                } else {
                    String prev = rawTokens[i-1];
                    if (laToanTu(prev) || prev.equals("(") || prev.equals("sqrt")) {
                        isNegativeSign = true;
                    }
                }
                
                if (isNegativeSign) {
                    tokens.add("NEG");
                } else {
                    tokens.add("-");
                }
            } else {
                tokens.add(t);
            }
        }

        // --- BƯỚC 5: THUẬT TOÁN SHUNTING-YARD ---
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
                    
                    if ((pt.equals("^") || pt.equals("NEG")) && pt.equals(nganXepToanTu.peek())) {
                        break;
                    }
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
            if (so.isEmpty()) throw new ArithmeticException("Lỗi sqrt");
            so.push(Math.sqrt(so.pop()));
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
                so.push((double)((long)(soDau / soSau))); break;
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