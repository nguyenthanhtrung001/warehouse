package com.example.orderservice.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "print_date")
//    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class) // Sử dụng converter
    private LocalDateTime printDate;

    private Long price;
    private Long employeeId;
    private Integer status;

    @ManyToOne
    private Customer customer;

    @JsonIgnore
    @OneToMany(mappedBy = "invoice")
    private List<ReturnNote> returnNotes;

    private String note;

    public Invoice(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<ReturnNote> getReturnNotes() {
        return returnNotes;
    }

    public void setReturnNotes(List<ReturnNote> returnNotes) {
        this.returnNotes = returnNotes;
    }


    public Invoice() {
    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

 /*   public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }*/

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}