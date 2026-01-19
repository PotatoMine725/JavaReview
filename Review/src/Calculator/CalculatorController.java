package Calculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class CalculatorController {
    // Tham chiếu đến giao diện người dùng của máy tính
    private CalculatorView view;
    // Tham chiếu đến logic xử lý kinh doanh của máy tính
    private CalculatorModel model;
    
    // Lưu trữ biểu thức toán học được nhập bởi người dùng
    private String bieuThuc = "";
    // Cờ để theo dõi nếu một kết quả vừa được hiển thị
    private boolean daRaKetQua = false;
    // Bộ định dạng để hiển thị kết quả với tối đa 10 chữ số thập phân
    private DecimalFormat df = new DecimalFormat("#.##########");

    /**
     * Hàm tạo khởi tạo bộ điều khiển với view và model.
     * Đăng ký một trình nghe hành động để xử lý các nhấn nút.
     */
    public CalculatorController(CalculatorView view, CalculatorModel model) {
        this.view = view;
        this.model = model;
        // Đăng ký trình nghe hành động để phản hồi với các nhấn nút trong view
        this.view.addCalculationListener(new CalculatorListener());
    }

    /**
     * Lớp nội bộ lắng nghe các hành động nút máy tính.
     * Xử lý các lệnh nút khác nhau (số, toán tử, hàm).
     */
    class CalculatorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            // Xử lý nút Xóa - đặt lại mọi thứ
            if (command.equals("C")) {
                bieuThuc = "";
                view.setDisplayText("0");
                view.setHistoryText("");
            }
            // Xử lý nút Xóa - loại bỏ ký tự cuối cùng
            else if (command.equals("Del")) {
                if (!bieuThuc.isEmpty()) {
                    // Xử lý đặc biệt cho "sqrt" và "//" là các toán tử nhiều ký tự
                    if (bieuThuc.endsWith("sqrt")) {
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 4);
                    } else if (bieuThuc.endsWith("//")) {
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 2);
                    } else {
                        // Loại bỏ ký tự đơn cuối cùng
                        bieuThuc = bieuThuc.substring(0, bieuThuc.length() - 1);
                    }
                    // Cập nhật hiển thị - hiển thị "0" nếu biểu thức trống
                    view.setDisplayText(bieuThuc.isEmpty() ? "0" : bieuThuc);
                }
            }
            // Xử lý nút Bằng - tính toán biểu thức
            else if (command.equals("=")) {
                try {
                    // Hiển thị biểu thức trong lịch sử
                    view.setHistoryText(bieuThuc + " =");
                    // Tính kết quả bằng mô hình
                    double ketQua = model.calculate(bieuThuc);
                    
                    // Kiểm tra nếu kết quả là vô cùng hoặc không hợp lệ
                    if (Double.isInfinite(ketQua) || Double.isNaN(ketQua)) {
                        view.setDisplayText("Lỗi");
                        bieuThuc = ""; // Đặt lại cho phép tính tiếp theo
                    } else {
                        // Định dạng và hiển thị kết quả
                        String ketQuaString = df.format(ketQua);
                        view.setDisplayText(ketQuaString);
                        bieuThuc = ketQuaString;
                        // Đánh dấu rằng một kết quả vừa được hiển thị
                        daRaKetQua = true;
                    }
                } catch (Exception ex) {
                    // Xử lý lỗi tính toán
                    view.setDisplayText("Lỗi");
                    bieuThuc = "";
                }
            }
            // Xử lý tất cả các đầu vào khác (số, toán tử, hàm)
            else {
                // Nếu một kết quả vừa được hiển thị, bắt đầu một đầu vào mới sẽ xóa nó
                if (daRaKetQua) {
                    // Xóa nếu bắt đầu một số hoặc hàm mới
                    if (command.matches("[0-9]") || command.equals("π") || command.equals("e") || command.equals("√") || command.equals("(")) {
                        bieuThuc = ""; 
                    }
                    daRaKetQua = false;
                }

                // Xử lý dấu thập phân - ngăn chặn nhiều dấu chấm trong cùng một số
                if (command.equals(".")) {
                    boolean hasDot = false;
                    // Kiểm tra xem số hiện tại có dấu thập phân không
                    for (int i = bieuThuc.length() - 1; i >= 0; i--) {
                        char c = bieuThuc.charAt(i);
                        // Dừng khi chúng ta gặp một toán tử
                        if (!Character.isDigit(c) && c != '.') break; 
                        // Tìm thấy dấu thập phân - đừng thêm một dấu khác
                        if (c == '.') { hasDot = true; break; }
                    }
                    if (hasDot) return;
                }
                
                // Chuyển đổi ký hiệu căn bậc hai thành "sqrt" cho mô hình
                String textToAdd = command;
                if (command.equals("√")) textToAdd = "sqrt";
                
                // Phân loại loại đầu vào
                boolean isOperator = command.matches("[+\\-*/^]") || command.equals("//");
                boolean isDot = command.equals(".");
                boolean isCloser = command.equals(")");
                
                // Thay thế "0" bằng đầu vào mới (trừ khi thêm toán tử hoặc dấu thập phân)
                if (bieuThuc.equals("0") && !isOperator && !isDot && !isCloser) {
                    bieuThuc = textToAdd; 
                } else {
                    // Nối đầu vào vào biểu thức
                    bieuThuc += textToAdd;
                }
                // Cập nhật hiển thị bằng biểu thức hiện tại
                view.setDisplayText(bieuThuc);
            }
        }
    }
}