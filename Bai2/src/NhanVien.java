import java.util.Scanner;

public class NhanVien extends CanBo {
    private String congViec;

    public NhanVien() {}

    public NhanVien(String hoTen, String gioiTinh, String ngaySinh, String diaChi, String congViec) {
        super(hoTen, gioiTinh, ngaySinh, diaChi);
        this.congViec = congViec;
    }
    public void nhapThongTin() {
        super.nhapThongTin();
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap cong viec: ");
        congViec = sc.nextLine();
    }
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Cong viec: " + congViec);
    } 
}
