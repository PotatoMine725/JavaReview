import java.util.Scanner;

public class Bai6 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

		KhachSan ql111 = new KhachSan();

		ql111.nhapDanhSach(sc);

		System.out.print("Nhap vao khach tro can tinh tien: ");
		int cmnd = sc.nextInt();

		System.out.println("==> Tong tien la: " + ql111.tinhTien(cmnd));

		sc.close();
    }
    
}
