import java.util.Scanner;
public class Bai3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QuanlySach qls111 = new QuanlySach();
        qls111.nhapTaiLieu(sc);
        System.out.println("\n\n Nhập mã tài liệu cần tìm: ");
        qls111.timMaTaiLieu(sc);
        String loai = sc.nextLine();
        System.out.println("\n\n Nhập loại tài liệu cần tìm (Sách, Tạp chí, Báo): ");
        qls111.timLoaiTaiLieu(loai);
        sc.close();
    }
}
