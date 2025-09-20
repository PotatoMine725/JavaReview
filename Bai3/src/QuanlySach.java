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
        int choice = -1;
        TaiLieu tl;
        do {
            System.out.println("Chon loai tai lieu de nhap:");
            System.out.println("1. Sach");
            System.out.println("2. Tap chi");
            System.out.println("3. Bao");
            System.out.println("0. Thoat");
            System.out.print("Lua chon cua ban: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so hop le!");
                continue;
            }

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
                    System.out.println("Thoat nhap tai lieu.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le. Vui long thu lai.");
            }
        } while (choice != 0);
    }

    public void timMaTaiLieu(Scanner sc) {
        System.out.print("Nhap ma tai lieu can tim: ");
        String maTim = sc.nextLine();
        boolean found = false;
        for (TaiLieu tl : dstl) {
            if (tl.getMaTaiLieu().equalsIgnoreCase(maTim)) {
                tl.hienThiThongTin();
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay tai lieu voi ma: " + maTim);
        }
    }

    public void timLoaiTaiLieu(String loai) {
        boolean found = false;
        for (TaiLieu tl : dstl) {
            if ((loai.equalsIgnoreCase("Sach") && tl instanceof Sach) ||
                (loai.equalsIgnoreCase("TapChi") && tl instanceof TapChi) ||
                (loai.equalsIgnoreCase("Bao") && tl instanceof Bao)) {
                tl.hienThiThongTin();
                System.out.println("-----------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay tai lieu loai: " + loai);
        }
    }

    // Thêm phương thức hiển thị toàn bộ
    public void hienThiTatCa() {
        if (dstl.isEmpty()) {
            System.out.println("Danh sach tai lieu rong.");
            return;
        }
        for (TaiLieu tl : dstl) {
            tl.hienThiThongTin();
            System.out.println("-----------------------");
        }
    }
}