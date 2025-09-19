import java.util.*;

public class Phanso {

    private int tuSo;
    private int mauSo;
    
    public int getTuSo() {
        return tuSo;
    }
    public int getMauSo() {
        return mauSo;
    }
    public void setTuSo(int tuSo) {
        this.tuSo = tuSo;
    }
    public void setMauSo(int mauSo) {
        this.mauSo = mauSo;
    }
    public Phanso(int tuSo, int mauSo) {
        this.tuSo = tuSo;
        this.mauSo = mauSo;
    }
    public Phanso() {}

    public void nhapPhanSo() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap tu so: ");
        tuSo = sc.nextInt();
        do {
            System.out.print("Nhap mau so: ");
            mauSo = sc.nextInt();
            if (mauSo == 0) {
                System.out.println("Mau so khong the bang 0. Vui long nhap lai.");
            }
        } while (mauSo == 0);
    }
    public void hienThiPhanSo() {
        System.out.println("Phan so: " + tuSo + "/" + mauSo);
    }
    private int ucln(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    public void rutGonPhanSo() {
        int ucln = ucln(tuSo, mauSo);
        tuSo /= ucln;
        mauSo /= ucln;
    }
    public Phanso cong(Phanso ps) {
        int tuSoMoi = this.tuSo * ps.mauSo + ps.tuSo * this.mauSo;
        int mauSoMoi = this.mauSo * ps.mauSo;
        Phanso ketQua = new Phanso(tuSoMoi, mauSoMoi);
        ketQua.rutGonPhanSo();
        return ketQua;
    }
    public Phanso tru(Phanso ps) {
        int tuSoMoi = this.tuSo * ps.mauSo - ps.tuSo * this.mauSo;
        int mauSoMoi = this.mauSo * ps.mauSo;
        Phanso ketQua = new Phanso(tuSoMoi, mauSoMoi);
        ketQua.rutGonPhanSo();
        return ketQua;
    }
    public Phanso nhan(Phanso ps) {
        int tuSoMoi = this.tuSo * ps.tuSo;
        int mauSoMoi = this.mauSo * ps.mauSo;
        Phanso ketQua = new Phanso(tuSoMoi, mauSoMoi);
        ketQua.rutGonPhanSo();
        return ketQua;
    }

    public Phanso chia(Phanso ps) {
        if (ps.tuSo == 0) {
            throw new IllegalArgumentException("Khong the chia cho phan so co tu so bang 0.");
        }
        int tuSoMoi = this.tuSo * ps.mauSo;
        int mauSoMoi = this.mauSo * ps.tuSo;
        Phanso ketQua = new Phanso(tuSoMoi, mauSoMoi);
        ketQua.rutGonPhanSo();
        return ketQua;
    }
    public boolean laToiGian() {
        return ucln(tuSo, mauSo) == 1;
    }
}
