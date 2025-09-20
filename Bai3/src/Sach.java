import java.util.Scanner;

public class Sach extends TaiLieu {
    private String tenTacGia;
    private int soTrang;

    public Sach(String maTaiLieu, String tenNhaXuatBan, int soBanPhatHanh, String tenTacGia, int soTrang) {
        super(maTaiLieu, tenNhaXuatBan, soBanPhatHanh);
        this.tenTacGia = tenTacGia;
        this.soTrang = soTrang;
    }
    public Sach() {
        // super();
    }
    @Override
    public void nhapThongTin(Scanner sc) {
        super.nhapThongTin(sc);
        System.out.print("Nhap ten tac gia: ");
        tenTacGia = sc.nextLine();
        System.out.print("Nhap so trang: ");
        soTrang = Integer.parseInt(sc.nextLine());
    }
    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Ten tac gia: " + tenTacGia);
        System.out.println("So trang: " + soTrang);
    }   
}
