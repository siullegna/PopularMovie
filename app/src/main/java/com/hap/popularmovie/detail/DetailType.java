package com.hap.popularmovie.detail;

/**
 * Created by luis on 11/18/17.
 */

public enum DetailType {
    INFORMATION,
    TRAILER,
    SEPARATOR,
    HEADER,
    UNKNOWN;

    public static DetailType fromOrdinal(final int ordinal) {
        DetailType detailType = UNKNOWN;

        for (final DetailType currentType : values()) {
            if (currentType.ordinal() == ordinal) {
                detailType = currentType;
                break;
            }
        }

        return detailType;
    }
}
