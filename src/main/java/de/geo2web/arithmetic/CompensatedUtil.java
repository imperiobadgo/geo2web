package de.geo2web.arithmetic;

public class CompensatedUtil {

    /**
     * Summation algorithm, also known as compensated summation introduced by Neumaier. <a href="https://en.wikipedia.org/wiki/Kahan_summation_algorithm">Base algorithm is the Kahan summation algorithm</a>
     * @param input numbers to sum
     * @return sum
     */
    static float compensatedSummation(float[] input) {
        var sum = 0f;
        var c = 0f;// A running compensation for lost low-order bits.
        for (float v : input) {
            var t = sum + v;
            if (Math.abs(sum) >= Math.abs(v)) {
                c += (sum - t) + v;// If sum is bigger, low-order digits of input[i] are lost.
            } else {
                c += (v - t) + sum;// Else low-order digits of sum are lost.
            }
            sum = t;
        }
        return sum + c;
    }

}
