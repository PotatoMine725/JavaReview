import java.util.Scanner;

public class Bai4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Tuyensinh dsTuyenSinh = new Tuyensinh();
        int choice;

        do {
            System.out.println("\n===== MENU TUYEN SINH =====");
            System.out.println("1. Nhap danh sach thi sinh");
            System.out.println("2. Hien thi danh sach thi sinh");
            System.out.println("3. Tim kiem thi sinh theo so bao danh");
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
                    dsTuyenSinh.nhapDanhSach(sc);
                    break;
                case 2:
                    dsTuyenSinh.hienThiDanhSach();
                    break;
                case 3:
                    System.out.print("Nhap so bao danh can tim: ");
                    try {
                        int soBaoDanh = Integer.parseInt(sc.nextLine());
                        dsTuyenSinh.timKiemThisinh(soBaoDanh);
                    } catch (NumberFormatException e) {
                        System.out.println("So bao danh phai la so nguyen!");
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