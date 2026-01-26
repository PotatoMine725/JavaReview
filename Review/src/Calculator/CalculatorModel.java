package Calculator;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {
    
    private boolean isDegree = false;
    private double ans = 0; // Biến lưu kết quả trước đó

    public void setDegreeMode(boolean isDegree) {
        this.isDegree = isDegree;
    }

    // Hàm cập nhật Ans
    public void setAns(double value) {
        this.ans = value;
    }

    public double calculate(String bieuThuc) throws Exception {
        String processed = bieuThuc;
        
        // Regex nhận diện ký tự đầu của: ( , π, e, s(sin/sqrt), c(cos), t(tan), l(log/ln), A(Ans)
        String funcStart = "[(πesctlA]"; 

        // 1. Xử lý nhân ngầm: Thêm * vào trước/sau các hàm/biến
        // VD: 2Ans -> 2*Ans, Ans( -> Ans*(
        processed = processed.replaceAll("(?<=\\d)(?=" + funcStart + ")", "*"); 
        processed = processed.replaceAll("(?<=\\))(?=[\\d" + funcStart + "])", "*"); 
        processed = processed.replaceAll("(?<=[πeA])(?=[\\d" + funcStart + "])", "*"); // A là Ans

        // 2. Thay thế Hằng số & Ans
        // Ans được thay bằng (value) để đảm bảo an toàn cho số âm. VD: Ans=-5 -> (-5.0)
        // Điều này giúp Ans^2 thành (-5.0)^2 = 25 (đúng), thay vì -5.0^2 = -25 (sai)
        processed = processed.replace("Ans", "(" + String.valueOf(ans) + ")");
        processed = processed.replace("π", String.valueOf(Math.PI));
        processed = processed.replace("e", String.valueOf(Math.E));

        // 3. Chuẩn hóa toán tử
        processed = processed.replace("sin", " sin ")
                             .replace("cos", " cos ")
                             .replace("tan", " tan ")
                             .replace("log", " log ")
                             .replace("ln", " ln ")
                             .replace("sqrt", " sqrt ")
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
        
        for (int i = 0; i < rawTokens.length; i++) {
            String t = rawTokens[i];
            if (t.equals("-")) {
                boolean isNegativeSign = false;
                if (i == 0) isNegativeSign = true; 
                else {
                    String prev = rawTokens[i-1];
                    if (laToanTu(prev) || prev.equals("(") || isFunction(prev)) {
                        isNegativeSign = true;
                    }
                }
                tokens.add(isNegativeSign ? "NEG" : "-");
            } else {
                tokens.add(t);
            }
        }

        Stack<Double> nganXepSo = new Stack<>();
        Stack<String> nganXepToanTu = new Stack<>();

        for (String pt : tokens) {
            if (pt.isEmpty()) continue;

            if (laSo(pt)) {
                nganXepSo.push(Double.parseDouble(pt));
            } else if (pt.equals("(")) {
                nganXepToanTu.push(pt);
            } else if (pt.equals(")")) {
                while (!nganXepToanTu.isEmpty() && !nganXepToanTu.peek().equals("(")) {
                    thucHienPhepTinh(nganXepSo, nganXepToanTu);
                }
                if (!nganXepToanTu.isEmpty()) nganXepToanTu.pop(); 
                if (!nganXepToanTu.isEmpty() && isFunction(nganXepToanTu.peek())) {
                    thucHienPhepTinh(nganXepSo, nganXepToanTu);
                }
            } else if (laToanTu(pt) || pt.equals("NEG") || isFunction(pt)) {
                while (!nganXepToanTu.isEmpty() && doUuTien(pt) <= doUuTien(nganXepToanTu.peek())) {
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
        
        if (isFunction(op) || op.equals("NEG")) {
            if (so.isEmpty()) throw new ArithmeticException("Lỗi cú pháp");
            double val = so.pop();

            switch (op) {
                case "sqrt":
                    if (val < 0) throw new ArithmeticException("Căn số âm");
                    so.push(Math.sqrt(val)); break;
                case "sin": 
                    if (isDegree) val = Math.toRadians(val);
                    double sinVal = Math.sin(val);
                    if (Math.abs(sinVal) < 1e-10) sinVal = 0; 
                    so.push(sinVal); break;
                case "cos": 
                    if (isDegree) val = Math.toRadians(val);
                    double cosVal = Math.cos(val);
                    if (Math.abs(cosVal) < 1e-10) cosVal = 0;
                    so.push(cosVal); break;
                case "tan": 
                    if (isDegree) {
                        if (val % 180 == 90 || val % 180 == -90) throw new ArithmeticException("Tan lỗi");
                        val = Math.toRadians(val);
                    }
                    so.push(Math.tan(val)); break;
                case "log": 
                    if (val <= 0) throw new ArithmeticException("Lỗi miền");
                    so.push(Math.log10(val)); break;
                case "ln": 
                    if (val <= 0) throw new ArithmeticException("Lỗi miền");
                    so.push(Math.log(val)); break;
                case "NEG": so.push(-val); break;
            }
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
                so.push(Math.floor(soDau / soSau)); break;
            case "^": so.push(Math.pow(soDau, soSau)); break;
        }
    }

    private boolean laSo(String s) {
        try { Double.parseDouble(s); return true; } catch (Exception e) { return false; }
    }
    private boolean isFunction(String s) { return s.matches("sqrt|sin|cos|tan|log|ln"); }
    private boolean laToanTu(String s) { return s.matches("\\/\\/|\\^|[+\\-*/]"); }
    private int doUuTien(String op) {
        if (isFunction(op)) return 5;
        if (op.equals("NEG")) return 4;
        if (op.equals("^")) return 3;
        if (op.equals("*") || op.equals("/") || op.equals("//")) return 2;
        if (op.equals("+") || op.equals("-")) return 1;
        return 0;
    }
}