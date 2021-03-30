/*
 * MIT License
 *
 * Copyright (c) 2021 codepenguin.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.codepenguin.example.springboot.openfeign.restcountries.service;

import com.google.gson.reflect.TypeToken;
import org.codepenguin.example.springboot.openfeign.restcountries.client.RestCountriesClient;
import org.codepenguin.example.springboot.openfeign.restcountries.domain.RestCountry;
import org.codepenguin.example.springboot.openfeign.restcountries.domain.SimpleRestCountry;
import org.codepenguin.example.springboot.openfeign.restcountries.repository.RestCountryRepository;
import org.codepenguin.example.springboot.openfeign.restcountries.repository.SimpleRestCountryRepository;
import org.codepenguin.util.JsonUtilException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.codepenguin.example.springboot.openfeign.restcountries.ResourcePathHelper.MOCK_REST_COUNTRY_REPOSITORY_FIND_BY_ID_RESPONSE;
import static org.codepenguin.example.springboot.openfeign.restcountries.ResourcePathHelper.MOCK_SIMPLE_REST_COUNTRY_REPOSITORY_FIND_ALL_RESPONSE;
import static org.codepenguin.util.JsonUtil.fromResourceContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * Unit tests for {@link RestCountriesService}.
 *
 * @author Jorge Garcia
 * @version 0.0.1
 * @since 11
 */
class RestCountriesServiceTest {

    private final TypeToken<List<SimpleRestCountry>> listSimpleRestCountryTypeToken = new TypeToken<>() {
    };

    @Mock
    private RestCountriesClient restCountriesClientMock;

    @Mock
    private SimpleRestCountryRepository simpleRestCountryRepositoryMock;

    @Mock
    private RestCountryRepository restCountryRepositoryMock;

    private RestCountriesService service;


    @BeforeEach
    void setUp() {
        openMocks(this);

        service = new RestCountriesService(restCountriesClientMock, simpleRestCountryRepositoryMock,
                restCountryRepositoryMock);
    }

    @Test
    void givenRedisNoEmptyResponseWhenGetAllCountriesThenReturnThatResponse() throws JsonUtilException {
        final var simpleRestCountries = fromResourceContent(
                MOCK_SIMPLE_REST_COUNTRY_REPOSITORY_FIND_ALL_RESPONSE, listSimpleRestCountryTypeToken, getClass());

        when(simpleRestCountryRepositoryMock.findAll()).thenReturn(simpleRestCountries);

        final var result = service.getAllCountries();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(simpleRestCountries, result);

        verify(simpleRestCountryRepositoryMock, times(INTEGER_ONE)).findAll();
        verify(restCountriesClientMock, times(INTEGER_ZERO)).getAllCountries();
    }

    @Test
    void givenEmptyRedisResponseWhenGetAllCountriesThenReturnRestClientResponse() throws JsonUtilException {
        final var simpleRestCountries = fromResourceContent(
                MOCK_SIMPLE_REST_COUNTRY_REPOSITORY_FIND_ALL_RESPONSE, listSimpleRestCountryTypeToken, getClass());

        when(simpleRestCountryRepositoryMock.findAll()).thenReturn(List.of());
        when(restCountriesClientMock.getAllCountries()).thenReturn(simpleRestCountries);

        final var result = service.getAllCountries();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(simpleRestCountries, result);

        verify(simpleRestCountryRepositoryMock, times(INTEGER_ONE)).findAll();
        verify(restCountriesClientMock, times(INTEGER_ONE)).getAllCountries();
    }

    @Test
    void givenPresentRedisResponseWhenGetCountryByAlphaCodeThenReturnThatResponse() throws JsonUtilException {
        final var restCountry = fromResourceContent(MOCK_REST_COUNTRY_REPOSITORY_FIND_BY_ID_RESPONSE,
                RestCountry.class, getClass());

        when(restCountryRepositoryMock.findById(anyString())).thenReturn(Optional.of(restCountry));

        final var result = service.getCountryByAlphaCode("CO");
        assertNotNull(result);
        assertEquals(restCountry, result);

        verify(restCountryRepositoryMock, times(INTEGER_ONE)).findById(anyString());
        verify(restCountriesClientMock, times(INTEGER_ZERO)).getCountryByAlphaCode(anyString());
    }

    @Test
    void givenNoPresentRedisResponseWhenGetCountryByAlphaCodeThenRestClientResponse() throws JsonUtilException {
        final var restCountry = fromResourceContent(MOCK_REST_COUNTRY_REPOSITORY_FIND_BY_ID_RESPONSE,
                RestCountry.class, getClass());

        when(restCountryRepositoryMock.findById(anyString())).thenReturn(Optional.empty());
        when(restCountriesClientMock.getCountryByAlphaCode(anyString())).thenReturn(restCountry);

        final var result = service.getCountryByAlphaCode("CO");
        assertNotNull(result);
        assertEquals(restCountry, result);

        verify(restCountryRepositoryMock, times(INTEGER_ONE)).findById(anyString());
        verify(restCountriesClientMock, times(INTEGER_ONE)).getCountryByAlphaCode(anyString());
    }
}