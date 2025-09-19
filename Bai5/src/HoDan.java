import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class HoDan extends Nguoi {
	
	private int soNguoi;
	private int soNha;
	private Nguoi[] list; 

	public HoDan() {
		list = new Nguoi[10];
	}

	public HoDan(String hoTen, Date ngaySinh, String ngheNghiep, int soNguoi, int soNha) {
		super(hoTen, ngaySinh, ngheNghiep);
		this.soNguoi = soNguoi;
		this.soNha = soNha;
	}

	public void nhapThongTin(Scanner sc) {
		System.out.print("\tNhap so nguoi: ");
		soNguoi = sc.nextInt();
		sc.nextLine();
		System.out.print("\tNhap so nha: ");
		soNha = sc.nextInt();
		sc.nextLine();
		System.out.println("\tNhap thong tin cho tung nguoi trong ho:");
		for (int i = 0; i < soNguoi; i++) {
			System.out.println("Nguoi thu " + (i + 1) + " la:");
			list[i] = new Nguoi();
			list[i].nhapThongTin(sc);
		}
	}

	public void hienThiThongTin() {
		System.out.println("\tSo nguoi: " + soNguoi);
		System.out.println("\tSo nha: " + soNha);
		System.out.println("\tThong tin cua tung nguoi trong gia dinh:");
		for (int i = 0; i < soNguoi; i++) {
			System.out.println("Nguoi thu " + (i + 1) + " la:");
			list[i].hienThiThongTin();
		}
	}

	public Nguoi[] getList() {
		return list;
	}

	public int getSoNguoi() {
		return soNguoi;
	}
}
