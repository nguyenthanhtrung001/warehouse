package com.example.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "print_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class) // Sử dụng converter
    private LocalDateTime printDate;

    @Column(name = "payment_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime paymentDate;

    private Long price;
    private Long employeeId;
    private Integer status;

    @JsonIgnore
    @ManyToOne
    private Customer customer;


    private String contactInfo;

    @JsonIgnore
    @OneToMany(mappedBy = "invoice")
    private List<ReturnNote> returnNotes;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    private String note;

    // Constructors
    public Invoice() {
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Invoice(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPrintDate() {
        return printDate;
    }

    public void setPrintDate(LocalDateTime printDate) {
        this.printDate = printDate;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<ReturnNote> getReturnNotes() {
        return returnNotes;
    }

    public void setReturnNotes(List<ReturnNote> returnNotes) {
        this.returnNotes = returnNotes;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
