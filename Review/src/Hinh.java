public abstract class Hinh {
    protected double chuVi;
    protected double dienTich;
    protected String tenHinh;

    public abstract void nhapThongTin();
    public abstract void tinhChuVi();
    public abstract void tinhDienTich();

    public String layKetQua() {
        return String.format("Hình: %s | Chu vi: %.2f | Diện tích: %.2f", tenHinh, chuVi, dienTich);
    }
}