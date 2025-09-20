import java.util.Scanner;
public class TaiLieu {
    private String maTaiLieu;
    private String tenNhaXuatBan;
    private int soBanPhatHanh;

    public TaiLieu(String maTaiLieu, String tenNhaXuatBan, int soBanPhatHanh) {
        this.maTaiLieu = maTaiLieu;
        this.tenNhaXuatBan = tenNhaXuatBan;
        this.soBanPhatHanh = soBanPhatHanh;
    }
    public TaiLieu() {
        
    }
    public void nhapThongTin(Scanner sc) {
        System.out.print("Nhap ma tai lieu: ");
        maTaiLieu = sc.nextLine();
        System.out.print("Nhap ten nha xuat ban: ");
        tenNhaXuatBan = sc.nextLine();
        System.out.print("Nhap so ban phat hanh: ");
        soBanPhatHanh = Integer.parseInt(sc.nextLine());
        
    }
    public void hienThiThongTin() {
        System.out.println("Ma tai lieu: " + maTaiLieu);
        System.out.println("Ten nha xuat ban: " + tenNhaXuatBan);
        System.out.println("So ban phat hanh: " + soBanPhatHanh);
    }
    public String getMaTaiLieu() {
        return maTaiLieu;
    }
}
