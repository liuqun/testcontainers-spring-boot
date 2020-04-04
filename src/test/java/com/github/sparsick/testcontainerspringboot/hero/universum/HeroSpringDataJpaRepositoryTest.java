package com.github.sparsick.testcontainerspringboot.hero.universum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(initializers = HeroSpringDataJpaRepositoryTest.Initializer.class)
@Testcontainers
class HeroSpringDataJpaRepositoryTest {

    @Container
    private static MySQLContainer database = new MySQLContainer();

    @Autowired
    private HeroSpringDataJpaRepository repositoryUnderTest;

    @Test
    void findHerosBySearchCriteria() {
        repositoryUnderTest.save(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));

        Collection<Hero> heros = repositoryUnderTest.findHerosBySearchCriteria("Batman");

        assertThat(heros).hasSize(1).contains(new Hero("Batman", "Gotham City", ComicUniversum.DC_COMICS));
    }

    static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext
                                       configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}