package bg.softuni.travelbudgetplanner.config;


import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Component
public class UserSession {
    private long id;
    private String username;

    public void login(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public boolean isUserLoggedIn() {
        return id != 0;
    }

    public void logout() {
        id = 0;
        username = "";
    }

    public String username() {
        return username;
    }

    public Long userId() {
        return id;
    }
}
