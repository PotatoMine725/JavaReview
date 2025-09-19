import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class NguoiDan {

	private String hoTen;
	private Date ngaySinh;
	private String ngheNghiep;

	public NguoiDan() {

	}

	public NguoiDan(String hoTen, Date ngaySinh, String ngheNghiep) {
		this.hoTen = hoTen;
		this.ngaySinh = ngaySinh;
		this.ngheNghiep = ngheNghiep;
	}

	public void nhapThongTin(Scanner sc) {
		System.out.print("\tNhap ho ten: ");
		hoTen = sc.nextLine();
		System.out.print("\tNhap ngay sinh: ");
		String strDate = sc.nextLine();
		ngaySinh = strToDate(strDate);
		System.out.print("\tNhap nghe nghiep: ");
		ngheNghiep = sc.nextLine();
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
		System.out.println("\tNgay sinh: " + dateToString(ngaySinh));
		System.out.println("\tNghe nghiep: " + ngheNghiep);
	}

	private String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		String strDate = sdf.format(date);

		return strDate;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}
}
