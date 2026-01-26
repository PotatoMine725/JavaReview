package Calculator;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {

    public double calculate(String bieuThuc) throws Exception {
        // --- BƯỚC 1: XỬ LÝ NHÂN NGẦM ---
        String processed = bieuThuc;
        
        // Regex nhận diện ký tự đầu của các hàm/hằng số:
        // ( = ngoặc mở
        // π, e = hằng số
        // s = sqrt, sin
        // c = cos
        // t = tan
        // l = log, ln
        String funcStart = "[(πesctl]"; 

        // 1. Số + [Hàm/Ngoặc/Hằng] -> Thêm * (VD: 2sin -> 2*sin)
        processed = processed.replaceAll("(?<=\\d)(?=" + funcStart + ")", "*"); 
        
        // 2. Ngoặc đóng + [Số/Hàm/Ngoặc/Hằng] -> Thêm * (VD: )sin -> )*sin)
        processed = processed.replaceAll("(?<=\\))(?=[\\d" + funcStart + "])", "*"); 
        
        // 3. Hằng số + [Số/Hàm/Ngoặc/Hằng] -> Thêm * (VD: πsin -> π*sin)
        processed = processed.replaceAll("(?<=[πe])(?=[\\d" + funcStart + "])", "*");

        // --- BƯỚC 2: THAY THẾ HẰNG SỐ ---
        processed = processed.replace("π", String.valueOf(Math.PI));
        processed = processed.replace("e", String.valueOf(Math.E));

        // --- BƯỚC 3: CHUẨN HÓA TOÁN TỬ ---
        // Tách các hàm ra để dễ split
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
        
        // --- BƯỚC 4: XỬ LÝ SỐ ÂM ---
        for (int i = 0; i < rawTokens.length; i++) {
            String t = rawTokens[i];
            if (t.equals("-")) {
                boolean isNegativeSign = false;
                if (i == 0) isNegativeSign = true; 
                else {
                    String prev = rawTokens[i-1];
                    // Nếu trước nó là toán tử hoặc ( hoặc TÊN HÀM -> là dấu âm
                    if (laToanTu(prev) || prev.equals("(") || isFunction(prev)) {
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
                
                // Nếu sau khi đóng ngoặc có hàm chờ sẵn (VD: sin(30)) -> Tính luôn
                if (!nganXepToanTu.isEmpty() && isFunction(nganXepToanTu.peek())) {
                    thucHienPhepTinh(nganXepSo, nganXepToanTu);
                }
            } 
            else if (laToanTu(pt) || pt.equals("NEG") || isFunction(pt)) {
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
        
        // --- XỬ LÝ CÁC HÀM 1 NGÔI ---
        if (isFunction(op) || op.equals("NEG")) {
            if (so.isEmpty()) throw new ArithmeticException("Lỗi cú pháp hàm");
            double val = so.pop();

            switch (op) {
                case "sqrt":
                    if (val < 0) throw new ArithmeticException("Căn số âm");
                    so.push(Math.sqrt(val)); break;
                case "sin": so.push(Math.sin(val)); break; // Input là Radian
                case "cos": so.push(Math.cos(val)); break;
                case "tan": so.push(Math.tan(val)); break;
                case "log": 
                    if (val <= 0) throw new ArithmeticException("Log lỗi miền");
                    so.push(Math.log10(val)); break;
                case "ln": 
                    if (val <= 0) throw new ArithmeticException("Ln lỗi miền");
                    so.push(Math.log(val)); break;
                case "NEG": so.push(-val); break;
            }
            return;
        }

        // --- XỬ LÝ TOÁN TỬ 2 NGÔI ---
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

    // Kiểm tra xem token có phải là hàm toán học không
    private boolean isFunction(String s) {
        return s.matches("sqrt|sin|cos|tan|log|ln");
    }

    private boolean laToanTu(String s) {
        return s.matches("\\/\\/|\\^|[+\\-*/]");
    }

    private int doUuTien(String op) {
        if (isFunction(op)) return 5; // Hàm ưu tiên cao nhất
        if (op.equals("NEG")) return 4;
        if (op.equals("^")) return 3;
        if (op.equals("*") || op.equals("/") || op.equals("//")) return 2;
        if (op.equals("+") || op.equals("-")) return 1;
        return 0;
    }
}