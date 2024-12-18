package com.example.goodsservice.dto;

public class Batch {
    private Long id;
    private String nameBath;
    private Long warehouseId;

    public Batch() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameBath() {
        return nameBath;
    }

    public void setNameBath(String nameBath) {
        this.nameBath = nameBath;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
