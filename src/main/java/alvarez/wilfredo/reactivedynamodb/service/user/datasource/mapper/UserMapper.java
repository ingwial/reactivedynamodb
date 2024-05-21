package alvarez.wilfredo.reactivedynamodb.service.user.datasource.mapper;


import alvarez.wilfredo.reactivedynamodb.service.user.datasource.domain.User;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public interface UserMapper {
    static List<User> to(List<Map<String, AttributeValue>> items) {
        return items.stream()
                .map(UserMapper::to)
                .collect(Collectors.toList());
    }

    static User to(Map<String, AttributeValue> attributeMap) {
        if (Objects.nonNull(attributeMap.get("id")) && Objects.nonNull(attributeMap.get("name"))) {
            return new User(attributeMap.get("id").s(), attributeMap.get("name").s());
        }
        return new User();
    }

    static Map<String, AttributeValue> toMap(User user) {
        if (Objects.nonNull(user.getId()) && Objects.nonNull(user.getName())) {
            return Map.of(
                    "id", AttributeValue.builder().s(user.getId()).build(),
                    "name", AttributeValue.builder().s(user.getName()).build());
        }
        return new HashMap<>();
    }
}
