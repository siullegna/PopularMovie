package com.hap.popularmovie.model.detail;

import com.hap.popularmovie.MovieApplication;

/**
 * Created by luis on 11/18/17.
 */

public class InformationItem {
    private final String header;
    private final String detail;

    public InformationItem(String header, String detail) {
        this.header = header;
        this.detail = detail;
    }

    public InformationItem(int header, String detail) {
        this(MovieApplication.getInstance().getString(header), detail);
    }

    public InformationItem(String header, int detail) {
        this(header, MovieApplication.getInstance().getString(detail));
    }

    public InformationItem(int header, int detail) {
        this(MovieApplication.getInstance().getString(header), MovieApplication.getInstance().getString(detail));
    }

    public String getHeader() {
        return header;
    }

    public String getDetail() {
        return detail;
    }
}
