import java.util.Scanner;

public class Bai5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

		KhuPho qlkp111 = new KhuPho();

		qlkp111.nhapDanhSach(sc);

		System.out.println("danh sach ho dan co nguoi 80 tuoi la:");
		qlkp111.timKiemThongTin();
	}  
}
