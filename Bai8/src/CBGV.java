import java.util.Scanner;

public class CBGV extends Nguoi {

	private double luongCung;
	private double thuong;
	private double phat;
	private double luongThucLinh;

	public CBGV() {

	}

	public void nhapThongTin(Scanner sc) {
		super.nhapThongTin(sc);

		System.out.print("\tNhap luong cung: ");
		luongCung = sc.nextDouble();
		sc.nextLine();

		System.out.print("\tNhap thuong: ");
		thuong = sc.nextDouble();
		sc.nextLine();

		System.out.print("\tNhap phat: ");
		phat = sc.nextDouble();
		sc.nextLine();
		luongThucLinh = luongCung + thuong - phat;
	}

	public void hienThiThongTin() {
		super.hienThiThongTin();

		System.out.println("\tLuong cung: " + luongCung);
		System.out.println("\tThuong: " + thuong);
		System.out.println("\tPhat: " + phat);
		System.out.println("\tLuong thuc linh: " + luongThucLinh);
	}

	public double getLuongThucLinh() {
		return luongThucLinh;
	}
}
