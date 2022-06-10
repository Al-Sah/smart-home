package org.smarthome.controlpanel;

import org.smarthome.controlpanel.repositories.CredentialsRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserAuthService implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;

    public UserAuthService(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var smartHomeUser = credentialsRepository.findByLogin(username);

        return new User(
                smartHomeUser.getLogin(),
                smartHomeUser.getPwd(),
                List.of(new SimpleGrantedAuthority(smartHomeUser.getRole()))
        );
    }

}
