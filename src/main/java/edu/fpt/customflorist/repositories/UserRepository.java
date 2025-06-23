package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<User> findByUserId(Long id);
    Optional<User> findByVerificationCode(String verificationCode);

    @Query("SELECT u FROM User u WHERE " +
            "((:keyword IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:keyword IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:keyword IS NULL OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%')))) " +
            "AND (:role IS NULL OR u.role = CASE WHEN :role = 'ADMIN' THEN 'ADMIN' WHEN :role = 'CUSTOMER' THEN 'CUSTOMER' WHEN :role = 'SHIPPER' THEN 'SHIPPER' WHEN :role = 'MANAGER' THEN 'MANAGER' ELSE u.role END) " +
            "AND (:accountStatus IS NULL OR u.accountStatus = CASE WHEN :accountStatus = 'ACTIVE' THEN 'ACTIVE' WHEN :accountStatus = 'BANNED' THEN 'BANNED' ELSE u.accountStatus END) " +
            "AND (:gender IS NULL OR u.gender = CASE WHEN :gender = 'Male' THEN 'Male' WHEN :gender = 'Female' THEN 'Female' WHEN :gender = 'Other' THEN 'Other' ELSE u.gender END)")
    Page<User> searchUsers(@Param("keyword") String keyword,
                           @Param("role") String role,
                           @Param("accountStatus") String accountStatus,
                           @Param("gender") String gender,
                           Pageable pageable);
}
