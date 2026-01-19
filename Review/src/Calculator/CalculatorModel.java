package Calculator;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {

    public double calculate(String bieuThuc) throws Exception {
        // --- BƯỚC 1: XỬ LÝ NHÂN NGẦM (Implicit Multiplication) ---
        // Thêm dấu * vào giữa các thành phần nhân ngầm
        // Quy tắc: [Số/NgoặcĐóng] đi liền với [π/e/sqrt/NgoặcMở] -> Thêm *
        
        String processed = bieuThuc;
        
        // 1. Chèn * vào sau số hoặc ngoặc đóng, nếu phía sau là: (, π, e, s (sqrt)
        // VD: 2π -> 2*π, )e -> )*e, 2( -> 2*(, 2sqrt -> 2*sqrt
        processed = processed.replaceAll("(?<=[\\d\\)])(?=[(πes])", "*");

        // 2. Chèn * vào sau hằng số (π, e), nếu phía sau là: số, (, π, e, s
        // VD: π2 -> π*2, ππ -> π*π
        processed = processed.replaceAll("(?<=[πe])(?=[\\d(πes])", "*");

        // --- BƯỚC 2: THAY THẾ HẰNG SỐ ---
        // Lưu ý: Chỉ thay thế sau khi đã chèn dấu *
        processed = processed.replace("π", String.valueOf(Math.PI));
        processed = processed.replace("e", String.valueOf(Math.E));

        // --- BƯỚC 3: CHUẨN HÓA TOÁN TỬ ---
        // Tách các toán tử bằng khoảng trắng để dễ split
        processed = processed.replace("sqrt", " sqrt ")
                             .replace("^", " ^ ")
                             .replace("(", " ( ")
                             .replace(")", " ) ")
                             .replace("+", " + ")
                             .replace("-", " - ")
                             .replace("*", " * ")
                             .replace("/", " / "); // Dòng này sẽ tách cả // thành / /
        
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
        // Phân biệt dấu trừ (-) và dấu âm (NEG)
        // Dấu âm xuất hiện khi nó ở đầu biểu thức HOẶC đi ngay sau một toán tử/ngoặc mở
        
        for (int i = 0; i < rawTokens.length; i++) {
            String t = rawTokens[i];
            if (t.equals("-")) {
                // Kiểm tra xem đây là trừ hay âm
                boolean isNegativeSign = false;
                if (i == 0) {
                    isNegativeSign = true; // Ở đầu dòng: -5
                } else {
                    String prev = rawTokens[i-1];
                    // Nếu trước nó là toán tử hoặc ( -> đây là dấu âm
                    if (laToanTu(prev) || prev.equals("(") || prev.equals("sqrt")) {
                        isNegativeSign = true;
                    }
                }
                
                if (isNegativeSign) {
                    tokens.add("NEG"); // Đánh dấu là dấu âm đặc biệt
                } else {
                    tokens.add("-"); // Phép trừ bình thường
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
                if (!nganXepToanTu.isEmpty()) nganXepToanTu.pop(); // Bỏ dấu (
                
                // Nếu sau khi đóng ngoặc có hàm sqrt chờ sẵn (VD: sqrt(4)) -> Tính luôn
                if (!nganXepToanTu.isEmpty() && nganXepToanTu.peek().equals("sqrt")) {
                    thucHienPhepTinh(nganXepSo, nganXepToanTu);
                }
            } 
            else if (laToanTu(pt) || pt.equals("NEG") || pt.equals("sqrt")) {
                while (!nganXepToanTu.isEmpty() && 
                       doUuTien(pt) <= doUuTien(nganXepToanTu.peek())) {
                    
                    // Lũy thừa ^ và NEG là kết hợp phải (Right Associative) -> Không tính ngay
                    // VD: 2^3^4 -> 2^(3^4)
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
        
        // Toán tử 1 ngôi
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

        // Toán tử 2 ngôi
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

    // Bảng ưu tiên toán tử
    private int doUuTien(String op) {
        if (op.equals("sqrt")) return 5;
        if (op.equals("NEG")) return 4; // Dấu âm ưu tiên cao hơn lũy thừa để 2^-3 hiểu là 2^(-3)
        if (op.equals("^")) return 3;
        if (op.equals("*") || op.equals("/") || op.equals("//")) return 2;
        if (op.equals("+") || op.equals("-")) return 1;
        return 0;
    }
}