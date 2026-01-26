package Calculator;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

public class CalculatorModel {
    
    private boolean isDegree = false;
    private double ans = 0;
    private DecimalFormat df = new DecimalFormat("#.##########");

    public void setDegreeMode(boolean isDegree) {
        this.isDegree = isDegree;
    }

    public void setAns(double value) {
        this.ans = value;
    }

    // --- LOGIC GIẢI PHƯƠNG TRÌNH THÔNG MINH ---
    public String solveEquation(String expression) throws Exception {
        // BƯỚC 1: Cố gắng nhận diện phương trình bậc 2: ax^2 + bx + c = 0
        // Phương pháp: Tính f(0), f(1), f(-1) để suy ra a, b, c
        try {
            double y0 = calculateWithValue(expression, 0);   // c
            double y1 = calculateWithValue(expression, 1);   // a + b + c
            double y_1 = calculateWithValue(expression, -1); // a - b + c

            double c = y0;
            double a = (y1 + y_1 - 2 * c) / 2;
            double b = (y1 - y_1) / 2;

            // Kiểm tra lại giả thuyết này với x = 2
            double y2_actual = calculateWithValue(expression, 2);
            double y2_predicted = a * 4 + b * 2 + c;

            // Nếu khớp (cho phép sai số nhỏ), thì đây đích thị là đa thức bậc <= 2
            if (Math.abs(y2_actual - y2_predicted) < 1e-9) {
                return giaiPhuongTrinhBac2(a, b, c);
            }
        } catch (Exception e) {
            // Nếu có lỗi (ví dụ log(x) tại x=-1), bỏ qua bước này
        }

        // BƯỚC 2: Nếu không phải bậc 2, dùng Newton-Raphson tìm nghiệm gần đúng
        double root = solveNewtonRaphson(expression);
        return "x ≈ " + df.format(root);
    }

    private String giaiPhuongTrinhBac2(double a, double b, double c) {
        // Trường hợp a ~ 0: Phương trình bậc 1 (bx + c = 0)
        if (Math.abs(a) < 1e-9) {
            if (Math.abs(b) < 1e-9) {
                return (Math.abs(c) < 1e-9) ? "Vô số nghiệm" : "Vô nghiệm";
            }
            return "x = " + df.format(-c / b);
        }

        // Trường hợp bậc 2: Tính Delta
        double delta = b * b - 4 * a * c;
        
        if (delta < -1e-9) {
            return "Vô nghiệm thực";
        } else if (Math.abs(delta) < 1e-9) { // Delta ~ 0
            double x = -b / (2 * a);
            return "x = " + df.format(x);
        } else { // Delta > 0 -> 2 nghiệm phân biệt
            double x1 = (-b + Math.sqrt(delta)) / (2 * a);
            double x2 = (-b - Math.sqrt(delta)) / (2 * a);
            // Sắp xếp nghiệm bé trước lớn sau cho đẹp
            return "x1=" + df.format(Math.min(x1, x2)) + "; x2=" + df.format(Math.max(x1, x2));
        }
    }

    // Thuật toán Newton-Raphson (đổi tên từ solveForX cũ)
    private double solveNewtonRaphson(String expression) throws Exception {
        double x = 2.0; 
        int maxIterations = 100;
        double tolerance = 1e-7;

        for (int i = 0; i < maxIterations; i++) {
            double y = calculateWithValue(expression, x);
            double h = 1e-5;
            double dy = (calculateWithValue(expression, x + h) - calculateWithValue(expression, x - h)) / (2 * h);

            if (Math.abs(dy) < 1e-10) { x += 1.0; continue; }

            double xNew = x - y / dy;
            if (Math.abs(xNew - x) < tolerance) return xNew;
            x = xNew;
        }
        throw new ArithmeticException("Không tìm thấy nghiệm");
    }

    private double calculateWithValue(String expression, double xValue) throws Exception {
        String exprWithVal = expression.replace("x", "(" + String.valueOf(xValue) + ")");
        return calculate(exprWithVal);
    }

    // --- CÁC HÀM CŨ GIỮ NGUYÊN ---
    public double calculate(String bieuThuc) throws Exception {
        String processed = bieuThuc;
        String funcStart = "[(πesctlAx]"; 
        processed = processed.replaceAll("(?<=\\d)(?=" + funcStart + ")", "*"); 
        processed = processed.replaceAll("(?<=\\))(?=[\\d" + funcStart + "])", "*"); 
        processed = processed.replaceAll("(?<=[πeAx])(?=[\\d" + funcStart + "])", "*");
        processed = processed.replace("Ans", "(" + String.valueOf(ans) + ")");
        processed = processed.replace("π", String.valueOf(Math.PI));
        processed = processed.replace("e", String.valueOf(Math.E));
        
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
                    double sVal = Math.sin(val);
                    if (Math.abs(sVal) < 1e-10) sVal = 0; 
                    so.push(sVal); break;
                case "cos": 
                    if (isDegree) val = Math.toRadians(val);
                    double cVal = Math.cos(val);
                    if (Math.abs(cVal) < 1e-10) cVal = 0;
                    so.push(cVal); break;
                case "tan": 
                    if (isDegree) {
                        if (val % 180 == 90 || val % 180 == -90) throw new ArithmeticException("Lỗi Tan");
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
            case "/": if (soSau == 0) throw new ArithmeticException("Chia 0"); so.push(soDau / soSau); break;
            case "//": if (soSau == 0) throw new ArithmeticException("Chia 0"); so.push(Math.floor(soDau / soSau)); break;
            case "^": so.push(Math.pow(soDau, soSau)); break;
        }
    }
    private boolean laSo(String s) { try { Double.parseDouble(s); return true; } catch (Exception e) { return false; } }
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