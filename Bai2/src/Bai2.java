import java.util.ArrayList;

public class Bai2 {
    public static void main(String[] args) {
        int choice;
        QLCB dscb_111 = new QLCB();
        java.util.Scanner sc = new java.util.Scanner(System.in);

        do {
            System.out.println("\n===== MENU CHINH =====");
            System.out.println("1. Nhap danh sach can bo");
            System.out.println("2. Hien thi thong tin danh sach can bo");
            System.out.println("3. Tim kiem can bo theo ho ten");
            System.out.println("4. Thoat");
            System.out.print("Nhap lua chon cua ban (1-4): ");
            
            choice = sc.nextInt();
            
            switch (choice) {
                case 1:
                    int loaiCanBo;
                    do {
                        System.out.println("\n--- MENU NHAP CAN BO ---");
                        System.out.println("1. Cong nhan");
                        System.out.println("2. Ky su");
                        System.out.println("3. Nhan vien");
                        System.out.println("4. Quay lai menu chinh");
                        System.out.print("Nhap lua chon cua ban (1-4): ");
                        loaiCanBo = sc.nextInt();

                        if (loaiCanBo == 1) {
                            CongNhan cn = new CongNhan();
                            cn.nhapThongTin();
                            dscb_111.themCanBo(cn);
                        } else if (loaiCanBo == 2) {
                            KySu ks = new KySu();
                            ks.nhapThongTin();
                            dscb_111.themCanBo(ks);
                        } else if (loaiCanBo == 3) {
                            NhanVien nv = new NhanVien();
                            nv.nhapThongTin();
                            dscb_111.themCanBo(nv);
                        } else if (loaiCanBo == 4) {
                            System.out.println("Quay lai menu chinh...");
                        } else {
                            System.out.println("Lua chon khong hop le. Vui long chon lai.");
                        }
                    } while (loaiCanBo != 4);
                    break;

                case 2:
                    dscb_111.hienThiDanhSach();
                    break;

                case 3:
                    sc.nextLine(); 
                    System.out.print("Nhap ho ten can tim: ");
                    String hoTen = sc.nextLine();
                    ArrayList<CanBo> ketQua = dscb_111.timKiemTheoTen(hoTen);
                    if (ketQua.isEmpty()) {
                        System.out.println("Khong tim thay can bo nao co ten chua: " + hoTen);
                    } else {
                        System.out.println("Ket qua tim kiem:");
                        for (CanBo cb : ketQua) {
                            cb.hienThiThongTin();
                            System.out.println("-----------------------");
                        }
                    }
                    break;

                case 4:
                    System.out.println("Thoat chuong trinh.");
                    break;

                default:
                    System.out.println("Lua chon khong hop le. Vui long chon lai.");
            }
        } while (choice != 4);
    }
}

