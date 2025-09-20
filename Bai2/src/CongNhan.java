import java.util.Scanner;

public class CongNhan extends CanBo {
    private String bac;

    public CongNhan() {}

    public CongNhan(String hoTen, String gioiTinh, String ngaySinh, String diaChi, String bac) {
        super(hoTen, gioiTinh, ngaySinh, diaChi);
        this.bac = bac;
    }
    @Override
    public void nhapThongTin() {
        super.nhapThongTin();
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap bac: ");
        bac = sc.nextLine();
    }
    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Bac: " + bac);
    }
    
}
