package repository.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dbConnection.DBConnection;
import dto.response.AdminUserResponse;
import entity.Role;
import entity.User;

public class UserRepository {
	Connection connection = null;
	PreparedStatement pst = null;

	// lấy ra người dùng bằng số điện thoại
	public User getUserByPhone(String phone) {
		connection = DBConnection.getConection();
		try {
			String sql = "SELECT * FROM user WHERE phone = ? ";
			pst = connection.prepareStatement(sql);
			pst.setString(1, phone);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setEmail(rs.getString(2));
				user.setPhone(rs.getString(3));
				user.setAddress(rs.getString(4));
				user.setName(rs.getString(6));
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	// thay đổi mật khẩu người dùng
	public void changePassword(User user, String password) {
		connection = DBConnection.getConection();
		try {
			// Tắt auto-commit để thực hiện giao dịch thủ công
			connection.setAutoCommit(false);
			String sql = "UPDATE user SET password = ? WHERE id = ?";
			pst = connection.prepareStatement(sql);
			pst.setString(1, password);
			pst.setLong(2, user.getId());
			pst.executeUpdate();

			// Commit nếu mọi thứ đều thành công
			connection.commit();

		} catch (Exception e) {
			// Rollback giao dịch nếu có lỗi xảy ra
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (Exception rollbackEx) {
				rollbackEx.printStackTrace();
			}

			// In ra lỗi ban đầu
			e.printStackTrace();

		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.setAutoCommit(true); // Bật lại auto-commit sau giao dịch
					DBConnection.closeConnection(connection);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// thay đổi thông tin người dùng
	public void changeInformationUser(Long userId, String name, String address) {
		connection = DBConnection.getConection();
		try {
			// Tắt auto-commit để thực hiện giao dịch thủ công
			connection.setAutoCommit(false);

			String sql = "UPDATE user SET name = ?, address = ? WHERE id = ?";
			pst = connection.prepareStatement(sql);
			pst.setString(1, name);
			pst.setString(2, address);
			pst.setLong(3, userId);

			pst.executeUpdate();

			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// thay đổi email người dùng
	public void changeEmailUser(User user, String email) {
		connection = DBConnection.getConection();
		try {
			// Tắt auto-commit để thực hiện giao dịch thủ công
			connection.setAutoCommit(false);

			String sql = "UPDATE user SET email = ? WHERE id = ?";
			pst = connection.prepareStatement(sql);
			pst.setString(1, email);
			pst.setLong(2, user.getId());

			pst.executeUpdate();

			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.setAutoCommit(true); // Bật lại auto-commit sau giao dịch
					DBConnection.closeConnection(connection);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// kiểm tra tài khoản người dùng tồn tại chưa
	public User isAccountExist(String email, String phone) {
		connection = DBConnection.getConection();
		String sql = "SELECT * FROM user WHERE email = ? OR phone = ? ";
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, email);
			pst.setString(2, phone);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setEmail(rs.getString(2));
				user.setPhone(rs.getString(3));
				user.setAddress(rs.getString(4));
				user.setName(rs.getString(6));
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	// Lấy ra email của người dùng
	public List<String> getEmailUser() {
		connection = DBConnection.getConection();
		List<String> lst = new ArrayList<>();

		try {
			String sql = "SELECT email FROM user";
			pst = connection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();

			// Sử dụng vòng lặp while để lấy tất cả email
			while (rs.next()) {
				String email = rs.getString("email"); // Sử dụng getString và lấy theo tên cột
				lst.add(email); // Thêm email vào danh sách
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return lst; // Trả về danh sách email
	}

	// Kiểm tra mail tồn tại hay chưa
	public boolean checkUserByEmail(String email) {
		return getEmailUser().contains(email);
	}

	public User getUserByEmail(String email) {
		connection = DBConnection.getConection();
		try {
			String sql = "SELECT * FROM user WHERE email = ? ";
			pst = connection.prepareStatement(sql);
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setEmail(rs.getString(2));
				user.setPhone(rs.getString(3));
				user.setAddress(rs.getString(4));
				user.setName(rs.getString(6));
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public void addUserByEmail(String email, String name) {
		// Kết nối cơ sở dữ liệu
		connection = DBConnection.getConection();
		try {
			// SQL query để thêm người dùng mà không cần chỉ định user_id
			String sql = "INSERT INTO user (email, name) VALUES (?, ?)";

			// Chuẩn bị câu lệnh SQL
			pst = connection.prepareStatement(sql);

			// Gán giá trị cho các tham số trong câu lệnh SQL
			pst.setString(1, email); // Gán email vào tham số đầu tiên
			pst.setString(2, name); // Gán tên vào tham số thứ hai

			// Thực thi câu lệnh cập nhật cơ sở dữ liệu
			int rowsAffected = pst.executeUpdate(); // executeUpdate dùng cho INSERT, UPDATE, DELETE

			// Kiểm tra xem có ảnh hưởng đến cơ sở dữ liệu hay không
			if (rowsAffected > 0) {
				System.out.println("User added successfully.");
			} else {
				System.out.println("Failed to add user.");
			}
		} catch (Exception e) {
			// In thông báo lỗi nếu có lỗi
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean checkUserByPhone(String phone) {
		return getPhoneUser().contains(phone);
	}

	// Lấy ra sdt của ngừi dùng
	public List<String> getPhoneUser() {
		connection = DBConnection.getConection();
		List<String> lst = new ArrayList<>();

		try {
			String sql = "SELECT phone FROM user";
			pst = connection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();

			// Sử dụng vòng lặp while để lấy tất cả phone
			while (rs.next()) {
				String phone = rs.getString("phone"); // Sử dụng getString và lấy theo tên cột
				lst.add(phone); // Thêm phone vào danh sách
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return lst; // Trả về danh sách email
	}

	// Thêm sdt user vào database
	public void addUserByPhone(String phone) {
		// Kết nối cơ sở dữ liệu
		connection = DBConnection.getConection();
		try {
			// SQL query để thêm số điện thoại user
			String sql = "INSERT INTO user (phone) VALUES (?)";

			// Chuẩn bị câu lệnh SQL
			pst = connection.prepareStatement(sql);

			// Gán giá trị cho các tham số trong câu lệnh SQL
			pst.setString(1, phone); // Thêm số điện thoại vào cơ sở dữ liệu

			// Thực thi câu lệnh cập nhật cơ sở dữ liệu
			int rowsAffected = pst.executeUpdate(); // executeUpdate dùng cho INSERT, UPDATE, DELETE

			// Kiểm tra xem có ảnh hưởng đến cơ sở dữ liệu hay không
			if (rowsAffected > 0) {
				System.out.println("User added successfully.");
			} else {
				System.out.println("Failed to add user.");
			}
		} catch (Exception e) {
			// In thông báo lỗi nếu có lỗi
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// thay đổi thông tin người dùng qua số điện thoại
	public void changeInformationUserByPhone(String name, String email, String phone, String address) {
		connection = DBConnection.getConection();
		try {
			// Tắt auto-commit để thực hiện giao dịch thủ công
			connection.setAutoCommit(false);

			String sql = "UPDATE user SET name = ?, address = ?, email = ? WHERE phone = ?";

			pst = connection.prepareStatement(sql);
			pst.setString(1, name);
			pst.setString(2, address);
			pst.setString(3, email);
			pst.setString(4, phone);

			// Thực thi câu lệnh
			int rowsUpdated = pst.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("Thông tin người dùng đã được cập nhật thành công!");
			} else {
				System.out.println("Không tìm thấy số điện thoại: " + phone);
			}

			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.setAutoCommit(true); // Bật lại auto-commit sau giao dịch
					DBConnection.closeConnection(connection);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// thay đổi thông tin người dùng qua email
	public void changeInformationUserByEmail(String name, String email, String phone, String address) {
		connection = DBConnection.getConection();
		try {
			// Tắt auto-commit để thực hiện giao dịch thủ công
			connection.setAutoCommit(false);

			String sql = "UPDATE user SET name = ?, address = ?, phone = ? WHERE email = ?";

			pst = connection.prepareStatement(sql);
			pst.setString(1, name);
			pst.setString(2, address);
			pst.setString(3, phone);
			pst.setString(4, email);

			// Thực thi câu lệnh
			int rowsUpdated = pst.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("Thông tin người dùng đã được cập nhật thành công!");
			} else {
				System.out.println("Không tìm thấy email: " + email);
			}

			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.setAutoCommit(true); // Bật lại auto-commit sau giao dịch
					DBConnection.closeConnection(connection);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// thay đổi thông tin người dùng qua email
	public void changeInformationUserById(String name, String email, String phone, String address, Long id) {
		connection = DBConnection.getConection();
		try {
			// Tắt auto-commit để thực hiện giao dịch thủ công
			connection.setAutoCommit(false);

			String sql = "UPDATE user SET name = ?, address = ?, phone = ?, email = ? where id = ?";

			pst = connection.prepareStatement(sql);
			pst.setString(1, name);
			pst.setString(2, address);
			pst.setString(3, phone);
			pst.setString(4, email);
			pst.setLong(5, id);

			// Thực thi câu lệnh
			int rowsUpdated = pst.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("Thông tin người dùng đã được cập nhật thành công!");
			} else {
				System.out.println("Không tìm thấy email: " + email);
			}

			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.setAutoCommit(true); // Bật lại auto-commit sau giao dịch
					DBConnection.closeConnection(connection);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// lấy ra người dùng bằng số điện thoại
	public User getUserById(Long id) {
		connection = DBConnection.getConection();
		try {
			String sql = "SELECT * FROM user WHERE id = ? ";
			pst = connection.prepareStatement(sql);
			pst.setLong(1, id);
			;
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setEmail(rs.getString(2));
				user.setPhone(rs.getString(3));
				user.setAddress(rs.getString(4));
				user.setName(rs.getString(6));
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	
	public User loginForAdmin(String phone, String password) {
		connection = DBConnection.getConection();
		try {
			String sql = "SELECT * FROM user WHERE phone = ? and password = ? ";
			pst = connection.prepareStatement(sql);
			pst.setString(1, "84" + phone);
			pst.setString(2, password);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setEmail(rs.getString(2));
				user.setPhone(rs.getString(3));
				user.setAddress(rs.getString(4));
				user.setName(rs.getString(6));
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	// Lấy ra danh sach nhan vien
	public List<AdminUserResponse> getAllEmployee() {
		connection = DBConnection.getConection();
		List<AdminUserResponse> lst = new ArrayList<>();

		try {
			String sql = "select u.id, u.email, u.phone, u.name, u.address, u.create_at, r.name "
					+ " from ecommerce.user u " + " inner join ecommerce.user_role  ur on  u.id = ur.user_id "
					+ " inner join ecommerce.role r on ur.role_id = r.id " + " where r.id = 2";
			pst = connection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();

			// Sử dụng vòng lặp while để lấy tất cả phone
			while (rs.next()) {
				AdminUserResponse aur = new AdminUserResponse(rs.getLong(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				lst.add(aur);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return lst; // Trả về danh sách email
	}

	// Lấy ra danh sach khach hang
	public List<AdminUserResponse> getAllCustomer() {
		connection = DBConnection.getConection();
		List<AdminUserResponse> lst = new ArrayList<>();

		try {
			String sql = "select u.id, u.email, u.phone, u.name, u.address,u.create_at, r.name "
					+ " from ecommerce.user u " + " inner join ecommerce.user_role  ur on  u.id = ur.user_id "
					+ " inner join ecommerce.role r on ur.role_id = r.id " + " where r.id = 3";
			pst = connection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();

			// Sử dụng vòng lặp while để lấy tất cả phone
			while (rs.next()) {
				AdminUserResponse aur = new AdminUserResponse(rs.getLong(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				lst.add(aur);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return lst; // Trả về danh sách email
	}

	// thay đổi thông tin người dùng
	public void setRoleForUser(Long userId) {
		connection = DBConnection.getConection();
		try {
			// Tắt auto-commit để thực hiện giao dịch thủ công
			connection.setAutoCommit(false);

			String sql = "Insert into ecommerce.user_role (user_id, role_id) " + " values(?, 3)";
			pst = connection.prepareStatement(sql);
			pst.setLong(1, userId);

			pst.executeUpdate();

			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean deleteUser(Long userId) {
	    boolean roleDeleted = deleteUserRole(userId);
	    boolean cartDeleted = deleteCart(userId);
	    boolean userDeleted = deleteUserRecord(userId);

	    return roleDeleted && cartDeleted && userDeleted;
	}

	private boolean deleteUserRole(Long userId) {
	    String deleteRoleUser = "DELETE FROM ecommerce.user_role WHERE user_id = ?";
	    try (Connection connection = DBConnection.getConection();
	         PreparedStatement userRolePST = connection.prepareStatement(deleteRoleUser)) {

	        userRolePST.setLong(1, userId);
	        userRolePST.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	private boolean deleteCart(Long userId) {
	    String deleteCart = "DELETE FROM ecommerce.cart WHERE user_id = ?";
	    try (Connection connection = DBConnection.getConection();
	         PreparedStatement cartPST = connection.prepareStatement(deleteCart)) {

	        cartPST.setLong(1, userId);
	        cartPST.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	private boolean deleteUserRecord(Long userId) {
	    String deleteUser = "DELETE FROM ecommerce.user WHERE id = ?";
	    try (Connection connection = DBConnection.getConection();
	         PreparedStatement userPST = connection.prepareStatement(deleteUser)) {

	        userPST.setLong(1, userId);
	        int rowsAffected = userPST.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	
	public AdminUserResponse getUserResponseByEmail(String email) {
		connection = DBConnection.getConection();
		try {
			String sql = "SELECT * FROM ecommerce.user WHERE email = ? ";
			pst = connection.prepareStatement(sql);
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				AdminUserResponse aur = new AdminUserResponse(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4)
						, rs.getString(5), rs.getString(6), rs.getString(7));
				return aur;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public boolean addUser(String email, String phone, String address, String name) {
	    PreparedStatement pstUser = null;

	    try {
	        connection = DBConnection.getConection();

	        // Câu lệnh SQL để thêm user
	        String sqlUser = "INSERT INTO ecommerce.user (email, phone, address, ecommerce.user.name, create_at) "
	                + "VALUES (?, ?, ?, ?, DATE(NOW()))";
	        pstUser = connection.prepareStatement(sqlUser);
	        pstUser.setString(1, email);
	        pstUser.setString(2, phone);
	        pstUser.setString(3, address);
	        pstUser.setString(4, name);
	        int result = pstUser.executeUpdate();
	        
	        return result > 0; // Trả về true nếu thêm thành công
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Nếu có lỗi, trả về false
	    } finally {
	        try {
	            if (pstUser != null) pstUser.close();
	            if (connection != null) DBConnection.closeConnection(connection);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public boolean setRoleUser(Long userId, Long roleId) {
	    PreparedStatement pstRole = null;

	    try {
	        connection = DBConnection.getConection();

	        // Câu lệnh SQL để gán quyền
	        String sqlRole = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
	        pstRole = connection.prepareStatement(sqlRole);
	        pstRole.setLong(1, userId);
	        pstRole.setLong(2, roleId);
	        int result = pstRole.executeUpdate();
	        
	        return result > 0; // Trả về true nếu gán quyền thành công
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Nếu có lỗi, trả về false
	    } finally {
	        try {
	            if (pstRole != null) pstRole.close();
	            if (connection != null) DBConnection.closeConnection(connection);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	
	public List<Role> getListRole(){
		List<Role> list = new ArrayList<Role>();
		connection = DBConnection.getConection();
		try {
			String sql = "SELECT id, name FROM ecommerce.role";
			pst = connection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				list.add(new Role(rs.getInt(1), rs.getString(2)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					DBConnection.closeConnection(connection);
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	
	public static void main(String[] args) {
//		List<Role> lst = new UserRepository().getListRole();
//		for(Role r : lst) {
//			System.out.println(r);
//		}
		
//		System.out.println(new UserRepository().addUser("aw@gmail.com", "84852852852", "đâsdas", "adas"));
//		User user;
//		System.out.println(user = new UserRepository().getUserByEmail("aw@gmail.com"));
//		System.out.println(new UserRepository().setRoleUser(user.getId(),(long) 2));
		
		System.out.println(new UserRepository().deleteUser((long)37));
	}
}