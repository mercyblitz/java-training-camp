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
package com.acme.biz.api.openfeign;

import com.acme.biz.api.ApiResponse;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
public class ApiResponseDecoder implements Decoder {

    private final Decoder decoder;

    public ApiResponseDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        // 服务端返回 ApiResponse，客户端需要 Boolean
        String contentType = getContentType(response);
        MediaType mediaType = MediaType.parseMediaType(contentType);
        String version = mediaType.getParameter("v");
        if (version == null) {
            Object object = decoder.decode(response, ApiResponse.class);
            if (object instanceof ApiResponse) {
                return ApiResponse.class.cast(object).getBody();
            }
        }
        return decoder.decode(response, type);
    }

    private String getContentType(Response response) {
        Collection<String> types = response.headers().getOrDefault("Content-Type", Arrays.asList("application/json;v=3"));
        return types.iterator().next();
    }
}
