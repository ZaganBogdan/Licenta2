package com.example.authService.services;

import com.example.authService.entities.PasswordRecovery;
import com.example.authService.exceptions.InvalidTokenException;
import com.example.authService.repositories.PasswordRecoveryRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordRecoveryService {

    private final PasswordRecoveryRepository passwordRecoveryRepository;

    public PasswordRecoveryService( final PasswordRecoveryRepository passwordRecoveryRepository) {
        this.passwordRecoveryRepository = passwordRecoveryRepository;
    }

    public PasswordRecovery addPasswordRecovery(final String email) {
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        String uuid = UUID.randomUUID().toString();
        passwordRecovery.setEmail(email);
        passwordRecovery.setUuid(uuid);
        passwordRecoveryRepository.save(passwordRecovery);

        return passwordRecovery;
    }
    public PasswordRecovery getPasswordRecovery(final String token){
        return passwordRecoveryRepository.findByUuid(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token to reset your password"));
    }
    public void removePasswordRecovery(final PasswordRecovery passwordRecovery){
        passwordRecoveryRepository.delete(passwordRecovery);
    }
}
