package com.yyw.yhyc.order.bo;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by shiyongxi on 2016/7/27.
 */
public class RequestMapModel<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, T> map;

    public void setMap(Map<String, T> map) {
        this.map = map;
    }

    public Map<String, T> getMap() {
        return map;
    }
}
