package Calculator;

public class CalculatorModel {
    
    // Thực hiện phép tính dựa trên 2 số và toán tử
    public double calculate(double number1, double number2, String operator) throws ArithmeticException {
        switch (operator) {
            case "+":
                return number1 + number2;
            case "-":
                return number1 - number2;
            case "*":
                return number1 * number2;
            case "/":
                if (number2 == 0) {
                    throw new ArithmeticException("Không thể chia cho 0");
                }
                return number1 / number2;
            default:
                return 0;
        }
    }
}