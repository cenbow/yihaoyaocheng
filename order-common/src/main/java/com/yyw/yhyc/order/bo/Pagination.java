package com.yyw.yhyc.order.bo;

/**
 * Created by shiyongxi on 2016/7/27.
 */

import java.io.Serializable;
import java.util.List;

public class Pagination<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_PAGE_SIZE = 15;
    private int total;
    private int totalPage;
    private int pageNo;
    private int pageSize = 15;
    private boolean paginationFlag = false;
    private List<T> resultList;

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
        this.totalPage = (int)Math.ceil((double)this.total / (double)this.pageSize);
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isPaginationFlag() {
        return this.paginationFlag;
    }

    public void setPaginationFlag(boolean paginationFlag) {
        this.paginationFlag = paginationFlag;
    }

    public List<T> getResultList() {
        return this.resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String toString() {
        return "Pagination [total=" + this.total + ", totalPage=" + this.totalPage + ", pageNo=" + this.pageNo + ", pageSize=" + this.pageSize + ", paginationFlag=" + this.paginationFlag + ", resultList=" + this.resultList + "]";
    }
}
