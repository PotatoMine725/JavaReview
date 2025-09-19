import java.util.Scanner;

public class KySu extends CanBo {
    private String nganhDaoTao;

    public KySu() {}

    public KySu(String hoTen, String gioiTinh, String ngaySinh, String diaChi, String nganhDaoTao) {
        super(hoTen, gioiTinh, ngaySinh, diaChi);
        this.nganhDaoTao = nganhDaoTao;
    }
    public void nhapThongTin() {
        super.nhapThongTin();
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap nganh dao tao: ");
        nganhDaoTao = sc.nextLine();
    }
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Nganh dao tao: " + nganhDaoTao);
    }
}
