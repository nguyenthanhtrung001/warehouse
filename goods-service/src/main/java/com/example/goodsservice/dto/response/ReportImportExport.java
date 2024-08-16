package com.example.goodsservice.dto.response;

public class ReportImportExport {
    Long id;
    String nameProduct;
    Integer import_supplier;
    Integer import_check_inventory;
    Integer import_return_order;
    Integer export_order;
    Integer export_cancel;
    Integer export_supplier;
    Integer export_check;
    Integer inventory;

    public ReportImportExport() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Integer getImport_supplier() {
        return import_supplier;
    }

    public void setImport_supplier(Integer import_supplier) {
        this.import_supplier = import_supplier;
    }

    public Integer getImport_check_inventory() {
        return import_check_inventory;
    }

    public void setImport_check_inventory(Integer import_check_inventory) {
        this.import_check_inventory = import_check_inventory;
    }

    public Integer getImport_return_order() {
        return import_return_order;
    }

    public void setImport_return_order(Integer import_return_order) {
        this.import_return_order = import_return_order;
    }

    public Integer getExport_order() {
        return export_order;
    }

    public void setExport_order(Integer export_order) {
        this.export_order = export_order;
    }

    public Integer getExport_cancel() {
        return export_cancel;
    }

    public void setExport_cancel(Integer export_cancel) {
        this.export_cancel = export_cancel;
    }

    public Integer getExport_supplier() {
        return export_supplier;
    }

    public void setExport_supplier(Integer export_supplier) {
        this.export_supplier = export_supplier;
    }

    public Integer getExport_check() {
        return export_check;
    }

    public void setExport_check(Integer export_check) {
        this.export_check = export_check;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }
}
