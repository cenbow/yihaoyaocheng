package com.yyw.yhyc.order.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiyongxi on 2016/7/27.
 */
public class RequestListModel<T extends Serializable> {
    private List<T> list;

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }
}
