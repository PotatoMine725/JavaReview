import java.util.Scanner;

public class Bai3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QuanlySach qls = new QuanlySach();
        int choice;

        do {
            System.out.println("\n===== MENU QUAN LY TAI LIEU =====");
            System.out.println("1. Nhap tai lieu");
            System.out.println("2. Tim tai lieu theo ma");
            System.out.println("3. Tim tai lieu theo loai (Sach, TapChi, Bao)");
            System.out.println("4. Hien thi tat ca tai lieu");
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
                    qls.nhapTaiLieu(sc);
                    break;
                case 2:
                    qls.timMaTaiLieu(sc);
                    break;
                case 3:
                    System.out.print("Nhap loai tai lieu can tim (Sach, TapChi, Bao): ");
                    String loai = sc.nextLine();
                    qls.timLoaiTaiLieu(loai);
                    break;
                case 4:
                    qls.hienThiTatCa();
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