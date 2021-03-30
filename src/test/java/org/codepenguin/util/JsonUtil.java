/*
 * MIT License
 *
 * Copyright (c) 2021 Jorge Garcia - codepenguin.org
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

package org.codepenguin.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility for JSON.
 * <p>
 * <i>NOTE: This class will be moved to an independent library in the future.</i>
 *
 * @author Jorge Garcia
 * @version 1.0.0
 * @since 11
 */
public final class JsonUtil {
    private JsonUtil() {
    }

    /**
     * Gets an instance of the type from the json content.
     *
     * @param json     the json content.
     * @param classOfT the class of the type.
     * @param <T>      the type.
     * @return the instance of the type.
     */
    public static <T> T fromJson(final String json, final Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    /**
     * Gets an instance of the type from the json content.
     *
     * @param json      the json content.
     * @param typeToken the type token of the type.
     * @param <T>       the type.
     * @return the instance of the type.
     */
    public static <T> T fromJson(final String json, final TypeToken<T> typeToken) {
        return new Gson().fromJson(json, typeToken.getType());
    }

    /**
     * Gets an instance of the type from the resource content.
     *
     * @param resourceName      the resource name.
     * @param classOfT          the class of the type.
     * @param classLoaderSource the class to get the class loader.
     * @param <T>               the type.
     * @return the instance of the type.
     * @throws JsonUtilException If an IO or URI exception has occurred.
     */
    public static <T> T fromResourceContent(final String resourceName, final Class<T> classOfT,
                                            final Class<?> classLoaderSource) throws JsonUtilException {
        return fromJson(getResourceContent(resourceName, classLoaderSource), classOfT);
    }

    /**
     * Gets an instance of the type from the resource content.
     *
     * @param resourceName      the resource name.
     * @param typeToken         the type token of the type.
     * @param classLoaderSource the class to get the class loader.
     * @param <T>               the type.
     * @return the instance of the type.
     * @throws JsonUtilException If an IO or URI exception has occurred.
     */
    public static <T> T fromResourceContent(final String resourceName, final TypeToken<T> typeToken,
                                            final Class<?> classLoaderSource) throws JsonUtilException {
        return fromJson(getResourceContent(resourceName, classLoaderSource), typeToken);
    }


    /**
     * Gets the content from a text resource.
     *
     * @param resourceName      the resource name.
     * @param classLoaderSource the class to get the class loader.
     * @return the text content.
     * @throws JsonUtilException If an IO or URI exception has occurred.
     */
    public static String getResourceContent(final String resourceName, final Class<?> classLoaderSource)
            throws JsonUtilException {
        final var resource = classLoaderSource.getClassLoader().getResource(resourceName);
        try {
            return Files.readString(Path.of(Objects.requireNonNull(resource).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new JsonUtilException(e);
        }
    }
}
