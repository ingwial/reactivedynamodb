package alvarez.wilfredo.reactivedynamodb.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDbConfiguration {
    @Value("${app.aws.region}")
    private String region;

    @Value("${app.aws.dynamodb.endpoint}")
    private String endpoint;



    @Bean
    public DynamoDbAsyncClient dynamoDbClient() {

        return endpoint.isEmpty() ?
                DynamoDbAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .build()
                :
                DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create(endpoint)).build();
    }
}
