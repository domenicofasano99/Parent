package com.bok.parent.be.grpc;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.annotation.Nullable;

@Configuration
public class GrpcConfiguration implements GrpcAuthenticationReader {
    @Nullable
    @Override
    public Authentication readAuthentication(ServerCall<?, ?> call, Metadata headers) throws AuthenticationException {
        return null;
    }
}
