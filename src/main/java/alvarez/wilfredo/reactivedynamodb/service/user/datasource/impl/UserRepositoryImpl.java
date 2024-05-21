package alvarez.wilfredo.reactivedynamodb.service.user.datasource.impl;

import alvarez.wilfredo.reactivedynamodb.service.user.datasource.UserRepository;
import alvarez.wilfredo.reactivedynamodb.service.user.datasource.domain.User;
import alvarez.wilfredo.reactivedynamodb.service.user.datasource.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String PK_USER_ID = "id";

    private final DynamoDbAsyncClient dDB;

    @Value("${app.aws.dynamodb.tables.users}")
    private String userTable;

    @Autowired
    public UserRepositoryImpl(DynamoDbAsyncClient dDB) {
        this.dDB = dDB;
    }

    @Override
    public Flux<User> findAll() {
        return Mono.fromFuture(dDB.scan(ScanRequest.builder()
                        .tableName(userTable)
                        .build()))
                .map(ScanResponse::items)
                .map(UserMapper::to)
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<User> findById(String userId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(PK_USER_ID, AttributeValue.builder().s(userId).build());

        GetItemRequest getItemRequest = GetItemRequest
                .builder()
                .tableName(userTable)
                .key(key)
                .build();

        return Mono.fromFuture(dDB.getItem(getItemRequest))
                .map(GetItemResponse::item)
                .map(UserMapper::to);
    }

    @Override
    public Mono<User> save(User user) {
        user.setId(UUID.randomUUID().toString());

        return Mono.fromFuture(dDB.putItem(PutItemRequest.builder()
                        .tableName(userTable)
                        .item(UserMapper.toMap(user))
                        .build()))
                .map(PutItemResponse::attributes)
                .map(map -> user);
    }

    @Override
    public Mono<User> update(String userId, User user) {
        user.setId(userId);

        return Mono.fromFuture(dDB.putItem(PutItemRequest.builder()
                        .tableName(userTable)
                        .item(UserMapper.toMap(user))
                        .build()))
                .map(updateItemResponse -> user);
    }

    @Override
    public Mono<String> delete(String userId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(PK_USER_ID, AttributeValue.builder().s(userId).build());

        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(userTable)
                .key(key)
                .build();

        return Mono.fromFuture(dDB.deleteItem(deleteItemRequest))
                .map(DeleteItemResponse::attributes)
                .map(attributeValueMap -> userId);
    }
}
