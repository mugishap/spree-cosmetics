package com.spreecosmetics.api.v1.serviceImpls;

import com.spreecosmetics.api.v1.enums.ERole;
import com.spreecosmetics.api.v1.enums.EUserStatus;
import com.spreecosmetics.api.v1.exceptions.BadRequestException;
import com.spreecosmetics.api.v1.exceptions.ResourceNotFoundException;
import com.spreecosmetics.api.v1.fileHandling.File;
import com.spreecosmetics.api.v1.models.Cart;
import com.spreecosmetics.api.v1.models.User;
import com.spreecosmetics.api.v1.repositories.ICartRepository;
import com.spreecosmetics.api.v1.repositories.IFileRepository;
import com.spreecosmetics.api.v1.repositories.IUserRepository;
import com.spreecosmetics.api.v1.services.IUserService;
import com.spreecosmetics.api.v1.services.MailService;
import com.spreecosmetics.api.v1.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IFileRepository fileRepository;
    private final MailService mailService;
    private final ICartRepository cartRepository;

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    public User getById(UUID id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString()));
    }

    @Override
    public User create(User user) {
        Optional<User> userOptional = this.userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent())
            throw new BadRequestException(String.format("User with email '%s' already exists", user.getEmail()));
        User entity = this.userRepository.save(user);
        Cart cart = new Cart(entity);
        this.cartRepository.save(cart);
        return entity;
    }

    @Override
    public User update(UUID id, User user) {
        User entity = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString()));

        Optional<User> userOptional = this.userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent() && (userOptional.get().getId() != entity.getId()))
            throw new BadRequestException(String.format("User with email '%s' already exists", entity.getEmail()));

        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setMobile(user.getMobile());
        entity.setGender(user.getGender());


        return this.userRepository.save(entity);
    }

    @Override
    public boolean delete(UUID id) {
        this.userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", id));

        this.userRepository.deleteById(id);
        return true;
    }

    @Override
    public List<User> getAllByRole(ERole role) {
        return this.userRepository.findByRoles(role);
    }

    @Override
    public Page<User> getAllByRole(Pageable pageable, ERole role) {
        return this.userRepository.findByRoles(pageable, role);
    }

    @Override
    public List<User> searchUser(String searchKey) {
        return this.userRepository.searchUser(searchKey);
    }

    @Override
    public Page<User> searchUser(Pageable pageable, String searchKey) {
        return this.userRepository.searchUser(pageable, searchKey);
    }

    @Override
    public User getLoggedInUser() {
        String email;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", email));
    }

    @Override
    public User getByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", email));
    }


    @Override
    public User changeStatus(UUID id, EUserStatus status) {
        User entity = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString()));

        entity.setStatus(status);

        return this.userRepository.save(entity);
    }

    @Override
    public User changeProfileImage(UUID id, File file) {
        User entity = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Document", "id", id.toString()));

        entity.setProfileImage(file);
        return this.userRepository.save(entity);

    }

    @Override
    public User deleteProfile() throws Exception {
        User user = this.getLoggedInUser();
        File file = user.getProfileImage();
        if (file.equals(null)) return null;
        this.fileRepository.deleteById(file.getId());
        user.setProfileImage(null);
        this.userRepository.save(user);
        return user;
    }

    @Override
    public String initiateAccountVerification() {
        User user = this.getLoggedInUser();
        String activationCode = Utility.randomUUID(6, 1, 'N');
        user.setActivationCode(activationCode);
        this.userRepository.save(user);
        mailService.sendAccountVerificationMail(user.getEmail(), user.getFirstName() + user.getLastName(), activationCode);
        return "Email verification email sent successfully";
    }

    @Override
    public String verifyAccount(String token) throws Exception {
        User user = this.userRepository.findUserByActivationCode(token)
                .orElseThrow(() -> new Exception("User with verification token " + token + " was not found"));
        user.setActivationCode(null);
        user.setVerified(true);
        this.userRepository.save(user);
        return "Account verified successfully";
    }
}
