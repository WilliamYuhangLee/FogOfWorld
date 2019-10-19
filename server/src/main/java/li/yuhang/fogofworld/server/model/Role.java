package li.yuhang.fogofworld.server.model;

import javax.persistence.*;

public enum Role {
    ADMIN,
    USER;

    @Converter(autoApply = true)
    public static class RoleConverter implements AttributeConverter<Role, String> {

        @Override
        public String convertToDatabaseColumn(Role attribute) {
            return attribute.name();
        }

        @Override
        public Role convertToEntityAttribute(String dbData) {
            return Role.valueOf(dbData);
        }
    }
}
