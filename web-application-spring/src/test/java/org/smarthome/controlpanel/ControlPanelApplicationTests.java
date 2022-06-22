package org.smarthome.controlpanel;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smarthome.controlpanel.entities.ProfileInfo;
import org.smarthome.controlpanel.entities.User;
import org.smarthome.controlpanel.repositories.UsersRepository;
import org.smarthome.controlpanel.services.UserAuthService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class ControlPanelApplicationTests {

    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private UserAuthService userAuthService;
    @Test
    public void loadContext(){}

    @Test
    public void loadAndValidateExistingOwnerUserDetails() {

        User owner = new User(1, "owner", "pwd", "owner", new ProfileInfo(), new HashSet<>());
        Mockito.when(usersRepository.findByLogin(owner.getLogin())).thenReturn(Optional.of(owner));

        var found = userAuthService.loadUserByUsername("owner");
        assertThat(found.getAuthorities().size()).isEqualTo(1);
        assertThat(found.getAuthorities().contains(new SimpleGrantedAuthority("owner"))).isEqualTo(true);
    }

}
