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
import org.codepenguin.example.springboot.openfeign.restcountries.service.RestCountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     * @param alphaCode the country's alpha code. Isn't required.
     * @param model     the UI model.
     * @return the template name.
     */
    @GetMapping("/")
    public String index(@RequestParam(required = false) String alphaCode, Model model) {
        LOGGER.log(Level.FINEST, "alphaCode = {0}", alphaCode);

        if (StringUtils.isNotBlank(alphaCode)) {
            model.addAttribute("country", restCountriesService.getCountryByAlphaCode(alphaCode));
        }

        final var allCountries = restCountriesService.getAllCountries();
        allCountries.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        model.addAttribute("countries", allCountries);
        return "index";
    }
}
