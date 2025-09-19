import java.util.Scanner;

public class HocSinh extends Nguoi {

	private String lop;
	private String khoHoc;
	private String kyHoc;

	public HocSinh() {

	}

	public HocSinh(String hoTen, String lop, String khoaHoc, String kyHoc) {
		this.hoTen = hoTen;
		this.lop = lop;
		this.khoHoc = khoaHoc;
		this.kyHoc = kyHoc;
	}

	public void nhapThongTin(Scanner sc) {
		super.nhapThongTin(sc);

		System.out.print("\tNhap lop: ");
		lop = sc.nextLine();

		System.out.print("\tNhap khoa hoc: ");
		khoHoc = sc.nextLine();

		System.out.print("\tNhap ky hoc: ");
		kyHoc = sc.nextLine();
	}

	public void hienThiThongTin() {
		super.hienThiThongTin();

		System.out.println("\tLop: " + lop);
		System.out.println("\tKhoa hoc: " + khoHoc);
		System.out.println("\tKy hoc: " + kyHoc);
	}

	public String getLop() {
		return lop;
	}
}
