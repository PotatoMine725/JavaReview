import java.util.ArrayList;
import java.util.Scanner;
public class QuanlySach {
    private ArrayList<TaiLieu> dstl;
    public QuanlySach() {
        dstl = new ArrayList<>();
    }
    public void themTaiLieu(TaiLieu tl) {
        dstl.add(tl);
    }
    public void nhapTaiLieu(Scanner sc) {
        int choice;
        TaiLieu tl;
        do {
            System.out.println("Chọn loại tài liệu để nhập:");
            System.out.println("1. Sách");
            System.out.println("2. Tạp chí");
            System.out.println("3. Báo");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn của bạn: ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    tl = new Sach();
                    tl.nhapThongTin(sc);
                    themTaiLieu(tl);
                    break;
                case 2:
                    tl = new TapChi();
                    tl.nhapThongTin(sc);
                    themTaiLieu(tl);
                    break;
                case 3:
                    tl = new Bao();
                    tl.nhapThongTin(sc);
                    themTaiLieu(tl);
                    break;
                case 0:
                    System.out.println("Thoát nhập tài liệu.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        } while (choice != 0);
    }
    public void timMaTaiLieu(Scanner sc) {
        System.out.print("Nhập mã tài liệu cần tìm: ");
        String maTim = sc.nextLine();
        boolean found = false;
        for (TaiLieu tl : dstl) {
            if (tl.getMaTaiLieu().equals(maTim)) {
                tl.hienThiThongTin();
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Không tìm thấy tài liệu với mã: " + maTim);
        }
    }
    public void timLoaiTaiLieu(String loai) {
        for (TaiLieu tl : dstl) {
            if ((loai.equalsIgnoreCase("Sach") && tl instanceof Sach) ||
                (loai.equalsIgnoreCase("TapChi") && tl instanceof TapChi) ||
                (loai.equalsIgnoreCase("Bao") && tl instanceof Bao)) {
                tl.hienThiThongTin();
                System.out.println("-----------------------");
            }
        }
    }
}
