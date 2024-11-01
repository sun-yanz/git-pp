import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TaylorSeriesTest {

    @Test
    public void testSinhZero() {
        TaylorSeries ts = new TaylorSeries();
        double result = ts.sinh(0, 0.0001);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testSinhPositiveValue() {
        TaylorSeries ts = new TaylorSeries();
        double result = ts.sinh(1, 0.0001);
        assertEquals(Math.sinh(1), result, 0.0001);
    }

    @Test
    public void testSinhNegativeValue() {
        TaylorSeries ts = new TaylorSeries();
        double result = ts.sinh(-1, 0.0001);
        assertEquals(Math.sinh(-1), result, 0.0001);
    }

    @Test
    public void testSinhLargeValue() {
        TaylorSeries ts = new TaylorSeries();
        double result = ts.sinh(10, 0.0001);
        assertEquals(Math.sinh(10), result, 0.0001);
    }

    @Test
    public void testSinhSmallEpsilon() {
        TaylorSeries ts = new TaylorSeries();
        double result = ts.sinh(1, 0.00000001);
        assertEquals(Math.sinh(1), result, 0.00000001);
    }
}