import java.util.Scanner;

public class Bai7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
		
		QLHS list111=new QLHS();
		
		list111.nhapDanhSachHS(sc);
		
		System.out.print("Nhung hoc sinh sinh nam 1985 va que Thai Nguyen la:");
		list111.timKiemThongTin(1985, "Thai Nguyen");
		
		System.out.println("Nhung hoc sinh hoc lop 10A1 la:");
		list111.timKiemThongTin("10A1");
		
		sc.close();
    }
    
}
