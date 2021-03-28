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

import org.apache.commons.lang3.StringUtils;
import org.codepenguin.example.springboot.openfeign.restcountries.domain.RestCountry;
import org.codepenguin.example.springboot.openfeign.restcountries.domain.SimpleRestCountry;
import org.codepenguin.example.springboot.openfeign.restcountries.service.RestCountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller for the RestCountries.
 *
 * @author Jorge Garcia
 * @version 0.0.1
 * @since 11
 */
@Controller
public class RestCountriesController {

    public static final Logger LOGGER = Logger.getLogger(RestCountriesController.class.getName());

    private final RestCountriesService restCountriesService;

    /**
     * Constructor.
     *
     * @param restCountriesService the RestCountries service.
     */
    @Autowired
    public RestCountriesController(RestCountriesService restCountriesService) {
        this.restCountriesService = restCountriesService;
    }

    /**
     * Process the index requests.
     *
     * @param random    if this value isn't blank, then it indicates to select a random country. It takes precedence
     *                  over whatever value the parameter alphaCode has.
     * @param alphaCode the country's alpha code. Isn't required.
     * @param model     the UI model.
     * @return the template name.
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping("/")
    public String index(@RequestParam(required = false) String random, @RequestParam(required = false) String alphaCode,
                        Model model) {
        LOGGER.log(Level.FINEST, "random = {0}, alphaCode = {1}", new Object[]{random, alphaCode});

        final List<SimpleRestCountry> allCountries = getAllCountries();
        getRestCountry(StringUtils.isNotBlank(random), alphaCode, allCountries)
                .ifPresent(country -> model.addAttribute("country", country));
        model.addAttribute("countries", allCountries);
        return "index";
    }

    private List<SimpleRestCountry> getAllCountries() {
        final var allCountries = restCountriesService.getAllCountries();
        allCountries.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return allCountries;
    }

    private Optional<RestCountry> getRestCountry(final boolean random, final String alphaCode,
                                                 final List<SimpleRestCountry> allCountries) {
        if (random) {
            final var index = new Random().nextInt(allCountries.size());
            return Optional.of(restCountriesService.getCountryByAlphaCode(allCountries.get(index).getAlpha2Code()));
        }

        if (StringUtils.isNotBlank(alphaCode)) {
            return Optional.of(restCountriesService.getCountryByAlphaCode(alphaCode));
        }

        return Optional.empty();
    }
}
