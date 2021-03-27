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

import org.codepenguin.example.springboot.openfeign.restcountries.client.RestCountriesClient;
import org.codepenguin.example.springboot.openfeign.restcountries.domain.RestCountry;
import org.codepenguin.example.springboot.openfeign.restcountries.domain.SimpleRestCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service for RestCountries.
 *
 * @author Jorge Garcia
 * @version 0.0.1
 * @since 11
 */
@Service
public class RestCountriesService {

    private final RestCountriesClient restCountriesClient;

    /**
     * Constructor.
     *
     * @param restCountriesClient The RestCountries client.
     */
    @Autowired
    public RestCountriesService(RestCountriesClient restCountriesClient) {
        this.restCountriesClient = restCountriesClient;
    }

    /**
     * Gets a list of simple countries.
     *
     * @return the list of simple countries.
     */
    public List<SimpleRestCountry> getAllCountries() {
        return restCountriesClient.getAllCountries();
    }

    /**
     * Gets a country by its alpha code.
     *
     * @param alphaCode the alpha code.
     * @return the country.
     */
    public RestCountry getCountryByAlphaCode(String alphaCode) {
        return restCountriesClient.getCountryByAlphaCode(alphaCode);
    }
}
