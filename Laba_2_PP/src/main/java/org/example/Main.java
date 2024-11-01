import java.util.Scanner;

class TaylorSeries {
    public double sinh(double x, double epsilon) {
        double sum = 0.0;
        double term = x; // первое слагаемое ряда
        int n = 1; // счётчик для степени и факториала

        while (Math.abs(term) >= epsilon) {
            sum += term;
            n++;
            term *= (x * x) / ((2 * n - 1) * (2 * n - 2)); // x^(2n+1) / (2n+1)!
        }

        return sum;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите значение x: ");
        double x = scanner.nextDouble();

        System.out.print("Введите натуральное число k: ");
        int k = scanner.nextInt();

        // Вычисляем ε
        double epsilon = Math.pow(10, -k);

        // Создаём экземпляр класса TaylorSeries
        TaylorSeries taylorSeries = new TaylorSeries();

        // Вычисляем значение через ряд Тейлора
        double taylorResult = taylorSeries.sinh(x, epsilon);

        // Вычисляем стандартное значение
        double standardResult = Math.sinh(x);

        System.out.printf("Результат с помощью ряда Тейлора: %,."+k+"f\n", taylorResult);
        System.out.printf("Стандартное значение sinh(x): %,."+k+"f\n", standardResult);

        // Округление и вывод целых значений в разных системах счисления
        int roundedTaylorResult = (int) Math.round(taylorResult);
        System.out.printf("Округлённый результат (десятичный): %d\n", roundedTaylorResult);
        System.out.printf("Округлённый результат (восьмеричный): %o\n", roundedTaylorResult);
        System.out.printf("Округлённый результат (шестнадцатеричный): %x\n", roundedTaylorResult);

        // Закрываем сканер
        scanner.close();
    }
}