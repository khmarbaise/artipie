/*
 * The MIT License (MIT) Copyright (c) 2020-2021 artipie.com
 * https://github.com/artipie/artipie/LICENSE.txt
 */
package com.artipie.api;

import com.jcabi.log.Logger;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.HttpException;
import io.vertx.ext.web.openapi.RouterBuilder;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Base class for rest-api operations.
 * @since 0.26
 */
abstract class BaseRest {
    /**
     * Key 'repo' inside json-object.
     */
    protected static final String REPO = "repo";

    /**
     * Mount openapi operation implementations.
     * @param rbr RouterBuilder
     */
    public abstract void init(RouterBuilder rbr);

    /**
     * Handle error.
     * @param code Error code
     * @return Error handler
     */
    protected Handler<RoutingContext> errorHandler(final int code) {
        return context -> {
            if (context.failure() instanceof HttpException) {
                context.response()
                    .setStatusMessage(context.failure().getMessage())
                    .setStatusCode(((HttpException) context.failure()).getStatusCode())
                    .end();
            } else {
                context.response()
                    .setStatusMessage(context.failure().getMessage())
                    .setStatusCode(code)
                    .end();
            }
            Logger.error(this, context.failure().getMessage());
        };
    }

    /**
     * Read body as JsonObject.
     * @param context RoutingContext
     * @return JsonObject
     */
    protected static JsonObject readJsonObject(final RoutingContext context) {
        return Json.createReader(new StringReader(context.body().asString())).readObject();
    }
}
