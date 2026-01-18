package Calculator;

import java.util.Stack;

public class CalculatorModel {

    // Hàm chính để tính giá trị từ chuỗi biểu thức
    public double calculate(String bieuThuc) throws Exception {
        // 1. Xử lý phép nhân ngầm (Implicit Multiplication)
        String processed = bieuThuc;
        
        // Trường hợp 1: Số + Ngoặc mở -> Thêm dấu * (VD: "3(" -> "3 * (")
        processed = processed.replaceAll("([0-9])\\(", "$1 * (");
        
        // Trường hợp 2: Ngoặc đóng + Số -> Thêm dấu * (VD: ")3" -> ") * 3")
        processed = processed.replaceAll("\\)([0-9])", ") * $1");
        
        // Trường hợp 3: Ngoặc đóng + Ngoặc mở -> Thêm dấu * (VD: ")(" -> ") * (")
        processed = processed.replaceAll("\\)\\(", ") * (");

        // 2. Chuẩn hóa biểu thức: thêm khoảng trắng quanh các toán tử để cắt chuỗi
        // Ví dụ: "2*(3+4)" -> "2 * ( 3 + 4 ) "
        String bieuThucChuan = processed.replaceAll("([+\\-*/()])", " $1 ")
                                        .replaceAll("\\s+", " ")
                                        .trim();

        return danhGiaBieuThuc(bieuThucChuan);
    }

    // Thuật toán đánh giá biểu thức (Shunting-yard & Stack)
    private double danhGiaBieuThuc(String bieuThuc) throws Exception {
        String[] cacPhanTu = bieuThuc.split(" ");
        Stack<Double> nganXepSo = new Stack<>();
        Stack<String> nganXepToanTu = new Stack<>();

        for (String pt : cacPhanTu) {
            if (pt.isEmpty()) continue;

            // Nếu là số
            if (laSo(pt)) {
                nganXepSo.push(Double.parseDouble(pt));
            } 
            // Nếu là dấu mở ngoặc
            else if (pt.equals("(")) {
                nganXepToanTu.push(pt);
            } 
            // Nếu là dấu đóng ngoặc
            else if (pt.equals(")")) {
                while (!nganXepToanTu.isEmpty() && !nganXepToanTu.peek().equals("(")) {
                    thucHienPhepTinh(nganXepSo, nganXepToanTu);
                }
                if (!nganXepToanTu.isEmpty()) nganXepToanTu.pop(); // Loại bỏ dấu "("
            } 
            // Nếu là toán tử (+ - * /)
            else if (laToanTu(pt)) {
                while (!nganXepToanTu.isEmpty() && doUuTien(pt) <= doUuTien(nganXepToanTu.peek())) {
                    thucHienPhepTinh(nganXepSo, nganXepToanTu);
                }
                nganXepToanTu.push(pt);
            }
        }

        // Xử lý các phép tính còn sót lại
        while (!nganXepToanTu.isEmpty()) {
            thucHienPhepTinh(nganXepSo, nganXepToanTu);
        }

        if (nganXepSo.isEmpty()) return 0;
        return nganXepSo.pop();
    }

    private void thucHienPhepTinh(Stack<Double> so, Stack<String> toanTu) {
        if (so.size() < 2) return; // Kiểm tra an toàn
        double soSau = so.pop();
        double soDau = so.pop();
        String op = toanTu.pop();
        
        switch (op) {
            case "+": so.push(soDau + soSau); break;
            case "-": so.push(soDau - soSau); break;
            case "*": so.push(soDau * soSau); break;
            case "/": 
                if (soSau == 0) throw new ArithmeticException("Lỗi chia 0");
                so.push(soDau / soSau); break;
        }
    }

    private boolean laSo(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean laToanTu(String s) {
        return s.matches("[+\\-*/]");
    }

    // Định nghĩa độ ưu tiên
    private int doUuTien(String op) {
        if (op.equals("*") || op.equals("/")) return 2;
        if (op.equals("+") || op.equals("-")) return 1;
        return 0;
    }
}