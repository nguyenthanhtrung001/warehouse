package com.example.goodsservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime receiptDate;

    @ManyToOne
    private Supplier supplier;

/*    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;*/

    public List<DeliveryNote> getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(List<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }
    @JsonIgnore
    @OneToMany(mappedBy = "receipt")
    private List<DeliveryNote> deliveryNotes;

    private Integer status;
    private Long purchasePrice;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Receipt() {
    }

    public Receipt(Long id) {
        this.id = id;
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
// Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDateTime receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

/*    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }*/

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
