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
package com.acme.biz.api.enums;

import org.springframework.http.HttpStatus;

/**
 * 状态码
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see HttpStatus
 * @since
 */
public enum StatusCode {

    OK(0, "OK") {
        @Override
        public String getMessage() {
            return super.message;
        }
    },


    FAILED(-1, "Failed"),

    CONTINUE(1, "{status-code.continue}");

    private final int code;

    private final String message; // 可能需要支持国际化

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return getLocalizedMessage();
    }

    public String getLocalizedMessage() {
        // FIXME 增加国际化支持
        // 如果 message 是占位符，翻译成当前 message text
        // 否则，直接返回 message
        return message;
    }
}

class MyEnum {

    public static final MyEnum ONE = new MyEnum() { // # 匿名类

        @Override
        public String getValue() {
            return "ONE";
        }
    };

    private MyEnum() {

    }

    public String getValue() {
        return "";
    }
}
