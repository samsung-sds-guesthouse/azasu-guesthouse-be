package com.samsung.azasuguesthouse.common.response;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public class ExceptionResponse {
    private final int status;
    private final Map<String, Object> data;

    public ExceptionResponse() {
        this.status = HttpServletResponse.SC_BAD_REQUEST;
        this.data = new HashMap<>();
        data.put("msg", "FAIL");
    }

    public ExceptionResponse(int status, String msg) {
        if (status < 100 || status >= 600) {
            status = HttpServletResponse.SC_BAD_REQUEST;
        }
        this.status = status;
        this.data = new HashMap<>();
        data.put("msg", msg);
    }

    public int getStatus() {
        return this.status;
    }

    public Map<String, Object> getData() {
        return this.data;
    }
}
