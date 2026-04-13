package com.devteria.identity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devteria.identity.constant.PredefinedRole;
import com.devteria.identity.dto.request.UserCreationRequest;
import com.devteria.identity.dto.request.UserUpdateRequest;
import com.devteria.identity.dto.response.UserResponse;
import com.devteria.identity.entity.Role;
import com.devteria.identity.entity.User;
import com.devteria.identity.exception.AppException;
import com.devteria.identity.exception.ErrorCode;
import com.devteria.identity.mapper.ProfileMapper;
import com.devteria.identity.mapper.UserMapper;
import com.devteria.identity.repository.RoleRepository;
import com.devteria.identity.repository.UserRepository;
import com.devteria.identity.repository.httpclient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user = userRepository.save(user);

//        var profileRequest = profileMapper.toProfileCreationRequest(request);
//        profileRequest.setUserId(user.getId());
//
//        profileClient.createProfile(profileRequest);

        return userMapper.toUserResponse(user);
    }

    public UserResponse createEmployee(UserCreationRequest request) {
        // Kiểm tra xem tên người dùng đã tồn tại chưa
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Tạo đối tượng User từ request
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(1);

        // Tạo một tập hợp vai trò cho nhân viên
        HashSet<Role> roles = new HashSet<>();
        if (request.getType() == 1)
        {
            roleRepository.findById(PredefinedRole.STAFF_ROLE).ifPresent(roles::add);
        }
        if (request.getType() == 2)
        {
            roleRepository.findById(PredefinedRole.MANAGER_ROLE).ifPresent(roles::add);
        }

        user.setRoles(roles);

        // Lưu người dùng vào cơ sở dữ liệu
        user = userRepository.save(user);

        // Nếu cần thêm thông tin liên quan đến nhân viên, thực hiện ở đây
        // Ví dụ: tạo profile cho nhân viên
        // var profileRequest = profileMapper.toProfileCreationRequest(request);
        // profileRequest.setUserId(user.getId());
        // profileClient.createProfile(profileRequest);

        return userMapper.toUserResponse(user);
    }
    public UserResponse createEmployeeAuto(Long employeeId) {
        UserCreationRequest request = new UserCreationRequest("NV000"+employeeId,"123456");

        // Kiểm tra xem tên người dùng đã tồn tại chưa
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Tạo đối tượng User từ request
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Tạo một tập hợp vai trò cho nhân viên
        HashSet<Role> roles = new HashSet<>();
        // Thêm vai trò nhân viên vào
        roleRepository.findById(PredefinedRole.STAFF_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        // Lưu người dùng vào cơ sở dữ liệu
        user = userRepository.save(user);

        // Nếu cần thêm thông tin liên quan đến nhân viên, thực hiện ở đây
        // Ví dụ: tạo profile cho nhân viên
        // var profileRequest = profileMapper.toProfileCreationRequest(request);
        // profileRequest.setUserId(user.getId());
        // profileClient.createProfile(profileRequest);

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        String username = user.get().getUsername();
        String numericPart = username.substring(2);

        Long employeeId = Long.parseLong(numericPart);


        profileClient.deleteUser(employeeId);

        userRepository.deleteById(userId);
    }


    public void deleteUserByUsername(String username) {
        // Tìm người dùng theo username
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            // Xóa người dùng khỏi cơ sở dữ liệu
            userRepository.delete(user.get());
            System.out.println("Người dùng với username '" + username + "' đã bị xóa.");
        } else {
            throw new RuntimeException("Không tìm thấy người dùng với username: " + username);
        }
    }


    //    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public List<UserResponse> getUsers() {
//        log.info("In method get Users");
//        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
//    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<UserResponse> getUsers() {
    log.info("In method get Managers");

    // Lọc những người dùng có vai trò là "MANAGER"
    return userRepository.findAll().stream()
            .filter(user -> user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("MANAGER")|| role.getName().equals("STAFF"))) // Kiểm tra nếu người dùng có vai trò "MANAGER"
            .map(userMapper::toUserResponse)  // Chuyển đổi thành UserResponse
            .toList();  // Trả về danh sách người dùng có vai trò "MANAGER"
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<UserResponse> getUsersEmployee(Long warehouseId) {
        log.info("In method getUsers");
        List<String> listUserId = profileClient.listUserId(warehouseId);

        return userRepository.findAll().stream()
                // Lọc ra các user có userId trong listUserId
                .filter(user -> listUserId.contains(user.getUsername()) &&
                        user.getRoles().stream()
                                .noneMatch(role -> role.getName().equals("ADMIN") || role.getName().equals("MANAGER")))
                .map(userMapper::toUserResponse) // Chuyển đổi sang UserResponse
                .toList();
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
    public void resetPassword(String userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)); // Sử dụng mã lỗi cụ thể
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void lockUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatus(0); // Đặt status = 0 để khóa tài khoản
        userRepository.save(user);
    }

    public void unlockUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatus(1); // Đặt status = 1 để mở khóa tài khoản
        userRepository.save(user);
    }

    public void updateUserRoles(String userId, List<String> roleNames) {
        // Tìm User trong cơ sở dữ liệu
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm các Role dựa trên danh sách roleNames
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleNames));
        if (roles.size() != roleNames.size()) {
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }

        // Cập nhật danh sách Role cho User
        user.setRoles(roles);

        // Lưu User đã cập nhật vào cơ sở dữ liệu
        userRepository.save(user);
    }

}
