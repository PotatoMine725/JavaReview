import java.util.*;
public class JavaReview {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Nhap phan so 1: ");
        Phanso ps_111 = new Phanso();
        ps_111.nhapPhanSo();
        System.out.println("Nhap phan so 2: ");
        Phanso ps2 = new Phanso();
        ps2.nhapPhanSo();
        int choice; 
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Hien thi phan so");
            System.out.println("2. Rut gon phan so");
            System.out.println("3. Cong hai phan so");
            System.out.println("4. Tru hai phan so");
            System.out.println("5. Nhan hai phan so");
            System.out.println("6. Chia hai phan so");
            System.out.println("7. Kiem tra phan so toi gian");
            System.out.println("8. Thoat");
            System.out.print("Chon chuc nang (1-8): ");
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Phan so 1: ");
                    ps_111.hienThiPhanSo();
                    System.out.print("Phan so 2: ");
                    ps2.hienThiPhanSo();
                    break;
                case 2:
                    ps_111.rutGonPhanSo();
                    ps2.rutGonPhanSo();
                    System.out.println("Da rut gon hai phan so.");
                    break;
                case 3:
                    Phanso tong = ps_111.cong(ps2);
                    System.out.print("Tong hai phan so: ");
                    tong.rutGonPhanSo();
                    tong.hienThiPhanSo();
                    break;
                case 4:
                    Phanso hieu = ps_111.tru(ps2);
                    System.out.print("Hieu hai phan so: ");
                    hieu.rutGonPhanSo();
                    hieu.hienThiPhanSo();
                    break;
                case 5:
                    Phanso tich = ps_111.nhan(ps2);
                    System.out.print("Tich hai phan so: ");
                    tich.rutGonPhanSo();
                    tich.hienThiPhanSo();
                    break;
                case 6:
                    Phanso thuong = ps_111.chia(ps2);
                    System.out.print("Thuong hai phan so: ");
                    thuong.rutGonPhanSo();
                    thuong.hienThiPhanSo();
                    break;
                 case 7:
                    if (ps_111.laToiGian()) {
                        System.out.println("Phan so 1 la phan so toi gian.");
                    } else {
                        System.out.println("Phan so 1 khong phai la phan so toi gian.");
                    }
                    if (ps2.laToiGian()) {
                        System.out.println("Phan so 2 la phan so toi gian.");
                    } else {
                        System.out.println("Phan so 2 khong phai la phan so toi gian.");
                    }
                    break;
                case 8:
                    System.out.println("Thoat chuong trinh.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le. Vui long chon lai.");
            }
        } while (choice != 8);
    }
    
}
