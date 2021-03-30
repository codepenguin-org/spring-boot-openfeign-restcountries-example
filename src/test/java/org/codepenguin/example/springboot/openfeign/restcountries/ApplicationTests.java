package org.codepenguin.example.springboot.openfeign.restcountries;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class ApplicationTests {

    @Test
    void givenNoParametersWhenMainThenDoNothing() {
        try (var mockStatic = mockStatic(SpringApplication.class)) {
            Application.main(new String[]{});

            mockStatic.verify(times(INTEGER_ONE), () -> SpringApplication.run(eq(Application.class), any()));
        }
    }
}
