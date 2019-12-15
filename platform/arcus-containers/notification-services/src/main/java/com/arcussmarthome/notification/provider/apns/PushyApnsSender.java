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
package com.arcussmarthome.notification.provider.apns;

import java.io.InputStream;
import java.util.concurrent.SynchronousQueue;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.arcussmarthome.notification.NotificationServiceConfig;
import com.arcussmarthome.notification.upstream.UpstreamNotificationResponder;
import com.arcussmarthome.platform.notification.Notification;
import com.arcussmarthome.resource.Resources;
import com.relayrides.pushy.apns.ApnsConnectionConfiguration;
import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.util.SSLContextUtil;

import io.netty.channel.nio.NioEventLoopGroup;

@Singleton
public class PushyApnsSender implements ApnsSender {

    private static final Logger logger = LoggerFactory.getLogger(PushyApnsSender.class);

    private final PushManager<IrisApnsPushNotification> pushManager;
    private final UpstreamNotificationResponder upstreamResponder;

    @Inject
    public PushyApnsSender(
            NotificationServiceConfig config,
            @Named("apns.pkcs12.path") String pkcs12Path,
            @Named("apns.pkcs12.password") String keystorePassword,
            @Named("apns.production") boolean isProduction,
            @Named("apns.inactiveCloseTime") int inactiveCloseTime,
            UpstreamNotificationResponder upstreamResponder) throws Exception
    {
        this.upstreamResponder = upstreamResponder;

        InputStream pkcsStream = Resources.open(pkcs12Path);

        PushManagerConfiguration pushManagerConfig = new PushManagerConfiguration();
        ApnsConnectionConfiguration connConfig = pushManagerConfig.getConnectionConfiguration();
        connConfig.setCloseAfterInactivityTime(inactiveCloseTime);

        pushManagerConfig.setConnectionConfiguration(connConfig);
        pushManagerConfig.setConcurrentConnectionCount(config.getApnsConnections());

        NioEventLoopGroup elGroup = new NioEventLoopGroup(Math.min(config.getApnsConnections(), config.getApnsThreads()));
        pushManager = new PushManager<IrisApnsPushNotification>(
                isProduction ? ApnsEnvironment.getProductionEnvironment() : ApnsEnvironment.getSandboxEnvironment(), // production or sandbox environment
                        SSLContextUtil.createDefaultSSLContext(pkcsStream, keystorePassword), // SSL context
                        elGroup, // Optional: custom event loop group
                        null, // Optional: custom ExecutorService for calling listeners
                        new SynchronousQueue<>(), // Optional: custom BlockingQueue implementation
                pushManagerConfig, // default configuration options
                "IrisApnsPushManager"); // Human-readable name of this manager

        // Register listeners for error conditions
        pushManager.registerRejectedNotificationListener(new RejectedNotificationAuditor(upstreamResponder));
        pushManager.registerFailedConnectionListener(new FailedConnectionAuditor());
        pushManager.registerExpiredTokenListener(new ExpiredTokenAuditor(upstreamResponder));

        // Start the Pushy service
        pushManager.start();

        // Request to be informed about expired APNS tokens
        pushManager.requestExpiredTokens();

        logger.info("APNS provider has started up.");
    }

    @PreDestroy
    public void shutdown() {
        try {
            logger.info("Shutting down APNS provider.");
            pushManager.shutdown();
        } catch (InterruptedException e) {
            logger.warn("APNS provider failed to shut down gracefully.", e);
        }
    }

    @Override
    public void sendMessage(Notification notification, byte[] token, String payload) {
        try {
            // Put the message in Pushy's queue for transmission to Apple
            pushManager.getQueue().put(new IrisApnsPushNotification(notification, token, payload));
            upstreamResponder.handleHandOff(notification);
        } catch (InterruptedException e) {
            upstreamResponder.handleError(notification, true, e.toString());
        }
    }
}

