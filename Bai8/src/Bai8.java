
import java.util.Scanner;

/**
 *
 * @author Wotbl
 */
public class Bai8 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

		QuanLy ql111 = new QuanLy();

		ql111.nhapDanhSach(sc);

		System.out.println("Danh sach nhung can bo co luong >= 8 trieu la:");
		ql111.timKiem(8000000);

		sc.close();
	} 
}
