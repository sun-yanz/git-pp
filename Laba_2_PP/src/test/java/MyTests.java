package taylorTest;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class taylorTest {
    private final taylor taylorInstance = new taylor();

    @Test
    public void testLn_double() {
        double result = taylorInstance.ln(1.99, 5); // ln(1.99) ~= 0.688130
        assertEquals(Math.log(1.99), result, 0.0001, "ln(2) должно быть около 0.688130");

        result = taylorInstance.ln(1.5, 5); // ln(1.5) ~= 0.40547
        assertEquals(Math.log(1.5), result, 0.0001, "ln(1.5) должно быть около 0.40547");
    }

    @Test
    public void testLn2_BigDecimal() {
        BigDecimal x = new BigDecimal("0.99");
        BigInteger precision = new BigInteger("5");

        BigDecimal expected = new BigDecimal("-0.01005");
        BigDecimal epsilon = new BigDecimal("0.0001"); // Допустимая погрешность
        BigDecimal result = taylorInstance.ln2(x, precision);

        BigDecimal difference = expected.subtract(result).abs();
        assertTrue(difference.compareTo(epsilon) < 0,
                "Разница слишком велика: ln(0.99) должно быть около -0.01005, но результат " + result);
    }
}