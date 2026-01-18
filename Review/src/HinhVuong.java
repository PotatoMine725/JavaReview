import java.util.Scanner;

public class HinhVuong extends Hinh {
    private double canh;

    public HinhVuong() {
        this.tenHinh = "Hình Vuông";
    }

    public HinhVuong(double canh) {
        this.tenHinh = "Hình Vuông";
        this.canh = canh;
    }

    @Override
    public void nhapThongTin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập cạnh: ");
        canh = sc.nextDouble();
    }

    @Override
    public void tinhChuVi() {
        chuVi = canh * 4;
    }

    @Override
    public void tinhDienTich() {
        dienTich = canh * canh;
    }
}