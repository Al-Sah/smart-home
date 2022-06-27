package org.smarthome.controlpanel;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smarthome.controlpanel.entities.ProfileInfo;
import org.smarthome.controlpanel.entities.User;
import org.smarthome.controlpanel.repositories.UsersRepository;
import org.smarthome.controlpanel.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

@SpringBootTest
@ActiveProfiles("test")
class ControlPanelApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserAuthService userAuthService;


    @Test
    public void loadContext(){}

    @Test
    public void createUserWithRoleOwner() {
        User user = new User(null, "owner", passwordEncoder.encode("owner"), "owner", new ProfileInfo(), new HashSet<>());
        User savedUser = usersRepository.save(user);
        AssertionsForClassTypes.assertThat(savedUser).usingRecursiveComparison().ignoringFields("userId").isEqualTo(user);
    }

    @Test
    public void shouldLoadSavedOwner() {
        Assertions.assertTrue(usersRepository.findByLogin("owner").isPresent());
    }

    @Test
    public void loadAndCheckOwnerSecurityDetails(){
        var res = userAuthService.loadUserByUsername("owner");

        Assertions.assertEquals(res.getAuthorities().size(), 1);
        Assertions.assertEquals(res.getAuthorities().toArray()[0], new SimpleGrantedAuthority("owner"));
        Assertions.assertTrue(passwordEncoder.matches( "owner", res.getPassword()));
    }

}
