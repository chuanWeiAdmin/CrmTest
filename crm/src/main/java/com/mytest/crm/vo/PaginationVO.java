package com.mytest.crm.vo;

import java.util.List;

public class PaginationVO<T> {

    private int total;
    private List<T> dataList;  //使用泛型，达到复用的目的

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
