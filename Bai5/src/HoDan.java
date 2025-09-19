import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class HoDan {
	private int soNha;
	private ArrayList<NguoiDan> danhSachNguoi;

	public HoDan() {
		danhSachNguoi = new ArrayList<>();
	}

	public void nhapThongTin(Scanner sc) {
		System.out.print("\tNhap so nha: ");
		soNha = Integer.parseInt(sc.nextLine());

		System.out.print("\tNhap so nguoi: ");
		int soNguoi = Integer.parseInt(sc.nextLine());

		System.out.println("\tNhap thong tin cho tung nguoi trong ho:");
		for (int i = 0; i < soNguoi; i++) {
			System.out.println("Nguoi thu " + (i + 1) + " la:");
			NguoiDan nguoi = new NguoiDan();
			nguoi.nhapThongTin(sc);
			danhSachNguoi.add(nguoi);
		}
	}

	public void hienThiThongTin() {
		System.out.println("\tSo nha: " + soNha);
		System.out.println("\tSo nguoi: " + danhSachNguoi.size());
		System.out.println("\tThong tin cua tung nguoi trong gia dinh:");
		for (int i = 0; i < danhSachNguoi.size(); i++) {
			System.out.println("Nguoi thu " + (i + 1) + " la:");
			danhSachNguoi.get(i).hienThiThongTin();
		}
	}

	public ArrayList<NguoiDan> getList() {
		return danhSachNguoi;
	}

	public int getSoNguoi() {
		return danhSachNguoi.size();
	}
}