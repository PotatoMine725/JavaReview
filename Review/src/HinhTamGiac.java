import java.util.Scanner;

public class HinhTamGiac extends Hinh {
    private double a, b, c;

    public HinhTamGiac() {
        this.tenHinh = "Hình Tam Giác";
    }

    public HinhTamGiac(double a, double b, double c) {
        this.tenHinh = "Hình Tam Giác";
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public void nhapThongTin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập cạnh a: "); a = sc.nextDouble();
        System.out.print("Nhập cạnh b: "); b = sc.nextDouble();
        System.out.print("Nhập cạnh c: "); c = sc.nextDouble();
    }

    @Override
    public void tinhChuVi() {
        chuVi = a + b + c;
    }

    @Override
    public void tinhDienTich() {
        // Công thức Heron
        double p = chuVi / 2;
        dienTich = Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }
    
    public boolean kiemTraHopLe() {
        return (a + b > c) && (a + c > b) && (b + c > a);
    }
}