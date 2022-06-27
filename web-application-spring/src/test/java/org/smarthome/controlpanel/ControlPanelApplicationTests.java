package org.smarthome.controlpanel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smarthome.controlpanel.entities.ProfileInfo;
import org.smarthome.controlpanel.entities.User;
import org.smarthome.controlpanel.repositories.UsersRepository;
import org.smarthome.controlpanel.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*@RunWith(SpringRunner.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)*/
@SpringBootTest
@ActiveProfiles("test")
class ControlPanelApplicationTests {

/*    @Configuration
    public static class TestingDataSourceConfig {

        @Bean
        @Primary
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .generateUniqueName(true)
                    .setType(EmbeddedDatabaseType.H2)
                    .setScriptEncoding("UTF-8")
                    .ignoreFailedDrops(true)
                    .addScript("smart-home.sql")
                    .build();
        }
    }*/

/*    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private UserAuthService userAuthService;*/

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserAuthService userAuthService;


    @Test
    public void shouldSaveUser() {
        User user = new User(null, "test user", "secret password", "role", new ProfileInfo(), new HashSet<>());
        User savedUser = usersRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("userId").isEqualTo(user);
    }

/*
    @Test
    public void loadContext(){}

    @Test
    public void loadAndValidateExistingOwnerUserDetails() {

*/
/*        User owner = new User(1, "owner", "pwd", "owner", new ProfileInfo(), new HashSet<>());
        Mockito.when(usersRepository.findByLogin(owner.getLogin())).thenReturn(Optional.of(owner));*//*


        var found = userAuthService.loadUserByUsername("owner");
        assertThat(found.getAuthorities().size()).isEqualTo(1);
        assertThat(found.getAuthorities().contains(new SimpleGrantedAuthority("owner"))).isEqualTo(true);
    }
*/

}
