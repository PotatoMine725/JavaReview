import java.util.ArrayList;

public class QLCB {
    private ArrayList<CanBo> dscb;
    public QLCB() {
        dscb = new ArrayList<>();
    }
    public void themCanBo(CanBo cb) {
        dscb.add(cb);
    }
    public void hienThiDanhSach() {
        for (CanBo cb : dscb) {
            cb.hienThiThongTin();
            System.out.println("-----------------------");
        }
    }
    public ArrayList<CanBo> timKiemTheoTen(String ten) {
        ArrayList<CanBo> ketQua = new ArrayList<>();
        for (CanBo cb : dscb) {
            String hoTen = cb.getHoTen().toLowerCase();
            if (hoTen.contains(ten.toLowerCase())) {
                ketQua.add(cb);
            }
        }
        return ketQua;
    }

    public void nhapDanhSach(ArrayList<CanBo> danhSach) {
        dscb.addAll(danhSach);
    }
}
