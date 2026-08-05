package br.pucminas.researchyoutube.util;

import java.math.BigInteger;
import java.time.Duration;

public class Convert {
    public static int toInt(BigInteger bigInteger) {
        if (bigInteger == null) return 0;
        return bigInteger.intValue();
    }

    public static long durationToSeconds(String duration) {
        return Duration.parse(duration).getSeconds();
    }
}
