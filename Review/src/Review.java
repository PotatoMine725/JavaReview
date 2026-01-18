import javax.swing.*;
import java.awt.*;


public class Review extends JFrame {
    private JComboBox<String> cbLoaiHinh;
    private JPanel panelInputs;
    private JLabel lblResult;
    
    // Các trường nhập liệu 
    private JTextField txtVal1, txtVal2, txtVal3;
    private JLabel lblVal1, lblVal2, lblVal3;

    public Review() {
        setTitle("Geometry for Babies V1");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Phần chọn hình
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Chọn hình: "));
        String[] hinhs = {"Hình Tròn", "Hình Vuông", "Hình Tam Giác", "Hình Bình Hành"};
        cbLoaiHinh = new JComboBox<>(hinhs);
        topPanel.add(cbLoaiHinh);
        add(topPanel, BorderLayout.NORTH);

        //Phần nhập liệu
        panelInputs = new JPanel(new GridLayout(4, 2, 10, 10));
        panelInputs.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblVal1 = new JLabel("Bán kính:");
        txtVal1 = new JTextField();
        lblVal2 = new JLabel("Giá trị 2:");
        txtVal2 = new JTextField();
        lblVal3 = new JLabel("Giá trị 3:");
        txtVal3 = new JTextField();

        // Mặc định hiển thị cho Hình Tròn
        addInputRows();
        updateInputFields("Hình Tròn");

        add(panelInputs, BorderLayout.CENTER);

        //Phần nút bấm và kết quả
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        JButton btnCalc = new JButton("TÍNH TOÁN");
        btnCalc.setBackground(Color.CYAN);
        lblResult = new JLabel("Kết quả sẽ hiện ở đây", SwingConstants.CENTER);
        lblResult.setFont(new Font("Arial", Font.BOLD, 14));
        
        bottomPanel.add(btnCalc);
        bottomPanel.add(lblResult);
        add(bottomPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện
        
        // Khi đổi loại hình trong Dropdown
        cbLoaiHinh.addActionListener(e -> {
            String selected = (String) cbLoaiHinh.getSelectedItem();
            updateInputFields(selected);
        });

        // Khi bấm nút Tính
        btnCalc.addActionListener(e -> calculate());
    }

    private void addInputRows() {
        panelInputs.add(lblVal1); panelInputs.add(txtVal1);
        panelInputs.add(lblVal2); panelInputs.add(txtVal2);
        panelInputs.add(lblVal3); panelInputs.add(txtVal3);
    }

    private void updateInputFields(String shapeType) {
        // Reset text
        txtVal1.setText(""); txtVal2.setText(""); txtVal3.setText("");
        
        // Ẩn/Hiện dựa theo hình
        switch (shapeType) {
            case "Hình Tròn":
                lblVal1.setText("Bán kính (r):");
                setVisibleRow(true, false, false);
                break;
            case "Hình Vuông":
                lblVal1.setText("Cạnh (a):");
                setVisibleRow(true, false, false);
                break;
            case "Hình Tam Giác":
                lblVal1.setText("Cạnh a:");
                lblVal2.setText("Cạnh b:");
                lblVal3.setText("Cạnh c:");
                setVisibleRow(true, true, true);
                break;
            case "Hình Bình Hành":
                lblVal1.setText("Cạnh đáy:");
                lblVal2.setText("Cạnh bên:");
                lblVal3.setText("Chiều cao:");
                setVisibleRow(true, true, true);
                break;
        }
    }

    private void setVisibleRow(boolean r1, boolean r2, boolean r3) {
        lblVal1.setVisible(r1); txtVal1.setVisible(r1);
        lblVal2.setVisible(r2); txtVal2.setVisible(r2);
        lblVal3.setVisible(r3); txtVal3.setVisible(r3);
    }

    private void calculate() {
        try {
            String selected = (String) cbLoaiHinh.getSelectedItem();
            Hinh hinh = null;
            
            double v1 = txtVal1.isVisible() && !txtVal1.getText().isEmpty() ? Double.parseDouble(txtVal1.getText()) : 0;
            double v2 = txtVal2.isVisible() && !txtVal2.getText().isEmpty() ? Double.parseDouble(txtVal2.getText()) : 0;
            double v3 = txtVal3.isVisible() && !txtVal3.getText().isEmpty() ? Double.parseDouble(txtVal3.getText()) : 0;

            switch (selected) {
                case "Hình Tròn":
                    hinh = new HinhTron(v1);
                    break;
                case "Hình Vuông":
                    hinh = new HinhVuong(v1);
                    break;
                case "Hình Tam Giác":
                    hinh = new HinhTamGiac(v1, v2, v3);
                    if (!((HinhTamGiac)hinh).kiemTraHopLe()) {
                        lblResult.setText("Lỗi: 3 cạnh không tạo thành tam giác!");
                        return;
                    }
                    break;
                case "Hình Bình Hành":
                    hinh = new HinhBinhHanh(v1, v2, v3);
                    break;
            }

            if (hinh != null) {
                hinh.tinhChuVi();
                hinh.tinhDienTich();
                lblResult.setText(hinh.layKetQua());
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
        }
    }

    public static void main(String[] args) {
        // Chạy giao diện
        SwingUtilities.invokeLater(() -> {
            new Review().setVisible(true);
        });
    }
}