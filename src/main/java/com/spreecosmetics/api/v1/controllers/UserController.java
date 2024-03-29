package com.spreecosmetics.api.v1.controllers;

import com.spreecosmetics.api.v1.dtos.SignUpDTO;
import com.spreecosmetics.api.v1.exceptions.BadRequestException;
import com.spreecosmetics.api.v1.fileHandling.File;
import com.spreecosmetics.api.v1.fileHandling.FileStorageService;
import com.spreecosmetics.api.v1.models.Role;
import com.spreecosmetics.api.v1.models.User;
import com.spreecosmetics.api.v1.payload.ApiResponse;
import com.spreecosmetics.api.v1.repositories.ICartRepository;
import com.spreecosmetics.api.v1.repositories.IRoleRepository;
import com.spreecosmetics.api.v1.repositories.IUserRepository;
import com.spreecosmetics.api.v1.security.JwtTokenProvider;
import com.spreecosmetics.api.v1.services.IFileService;
import com.spreecosmetics.api.v1.services.IUserService;
import com.spreecosmetics.api.v1.utils.Constants;
import com.spreecosmetics.api.v1.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private static final ModelMapper modelMapper = new ModelMapper();
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileStorageService fileStorageService;
    private final IFileService fileService;

    @Value("${uploads.directory.user_profiles}")
    private String directory;


    @GetMapping(path = "/current-user")
    public ResponseEntity<ApiResponse> currentlyLoggedInUser() {
        return ResponseEntity.ok(new ApiResponse(true, userService.getLoggedInUser()));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.getAll();
    }

    @GetMapping(path = "/paginated")
    public Page<User> getAllUsers(@RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
                                  @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return userService.getAll(pageable);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(this.userService.getById(id));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid SignUpDTO dto) {

        User user = new User();

        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        Role role = roleRepository.findByName(dto.getRole()).orElseThrow(
                () -> new BadRequestException("User Role not set"));

        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setMobile(dto.getMobile());
        user.setPassword(encodedPassword);
        user.setRoles(Collections.singleton(role));

        User entity = this.userService.create(user);
        return ResponseEntity.ok(new ApiResponse(true, entity));
    }

    @PutMapping(path = "/upload-profile")
    public ResponseEntity<ApiResponse> uploadProfileImage(
            @RequestParam("file") MultipartFile document
    ) {
        UUID id = this.userService.getLoggedInUser().getId();
        File file = this.fileService.create(document, directory);

        User updated = this.userService.changeProfileImage(id, file);
        System.out.println("Before closing");
        return ResponseEntity.ok(new ApiResponse(true, "File saved successfully", updated));
    }

    @GetMapping("/load-file/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> loadProfileImage(@PathVariable String filename) {

        Resource file = this.fileStorageService.load(directory, filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<ApiResponse> removeProfile() throws Exception {
        User user = this.userService.deleteProfile();
        return ResponseEntity.ok().body(new ApiResponse(true, "Profile image updated successfully", user));
    }


    @PutMapping("/initiate-account-verification")
    public ResponseEntity<ApiResponse> initiateAccountVerification() {
        return ResponseEntity.ok().body(new ApiResponse(true, this.userService.initiateAccountVerification()));
    }

    @PutMapping("/verify-account")
    public ResponseEntity<ApiResponse> verifyAccount(@RequestBody @Valid String verificationToken) throws Exception {
        return ResponseEntity.ok().body(new ApiResponse(true, this.userService.verifyAccount(verificationToken)));
    }

    private User convertDTO(SignUpDTO dto) {
        return modelMapper.map(dto, User.class);
    }
}