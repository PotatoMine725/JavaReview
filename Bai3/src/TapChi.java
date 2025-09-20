import java.util.Scanner;

public class TapChi extends TaiLieu {
    private int soPhatHanh;
    private int thangPhatHanh;

    public TapChi(String maTaiLieu, String tenNhaXuatBan, int soBanPhatHanh, int soPhatHanh, int thangPhatHanh) {
        super(maTaiLieu, tenNhaXuatBan, soBanPhatHanh);
        this.soPhatHanh = soPhatHanh;
        this.thangPhatHanh = thangPhatHanh;
    }
    public TapChi() {
        // super();
    }
    @Override
    public void nhapThongTin(Scanner sc) {
        super.nhapThongTin(sc);
        System.out.print("Nhap so phat hanh: ");
        soPhatHanh = Integer.parseInt(sc.nextLine());
        System.out.print("Nhap thang phat hanh: ");
        thangPhatHanh = Integer.parseInt(sc.nextLine());
    }
    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("So phat hanh: " + soPhatHanh);
        System.out.println("Thang phat hanh: " + thangPhatHanh);
    }   
}
