package org.example.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.model.BaseModel;


@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)

public class User extends BaseModel {
    private String name;
    private String lastname;
    private String email;
    private String password;
    private UserRole role;

    {
        this.role = UserRole.USER;
    }

    @Override
    public String toString() {
        return "name=" + name +
                "\n lastname=" + lastname +
                "\n role=" + role +
                "\n createdDate=" + createdDate +
                "\n updatedDate=" + updatedDate;
    }
}
