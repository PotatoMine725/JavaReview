import java.util.Scanner;

public class Bai6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        KhachSan qlKhachSan = new KhachSan();
        int choice;

        do {
            System.out.println("\n===== MENU QUAN LY KHACH SAN =====");
            System.out.println("1. Nhap danh sach khach tro");
            System.out.println("2. Hien thi danh sach khach tro");
            System.out.println("3. Tinh tien cho khach tro theo CMND");
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
                    qlKhachSan.nhapDanhSach(sc);
                    break;
                case 2:
                    qlKhachSan.hienThiDanhSach();
                    break;
                case 3:
                    System.out.print("Nhap vao CMND khach tro can tinh tien: ");
                    try {
                        int cmnd = Integer.parseInt(sc.nextLine());
                        double tongTien = qlKhachSan.tinhTien(cmnd);
                        System.out.println("==> Tong tien la: " + tongTien);
                    } catch (NumberFormatException e) {
                        System.out.println("CMND phai la so nguyen!");
                    }
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