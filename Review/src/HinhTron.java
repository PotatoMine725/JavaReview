import java.util.Scanner;

public class HinhTron extends Hinh {
    private double banKinh;

    public HinhTron() {
        this.tenHinh = "Hình Tròn";
    }

    // Constructor dùng cho UI
    public HinhTron(double banKinh) {
        this.tenHinh = "Hình Tròn";
        this.banKinh = banKinh;
    }

    @Override
    public void nhapThongTin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập bán kính: ");
        banKinh = sc.nextDouble();
    }

    @Override
    public void tinhChuVi() {
        chuVi = 2 * Math.PI * banKinh;
    }

    @Override
    public void tinhDienTich() {
        dienTich = Math.PI * banKinh * banKinh;
    }
}