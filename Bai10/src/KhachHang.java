import java.util.Scanner;

public class KhachHang {

	protected String tenChuHo;
	protected int soNha;
	protected String maCongTo;

	public KhachHang() {

	}

	public KhachHang(String tenChuHo, int soNha, String maCongTo) {
		this.tenChuHo = tenChuHo;
		this.soNha = soNha;
		this.maCongTo = maCongTo;
	}

	public void nhapThongTin(Scanner sc) {
		System.out.print("\tNhap ten chu ho: ");
		tenChuHo = sc.nextLine();
		System.out.print("\tNhap so nha: ");
		soNha = sc.nextInt();
		sc.nextLine();
		System.out.print("\tNhap ma cong to: ");
		maCongTo = sc.nextLine();
	}

	public void hienThiThongTin() {
		System.out.println("\tTen chu ho: " + tenChuHo);
		System.out.println("\tSo nha: " + soNha);
		System.out.println("\tMa cong to: " + maCongTo);
	}
}
