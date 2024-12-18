package com.example.goodsservice.validation;
import com.example.goodsservice.entity.Supplier;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SupplierValidator {

    // Biểu thức chính quy cho số điện thoại hợp lệ (từ 10 đến 15 chữ số)
    private static final String PHONE_REGEX = "^\\+?[0-9]{10,15}$";

    // Biểu thức chính quy cho email hợp lệ
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    // Hàm kiểm tra hợp lệ đối tượng Supplier
    public static List<String> validate(Supplier supplier) {
        List<String> errors = new ArrayList<>();

        // Kiểm tra tên nhà cung cấp
        if (supplier.getSupplierName() == null || supplier.getSupplierName().isEmpty()) {
            errors.add("Tên nhà cung cấp không được để trống.");
        } else if (supplier.getSupplierName().length() < 3 || supplier.getSupplierName().length() > 100) {
            errors.add("Tên nhà cung cấp phải có độ dài từ 3 đến 100 ký tự.");
        }

        // Kiểm tra số điện thoại
        if (supplier.getPhoneNumber() == null || supplier.getPhoneNumber().isEmpty()) {
            errors.add("Số điện thoại không được để trống.");
        } else if (!Pattern.matches(PHONE_REGEX, supplier.getPhoneNumber())) {
            errors.add("Số điện thoại không hợp lệ. Nó phải bao gồm từ 10 đến 15 chữ số.");
        }

        // Kiểm tra email
        if (supplier.getEmail() == null || supplier.getEmail().isEmpty()) {
            errors.add("Email không được để trống.");
        } else if (!Pattern.matches(EMAIL_REGEX, supplier.getEmail())) {
            errors.add("Email không hợp lệ.");
        }

        // Kiểm tra địa chỉ
        if (supplier.getAddress() != null && supplier.getAddress().length() > 255) {
            errors.add("Địa chỉ không được vượt quá 255 ký tự.");
        }

        // Kiểm tra ghi chú
        if (supplier.getNote() != null && supplier.getNote().length() > 500) {
            errors.add("Ghi chú không được vượt quá 500 ký tự.");
        }

        return errors;
    }
}
