package config;

import org.epam.config.PersistenceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@SpringJUnitConfig(classes = {PersistenceConfig.class})
@TestPropertySource("classpath:application.properties")
public class PersistenceConfigTest {

    @Autowired
    private Environment environment;

    @Test
    public void testDataSourceBeanExists() {
        assertNotNull(environment.getProperty("jdbc.driverClassName"));
        assertNotNull(environment.getProperty("jdbc.url"));
        assertNotNull(environment.getProperty("jdbc.username"));
        assertNotNull(environment.getProperty("jdbc.password"));
    }
}

