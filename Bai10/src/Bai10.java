
import java.util.Scanner;

/**
 *
 * @author Wotbl
 */
public class Bai10 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

		QuanLy ql = new QuanLy();
		ql.nhapDanhSach(sc);

		System.out.println("Danh sach bien lai cua cac ho gia dinh la:");
		ql.hienThiDanhSach();

		sc.close();
    }
    
}
