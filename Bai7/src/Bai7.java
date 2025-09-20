import java.util.Scanner;

public class Bai7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QLHS qlhs = new QLHS();
        int choice;

        do {
            System.out.println("\n===== MENU QUAN LY HOC SINH =====");
            System.out.println("1. Nhap danh sach hoc sinh");
            System.out.println("2. Hien thi danh sach hoc sinh");
            System.out.println("3. Tim hoc sinh sinh nam 1985 va que Thai Nguyen");
            System.out.println("4. Tim hoc sinh theo lop");
            System.out.println("0. Thoat");
            System.out.print("Lua chon cua ban: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so hop le!");
                choice = -1;
                continue;
            }

            switch (choice) {
                case 1:
                    qlhs.nhapDanhSachHS(sc);
                    break;
                case 2:
                    qlhs.hienThiDanhSach();
                    break;
                case 3:
                    System.out.println("Nhung hoc sinh sinh nam 1985 va que Thai Nguyen la:");
                    qlhs.timKiemThongTin(1985, "Thai Nguyen");
                    break;
                case 4:
                    System.out.print("Nhap ten lop can tim: ");
                    String lop = sc.nextLine();
                    System.out.println("Nhung hoc sinh hoc lop " + lop + " la:");
                    qlhs.timKiemThongTin(lop);
                    break;
                case 0:
                    System.out.println("Thoat chuong trinh.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le. Vui long thu lai.");
            }
        } while (choice != 0);

        sc.close();
    }
}