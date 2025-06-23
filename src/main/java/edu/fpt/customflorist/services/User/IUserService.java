package edu.fpt.customflorist.services.User;

import edu.fpt.customflorist.dtos.User.UpdateRoleAccountDTO;
import edu.fpt.customflorist.dtos.User.UpdateUserDTO;
import edu.fpt.customflorist.dtos.User.UserDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.exceptions.InvalidPasswordException;
import edu.fpt.customflorist.models.Enums.Role;
import edu.fpt.customflorist.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String email, String password) throws Exception;
    void requestResetPassword(String email) throws Exception;
    void confirmResetPassword(String token, String newPassword) throws Exception;
    void verifyAccount(String verificationCode) throws Exception;
    String generateTokenForGoogleAccount(OAuth2AuthenticatedPrincipal principal) throws Exception;

    User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws Exception;

    User getUserById(Long userId) throws DataNotFoundException;
    User getUserDetailsFromToken(String token) throws Exception;
    Page<User> findAll(String keyword, String role, String accountStatus, String gender, Pageable pageable) throws Exception;

    void resetPassword(Long userId, String oldPassword, String newPassword)
            throws InvalidPasswordException, DataNotFoundException;
    void blockOrEnable(Long userId, Boolean active) throws DataNotFoundException;
    void UpdateRoleAccount(Long userId, UpdateRoleAccountDTO role) throws DataNotFoundException;
}
