package bg.softuni.travelbudgetplanner.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class TravelUserDetails extends User {

    public TravelUserDetails(String username, String password,  Collection<? extends GrantedAuthority> authorities){
        super(username, password, authorities);
    }
}
