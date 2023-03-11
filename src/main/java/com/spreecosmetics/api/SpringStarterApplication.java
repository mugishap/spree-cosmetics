package com.spreecosmetics.api;

import com.spreecosmetics.api.v1.enums.ERole;
import com.spreecosmetics.api.v1.models.Role;
import com.spreecosmetics.api.v1.repositories.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@SpringBootApplication
public class SpringStarterApplication {

    private final IRoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringStarterApplication.class, args);
    }

    @Bean
    public void registerRoles(){
        Set<ERole> roles = new HashSet<>();
        roles.add(ERole.ADMIN);
        roles.add(ERole.CUSTOMER);
        roles.add(ERole.FRONT_DESK);
        roles.add(ERole.SUPPLIER);

        for (ERole role: roles){
            Optional<Role> roleByName = roleRepository.findByName(role);
            if(!roleByName.isPresent()){
                Role newRole = new Role(role,role.toString());
                roleRepository.save(newRole);
                System.out.println("Created: "+role.toString());
            }
        }
    }
}
