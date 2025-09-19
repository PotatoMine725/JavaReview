import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class KhuPho {

	private ArrayList<HoDan> dshd;

	public KhuPho() {
		dshd = new ArrayList<HoDan>(10);
	}

	public void themHoDan(HoDan hoDan) {
		dshd.add(hoDan);
	}

	public void nhapDanhSach(Scanner sc) {
		System.out.print("Nhap vao so ho dan: ");
		int soHoDan = sc.nextInt();
		sc.nextLine();

		System.out.println("Nhap vao thong tin cho tung ho dan:");
		for (int i = 0; i < soHoDan; i++) {
			System.out.println("Ho dan thu " + (i + 1) + " la:");
			HoDan hoDan = new HoDan();
			hoDan.nhapThongTin(sc);
			themHoDan(hoDan);
		}
	}

	public void hienThiDanhSach() {
		for (int i = 0; i < dshd.size(); i++) {
			System.out.println("Ho dan thu " + (i + 1) + " la:");
			dshd.get(i).hienThiThongTin();
		}
	}

	public void timKiemThongTin() {
		Date now = new Date();
		int namHienTai = now.getYear() + 1900;

		for (HoDan hoDan : dshd) {
			ArrayList<NguoiDan> dstv = hoDan.getList();
			for (NguoiDan nguoi : dstv) {
				int namSinh = nguoi.getNgaySinh().getYear() + 1900;
				if (namHienTai - namSinh == 80) {
					System.out.println("Tim thay nguoi 80 tuoi trong ho dan:");
					hoDan.hienThiThongTin();
					break; // Nếu chỉ cần hiển thị một lần cho mỗi hộ
				}
			}
		}
	}
}