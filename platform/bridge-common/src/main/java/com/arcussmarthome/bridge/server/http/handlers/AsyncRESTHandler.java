/*
 * Copyright 2019 Arcus Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arcussmarthome.bridge.server.http.handlers;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arcussmarthome.bridge.server.config.RESTHandlerConfig;
import com.arcussmarthome.bridge.server.http.HttpSender;
import com.arcussmarthome.bridge.server.http.RequestAuthorizer;
import com.arcussmarthome.bridge.server.http.impl.AsyncHttpResource;
import com.arcussmarthome.bridge.server.netty.BridgeHeaders;
import com.arcussmarthome.capability.util.Addresses;
import com.arcussmarthome.io.json.JSON;
import com.arcussmarthome.messages.ClientMessage;
import com.arcussmarthome.messages.MessageBody;
import com.arcussmarthome.messages.errors.Errors;
import com.arcussmarthome.messages.service.PlaceService;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * This is a copy of RESTHandler that extends AsyncHttpResource instead of HttpResource.
 * 
 * TODO: As Java doesn't support multiple inheritance, I looked into several ways to merge this with RESTHandler via
 * a composition pattern like Decorator.  However, given the current state of our RequestHandler class hierarchy, it
 * seems like any such approach will require significant changes to heavily-reused classes within that hierarchy, and
 * this would introduce risk and require lots of QA.
 */
public abstract class AsyncRESTHandler extends AsyncHttpResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncRESTHandler.class);

	private final RESTHandlerConfig restHandlerConfig;

	public AsyncRESTHandler(RequestAuthorizer authorizer, HttpSender httpSender, Executor executor, RESTHandlerConfig restHandlerConfig) {
		super(authorizer, httpSender, executor);
		this.restHandlerConfig = restHandlerConfig;
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public FullHttpResponse respond(FullHttpRequest req, ChannelHandlerContext ctx) throws Exception {
		ClientMessage request;
		MessageBody response;

		try {
			request = decode(req);
		} catch (Throwable t) {
			LOGGER.debug("Unable to decode request", t);
			return response(HttpResponseStatus.BAD_REQUEST, "plain/text", "Unable to decode request");
		}

		HttpResponseStatus status = HttpResponseStatus.OK;
		try {
		   /* preHandleValidation is typically a NOOP. However
		    * there may be times where a RESTHandler might need
		    * AUTH style checks that require access to the
		    * ChannelHandlerContext.
		    */
		   assertValidRequest(req, ctx);
			response = doHandle(request, ctx);
		} catch (Throwable th) {
			LOGGER.error("Error handling client message", th);
			response = Errors.fromException(th);
			status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
		}

		ClientMessage message = ClientMessage.builder().withCorrelationId(request.getCorrelationId())
				.withDestination(request.getSource()).withSource(Addresses.toServiceAddress(PlaceService.NAMESPACE))
				.withPayload(response).create();

		return response(status, BridgeHeaders.CONTENT_TYPE_JSON_UTF8, JSON.toJson(message));
	}

	protected void assertValidRequest(FullHttpRequest req, ChannelHandlerContext ctx) {
	   return;
	}
	
	protected abstract MessageBody doHandle(ClientMessage request) throws Exception;
	
	protected MessageBody doHandle(ClientMessage request, ChannelHandlerContext ctx) throws Exception {
		return doHandle(request);
	}

	protected ClientMessage decode(FullHttpRequest request) {
		return RESTHandlerUtil.decode(request);
	}

	protected FullHttpResponse response(HttpResponseStatus status, String contentType, String contents) {
		FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer(contents, CharsetUtil.UTF_8));
		if (restHandlerConfig.isSendChunked()) {
			HttpHeaders.setTransferEncodingChunked(httpResponse);
		}
		httpResponse.headers().set(HttpHeaders.Names.CONTENT_TYPE, contentType);
		return httpResponse;
	}
}

