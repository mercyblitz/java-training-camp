/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.biz.api;

import com.acme.biz.api.enums.StatusCode;

import javax.validation.Valid;

/**
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since
 */
public class ApiResponse<T>  {

    private int code;

    private String message;

    @Valid
    private T body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public static <T> ApiResponse<T> ok(T body) {
        return of(body, StatusCode.OK);
    }

    public static <T> ApiResponse<T> failed(T body) {
        return of(body, StatusCode.FAILED);
    }

    public static <T> ApiResponse<T> failed(T body, String errorMessage) {
        ApiResponse<T> response = of(body, StatusCode.FAILED);
        response.setMessage(errorMessage);
        return response;
    }

    public static <T> ApiResponse<T> of(T body, StatusCode statusCode) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.setBody(body);
        response.setCode(statusCode.getCode());
        response.setMessage(statusCode.getLocalizedMessage());
        return response;
    }

    public static class Builder<T> {

        private int code;

        private String message;

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

    }
}
