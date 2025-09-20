import java.util.ArrayList;
import java.util.Scanner;

public class QuanLy {

	private ArrayList<BienLai> ds;

	public QuanLy() {
		ds = new ArrayList<BienLai>(10);
	}

	public void themBienLai(BienLai bienLai) {
		ds.add(bienLai);
	}

	public void nhapDanhSach(Scanner sc) {
		BienLai bienLai;
		System.out.print("Nhap vao so luong bien lai: ");
		int n = sc.nextInt();
		sc.nextLine();
		System.out.println("Nhap vao danh sach bien lai:");
		for (int i = 0; i < n; i++) {
			System.out.println("Bien lai thu " + (i + 1) + " la:");
			bienLai = new BienLai();
			bienLai.nhapThongTin(sc);

			themBienLai(bienLai);
		}
	}

	public void hienThiDanhSach() {
		for (int i = 0; i < ds.size(); i++) {
			System.out.println("Bien lai thu " + (i + 1) + " la:");
			ds.get(i).hienThiThongTin();
			System.out.println();
		}
	}
}
