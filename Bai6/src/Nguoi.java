
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Nguoi {

	protected String hoTen;
	protected Date ngaySinh;
	protected int CMND;

	public Nguoi() {

	}

	public Nguoi(String hoTen, Date ngaySinh, int CMND) {
		this.hoTen = hoTen;
		this.ngaySinh = ngaySinh;
		this.CMND = CMND;
	}

	public void nhapThongTin(Scanner sc) {
		System.out.print("\tNhap ho ten: ");
		hoTen = sc.nextLine();
		System.out.print("\tNhap ngay sinh: ");
		String strDate = sc.nextLine();
		ngaySinh = strToDate(strDate);
		System.out.print("\tNhap CMND: ");
		CMND = sc.nextInt();
	}

	private Date strToDate(String strDate) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			System.out.println("Loi dinh dang ngay thang!");
		}

		return date;
	}

	public void hienThiThongTin() {
		System.out.println("\tHo ten: " + hoTen);
		System.out.println("\tNgay sinh: " + ngaySinh);
		System.out.println("\tCMND: " + CMND);
	}

	public int getCMND() {
		return CMND;
	}
}
