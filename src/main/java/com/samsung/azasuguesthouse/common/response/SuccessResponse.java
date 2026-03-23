package com.samsung.azasuguesthouse.common.response;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public class SuccessResponse {
    private final int status;
    private final Map<String, Object> data;

    public SuccessResponse() {
        this.status = HttpServletResponse.SC_OK;
        this.data = new HashMap<>();
        this.data.put("msg", "SUCCESS");
    }

    public SuccessResponse(String msg) {
        this.status = HttpServletResponse.SC_OK;
        this.data = new HashMap<>();
        this.data.put("msg", msg);
    }

    public SuccessResponse(Map<String, Object> data) {
        this.status = HttpServletResponse.SC_OK;
        this.data = new HashMap<>(data);
        this.data.put("msg", "SUCCESS");
    }

    public SuccessResponse(Map<String, Object> data, String msg) {
        this.status = HttpServletResponse.SC_OK;
        this.data = new HashMap<>(data);
        this.data.put("msg", msg);
    }

    public int getStatus() {
        return this.status;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public void putData(String k, Object v) {
        this.data.put(k, v);
    }

    public void removeData(String k) {
        this.data.remove(k);
    }
}
