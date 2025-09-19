import java.util.Scanner;
public class Bai4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		Tuyensinh dsTuyenSinh111 = new Tuyensinh();
		System.out.println("Nhap vao danh sach thi sinh: ");
		dsTuyenSinh111.nhapDanhSach(sc);

		System.out.println("Danh sach thi sinh du thi la:");
		dsTuyenSinh111.hienThiDanhSach();

		System.out.print("Nhap so bao danh can tim: ");
		int soBaoDanh = sc.nextInt();
		sc.nextLine();

		dsTuyenSinh111.timKiemThisinh(soBaoDanh);

		sc.close();
	}
    
}
