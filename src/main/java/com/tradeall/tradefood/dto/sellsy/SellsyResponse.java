package com.tradeall.tradefood.dto.sellsy;

import java.util.List;

/**
 * @author Perrine Honoré
 * @date 2025-12-29
 */
public class SellsyResponse<T> {
    private List<T> data;
    private SellsyPagination pagination;

    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }
    public SellsyPagination getPagination() { return pagination; }
    public void setPagination(SellsyPagination pagination) { this.pagination = pagination; }

    public static class SellsyPagination {
        private Integer total;
        private Integer limit;
        private Integer offset;

        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }
        public Integer getLimit() { return limit; }
        public void setLimit(Integer limit) { this.limit = limit; }
        public Integer getOffset() { return offset; }
        public void setOffset(Integer offset) { this.offset = offset; }
    }
}
