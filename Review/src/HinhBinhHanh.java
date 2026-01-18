import java.util.Scanner;

public class HinhBinhHanh extends Hinh {
    private double canhDay;
    private double canhBen;
    private double chieuCao;

    public HinhBinhHanh() {
        this.tenHinh = "Hình Bình Hành";
    }

    public HinhBinhHanh(double canhDay, double canhBen, double chieuCao) {
        this.tenHinh = "Hình Bình Hành";
        this.canhDay = canhDay;
        this.canhBen = canhBen;
        this.chieuCao = chieuCao;
    }

    @Override
    public void nhapThongTin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập cạnh đáy: "); canhDay = sc.nextDouble();
        System.out.print("Nhập cạnh bên: "); canhBen = sc.nextDouble();
        System.out.print("Nhập chiều cao: "); chieuCao = sc.nextDouble();
    }

    @Override
    public void tinhChuVi() {
        chuVi = 2 * (canhDay + canhBen);
    }

    @Override
    public void tinhDienTich() {
        dienTich = canhDay * chieuCao;
    }
}