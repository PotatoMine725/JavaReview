import java.util.Scanner;
public class Bao extends TaiLieu {
    private String ngayPhatHanh;
    private String soPhatHanh;

    public Bao(String maTaiLieu, String tenNhaXuatBan, int soBanPhatHanh, String ngayPhatHanh, String soPhatHanh) {
        super(maTaiLieu, tenNhaXuatBan, soBanPhatHanh);
        this.ngayPhatHanh = ngayPhatHanh;
        this.soPhatHanh = soPhatHanh;
    }
    public Bao() {
        // super();
    }
    @Override
    public void nhapThongTin(Scanner sc) {
        super.nhapThongTin(sc);
        System.out.print("Nhập ngày phát hành: ");
        ngayPhatHanh = sc.nextLine();
        System.out.print("Nhập số phát hành: ");
        soPhatHanh = sc.nextLine();
    }
    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Ngày phát hành: " + ngayPhatHanh);
        System.out.println("Số phát hành: " + soPhatHanh);
    }
    
}
