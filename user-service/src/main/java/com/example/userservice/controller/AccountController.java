package com.example.userservice.controller;

import com.example.userservice.entity.Account;
import com.example.userservice.service.IAccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAccount(@RequestBody Account account, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Account createdAccount = accountService.createAccount(account);

            // Thành công
            response.put("result", 1);
            response.put("msg", "Thêm tài khoản thành công");
            response.put("method", request.getMethod());
            response.put("account", createdAccount);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            // Xử lý khi không thành công
            response.put("result", 0);
            response.put("msg", "Thêm tài khoản thất bại");
            response.put("method", request.getMethod());
            response.put("error", ex.getMessage()); // Thêm thông tin lỗi vào response nếu cần

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        boolean deleted = accountService.deleteAccount(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAccount(@RequestBody Account account, @PathVariable Long id, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Boolean updated = accountService.updateAccount(account, id);
        if (updated != null && updated) {
            response.put("result", 1);
            response.put("msg", "Cập nhật tài khoản thành công");
            response.put("method", request.getMethod());
            response.put("account", account);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("result", 0);
            response.put("msg", "Không tìm thấy tài khoản để cập nhật");
            response.put("method", request.getMethod());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("result", 0);
            response.put("msg", "Không tìm thấy tài khoản");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}
