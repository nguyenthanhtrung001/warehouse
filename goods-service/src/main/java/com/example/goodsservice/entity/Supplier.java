package com.example.goodsservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tên nhà cung cấp không được để trống")
    @Size(min = 3, max = 100, message = "Tên nhà cung cấp phải từ 3 đến 100 ký tự")
    private String supplierName;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Số điện thoại phải hợp lệ và có độ dài từ 10 đến 15 chữ số")
    @Column(unique = true, nullable = false, length = 15)  // Đảm bảo số điện thoại duy nhất và không null
    private String phoneNumber;

    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    @Email(message = "Email phải hợp lệ")
    @Column(unique = true, nullable = true)  // Đảm bảo email duy nhất và không null
    private String email;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private String note;

    @JsonIgnore
    @OneToMany(mappedBy = "supplier")
    private List<Receipt> receipts;

    // Constructors, getters, setters

    public Supplier() {
    }

    public Supplier(Long id) {
        this.id = id;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
