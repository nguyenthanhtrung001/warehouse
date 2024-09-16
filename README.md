Hệ thống BackEnd cho Warehouse

![image](https://github.com/user-attachments/assets/4ccd7299-9a00-4367-b965-3441f80c11d5)

- Cấu hình thiết lập 6 services
	+ Employee – service : Phục vụ quản lý nhân viên, chấm công, tính lương và lịch làm việc.
	+ Goods – service: Quản lý quá trình nhập xuất hàng hóa trong kho hàng.
	+ Inventory – service: Quản lý kho, vị trí hàng hóa, lô hàng.
	+ Order – service: Quản lý mua hàng, trả hàng của khách hàng.
	+ Product – service: Quản lý sản phẩm
	+ Indentity- service: Tài khoản, quyền hạn truy cập hệ thống.
- Cấu hình api-gateway: Định tuyến các yêu cầu từ client, chặn truy cập, xác thực (authentication) và ủy quyền (authorization).
