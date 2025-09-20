
import java.util.Scanner;

/**
 *
 * @author Wotbl
 */
public class Bai9 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

		QuanLy ql111 = new QuanLy();

		ql111.nhapDanhSach(sc);

		System.out.println("Danh sach sinh vien tra sach vao cuoi thang:");
		ql111.hienThiDanhSachTraCuoiThang();

		sc.close();
    }
    
}
