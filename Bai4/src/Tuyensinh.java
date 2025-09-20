import java.util.ArrayList;
import java.util.Scanner;

public class Tuyensinh {

	private ArrayList<Thisinh> dsts;

	public Tuyensinh() {
		dsts = new ArrayList<Thisinh>(10);
	}

	public void themThisinh(Thisinh ts) {
		dsts.add(ts);
	}

	public void nhapDanhSach(Scanner sc) {
		Thisinh ts;
		int chon;

		System.out.print("Nhap vao so luong thi sinh: ");
		int n = sc.nextInt();
		sc.nextLine();

		for (int i = 0; i < n; i++) {
			System.out.println("Thi sinh thuoc khoi nao (1-KhoiA; 2-KhoiB; 3-KhoiC): ");
			chon = sc.nextInt();

			switch (chon) {
			case 1:
				ts = new KhoiA("Toan", "Ly", "Hoa");
				break;
			case 2:
				ts = new KhoiB("Toan", "Hoa", "Sinh");
				break;
			case 3:
				ts = new KhoiC("Van", "Su", "Dia");
				break;
			default:
				ts = new KhoiA("Toan", "Ly", "Hoa");
				break;
			}

			ts.nhapThongTin(sc);
			themThisinh(ts);
		}
	}

	public void hienThiDanhSach() {
		int n = dsts.size();
		for (int i = 0; i < n; i++) {
			System.out.println("Thi sinh thu " + (i + 1) + " la:");
			dsts.get(i).hienThiThongTin();
		}
	}

	public void timKiemThisinh(int soBaoDanh) {
		for (Thisinh ts : dsts) {
			if (ts.getSoBD() == soBaoDanh) {
				ts.hienThiThongTin();
			}
		}
	}
}
