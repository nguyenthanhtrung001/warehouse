package com.example.orderservice.validator;

import com.example.orderservice.dto.CustomerDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerValidator {

    // Regular expression for valid email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    // Regular expression for valid phone number (10 digits)
    private static final String PHONE_REGEX = "^\\d{10}$";

    // Hàm kiểm tra hợp lệ CustomerDTO và trả về danh sách lỗi
    public static List<String> validate(CustomerDTO customerDTO) {
        List<String> errors = new ArrayList<>();

        // Kiểm tra tên khách hàng
        if (customerDTO.getCustomerName() == null || customerDTO.getCustomerName().isEmpty()) {
            errors.add("Tên khách hàng không được để trống.");
        }

        // Kiểm tra số điện thoại
        if (customerDTO.getPhoneNumber() != null || !customerDTO.getPhoneNumber().isEmpty()) {
            if (!isValidPhoneNumber(customerDTO.getPhoneNumber())) {
                errors.add("Số điện thoại không hợp lệ. Nó phải gồm 10 chữ số.");
            }
        }

        // Kiểm tra email
//        if (customerDTO.getEmail() != null || !customerDTO.getEmail().isEmpty()) {
//            if (!isValidEmail(customerDTO.getEmail())) {
//                errors.add("Email không hợp lệ.");
//            }
//        }

        return errors;
    }

    // Kiểm tra số điện thoại hợp lệ
    private static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_REGEX, phoneNumber);
    }

    // Kiểm tra email hợp lệ
    private static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }
}
