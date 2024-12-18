package com.example.mlservice.service.impl;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.IOException;

@Service
public class DialogflowService {

    private final SessionsClient sessionsClient;
    private final String projectId;

    public DialogflowService(
            @Value("${dialogflow.project-id}") String projectId,
            @Value("${dialogflow.credentials.file-path}") String credentialsPath
    ) throws IOException {
        this.projectId = projectId;

        // Load credentials from the specified file path
        InputStream credentialsStream = new ClassPathResource(credentialsPath).getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

        // Create a channel to specify endpoint explicitly
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("dialogflow.googleapis.com", 443)
                .build();

        SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                .setTransportChannelProvider(
                        FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel))
                )
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        this.sessionsClient = SessionsClient.create(sessionsSettings);
    }

    public String detectIntentTexts(String sessionId, String text) {
        SessionName session = SessionName.of(projectId, sessionId);
        TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode("vi");

        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
        DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

        return response.getQueryResult().getFulfillmentText();
    }
}
