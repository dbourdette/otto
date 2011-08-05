package org.otto.web.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author damien bourdette
 */
public class SizeInBytes {
    private static final long SCALE = 1024;

    private static final String[] UNITS = {"T", "G", "M", "K", "B"};

    private static final long BIGGEST_SCALE = (long) Math.pow(1024, UNITS.length - 1);

    private long value;

    public SizeInBytes(long value) {
        this.value = value;
    }

    public static SizeInBytes fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("value is not a valid size");
        }

        long multiplier = 1;

        if (value.endsWith("M")) {
            value = StringUtils.substringBeforeLast(value, "M");
            multiplier = SCALE * SCALE;
        } else if (value.endsWith("K")) {
            value = StringUtils.substringBeforeLast(value, "K");
            multiplier = SCALE;
        } else if (value.endsWith("B")) {
            value = StringUtils.substringBeforeLast(value, "B");
        }

        try {
            return new SizeInBytes(Long.parseLong(value) * multiplier);
        } catch (Exception e) {
            throw new IllegalArgumentException("value is not a valid size");
        }
    }

    public String getFormattedValue() {
        String result = "";

        long loopScale = BIGGEST_SCALE;
        long loopValue = value;
        long rest = value;

        for (String unit : UNITS) {
            loopValue = rest / loopScale;

            if (loopValue < 0) {
                loopValue = 0;
            }

            if (loopValue != 0) {
                if (StringUtils.isNotEmpty(result)) {
                    result += " ";
                }

                result += loopValue + unit;

                rest -= loopValue * loopScale;
            }

            loopScale = loopScale / SCALE;
        }

        return result;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getFormattedValue();
    }
}
