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

package org.codepenguin.example.springboot.openfeign.restcountries.controller;

import com.google.gson.reflect.TypeToken;
import org.codepenguin.example.springboot.openfeign.restcountries.domain.RestCountry;
import org.codepenguin.example.springboot.openfeign.restcountries.domain.SimpleRestCountry;
import org.codepenguin.example.springboot.openfeign.restcountries.service.RestCountriesService;
import org.codepenguin.util.JsonUtilException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.codepenguin.example.springboot.openfeign.restcountries.ResourcePathHelper.MOCK_REST_COUNTRY_REPOSITORY_FIND_BY_ID_RESPONSE;
import static org.codepenguin.example.springboot.openfeign.restcountries.ResourcePathHelper.MOCK_SIMPLE_REST_COUNTRY_REPOSITORY_FIND_ALL_RESPONSE;
import static org.codepenguin.util.JsonUtil.fromResourceContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestCountriesControllerTest {

    private final TypeToken<List<SimpleRestCountry>> listSimpleRestCountryTypeToken = new TypeToken<>() {
    };

    @Mock
    private RestCountriesService restCountriesServiceMock;

    @Mock
    private Model modelMock;

    private RestCountriesController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        controller = new RestCountriesController(restCountriesServiceMock);
    }

    @Test
    void givenRandomParameterWhenIndexThenLoadARandomCountryToModel() throws JsonUtilException {
        final var simpleRestCountries = fromResourceContent(
                MOCK_SIMPLE_REST_COUNTRY_REPOSITORY_FIND_ALL_RESPONSE, listSimpleRestCountryTypeToken, getClass());

        final var restCountry = fromResourceContent(
                MOCK_REST_COUNTRY_REPOSITORY_FIND_BY_ID_RESPONSE, RestCountry.class, getClass());

        when(restCountriesServiceMock.getAllCountries()).thenReturn(simpleRestCountries);
        when(restCountriesServiceMock.getCountryByAlphaCode(anyString())).thenReturn(restCountry);
        when(modelMock.addAttribute(eq("country"), any(RestCountry.class))).thenReturn(modelMock);
        when(modelMock.addAttribute(eq("countries"), any(List.class))).thenReturn(modelMock);

        final var result = controller.index("random", null, modelMock);
        assertNotNull(result);
        assertEquals("index", result);

        verify(restCountriesServiceMock, times(INTEGER_ONE)).getAllCountries();
        verify(restCountriesServiceMock, times(INTEGER_ONE)).getCountryByAlphaCode(anyString());
        verify(modelMock, times(INTEGER_ONE)).addAttribute(eq("country"), any(RestCountry.class));
        verify(modelMock, times(INTEGER_ONE)).addAttribute(eq("countries"), any(List.class));
    }

    @Test
    void givenAlphaCodeWhenIndexThenLoadCountryToModel() throws JsonUtilException {
        final var simpleRestCountries = fromResourceContent(
                MOCK_SIMPLE_REST_COUNTRY_REPOSITORY_FIND_ALL_RESPONSE, listSimpleRestCountryTypeToken, getClass());

        final var restCountry = fromResourceContent(
                MOCK_REST_COUNTRY_REPOSITORY_FIND_BY_ID_RESPONSE, RestCountry.class, getClass());

        when(restCountriesServiceMock.getAllCountries()).thenReturn(simpleRestCountries);
        when(restCountriesServiceMock.getCountryByAlphaCode(anyString())).thenReturn(restCountry);
        when(modelMock.addAttribute(eq("country"), any(RestCountry.class))).thenReturn(modelMock);
        when(modelMock.addAttribute(eq("countries"), any(List.class))).thenReturn(modelMock);

        final var result = controller.index(null, "CO", modelMock);
        assertNotNull(result);
        assertEquals("index", result);

        verify(restCountriesServiceMock, times(INTEGER_ONE)).getAllCountries();
        verify(restCountriesServiceMock, times(INTEGER_ONE)).getCountryByAlphaCode(anyString());
        verify(modelMock, times(INTEGER_ONE)).addAttribute(eq("country"), any(RestCountry.class));
        verify(modelMock, times(INTEGER_ONE)).addAttribute(eq("countries"), any(List.class));
    }

    @Test
    void givenNoParametersWhenIndexThenNoLoadCountryToModel() throws JsonUtilException {
        final var simpleRestCountries = fromResourceContent(
                MOCK_SIMPLE_REST_COUNTRY_REPOSITORY_FIND_ALL_RESPONSE, listSimpleRestCountryTypeToken, getClass());

        when(restCountriesServiceMock.getAllCountries()).thenReturn(simpleRestCountries);
        when(modelMock.addAttribute(eq("country"), any(RestCountry.class))).thenReturn(modelMock);
        when(modelMock.addAttribute(eq("countries"), any(List.class))).thenReturn(modelMock);

        final var result = controller.index(null, null, modelMock);
        assertNotNull(result);
        assertEquals("index", result);

        verify(restCountriesServiceMock, times(INTEGER_ONE)).getAllCountries();
        verify(restCountriesServiceMock, times(INTEGER_ZERO)).getCountryByAlphaCode(anyString());
        verify(modelMock, times(INTEGER_ZERO)).addAttribute(eq("country"), any(RestCountry.class));
        verify(modelMock, times(INTEGER_ONE)).addAttribute(eq("countries"), any(List.class));
    }
}