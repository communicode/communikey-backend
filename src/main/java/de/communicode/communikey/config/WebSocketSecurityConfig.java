/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * Configures the security for user authentications and requests to the WSS API.
 *
 * @author dvonderbey@communicode.de
 * @since 0.15.0
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
            .simpTypeMatchers(SimpMessageType.CONNECT,
                              SimpMessageType.HEARTBEAT,
                              SimpMessageType.UNSUBSCRIBE,
                              SimpMessageType.DISCONNECT).permitAll()
            .simpDestMatchers("/topic/**", "/queue/**", "/app/**").authenticated()
            .simpSubscribeDestMatchers("/topic/**", "/queue/**", "/app/**").authenticated()
            .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
            .anyMessage().denyAll();
    }
}
