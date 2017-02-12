/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package alpine.filters;

import alpine.Config;
import alpine.auth.PermissionRequired;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

/**
 * Determines if authorization is required or not (via {@link Config.Key#ENFORCE_AUTHENTICATION}
 * and {@link Config.Key#ENFORCE_AUTHORIZATION} and if so mandates that all resources requested
 * have the necessary permissions required to access the resource using {@link PermissionRequired}.
 *
 * @see AuthorizationFilter
 * @since 1.0.0
 */
@Provider
public class AuthorizationFeature implements DynamicFeature {

    private static final boolean ENFORCE_AUTHENTICATION = Config.getInstance().getPropertyAsBoolean(Config.Key.ENFORCE_AUTHENTICATION);
    private static final boolean ENFORCE_AUTHORIZATION = Config.getInstance().getPropertyAsBoolean(Config.Key.ENFORCE_AUTHORIZATION);

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (ENFORCE_AUTHENTICATION && ENFORCE_AUTHORIZATION) {
            Method method = resourceInfo.getResourceMethod();
            if (method.isAnnotationPresent(PermissionRequired.class)) {
                context.register(AuthorizationFilter.class);
            }
        }
    }

}