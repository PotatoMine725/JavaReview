import java.util.Scanner;

public class Thisinh {
    
	private int soBD;
	private String hoTen;
	private String diaChi;
	private String dienUuTien;

	public Thisinh() {

	}

	public Thisinh(int soBD, String hoTen, String diaChi, String dienUuTien) {
		this.soBD = soBD;
		this.hoTen = hoTen;
		this.diaChi = diaChi;
		this.dienUuTien = dienUuTien;
	}

	public void nhapThongTin(Scanner sc) {
		System.out.print("\tNhap so bao danh: ");
		soBD = sc.nextInt();
		sc.nextLine();
		System.out.print("\tNhap ho ten: ");
		hoTen = sc.nextLine();
		System.out.print("\tNhap dia chi: ");
		diaChi = sc.nextLine();
		System.out.print("\tNhap dien uu tien: ");
		dienUuTien = sc.nextLine();
	}

	public void hienThiThongTin() {
		System.out.println("\tSo bao danh: " + soBD);
		System.out.println("\tHo ten: " + hoTen);
		System.out.println("\tDia chi: " + diaChi);
		System.out.println("\tDien uu tien: " + dienUuTien);
	}

	public int getSoBD() {
		return soBD;
	}
}
