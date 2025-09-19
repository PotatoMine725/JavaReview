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
        System.out.print("Nhập mã tài liệu: ");
        maTaiLieu = sc.nextLine();
        System.out.print("Nhập tên nhà xuất bản: ");
        tenNhaXuatBan = sc.nextLine();
        System.out.print("Nhập số bản phát hành: ");
        soBanPhatHanh = Integer.parseInt(sc.nextLine());
        
    }
    public void hienThiThongTin() {
        System.out.println("Mã tài liệu: " + maTaiLieu);
        System.out.println("Tên nhà xuất bản: " + tenNhaXuatBan);
        System.out.println("Số bản phát hành: " + soBanPhatHanh);
    }
    public String getMaTaiLieu() {
        return maTaiLieu;
    }
}
