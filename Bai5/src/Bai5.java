import java.util.Scanner;

public class Bai5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        KhuPho qlkp = new KhuPho();
        int choice;

        do {
            System.out.println("\n===== MENU QUAN LY KHU PHO =====");
            System.out.println("1. Nhap danh sach ho dan");
            System.out.println("2. Hien thi danh sach ho dan");
            System.out.println("3. Tim kiem ho dan co nguoi 80 tuoi");
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
                    qlkp.nhapDanhSach(sc);
                    break;
                case 2:
                    qlkp.hienThiDanhSach();
                    break;
                case 3:
                    System.out.println("Danh sach ho dan co nguoi 80 tuoi la:");
                    qlkp.timKiemThongTin();
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