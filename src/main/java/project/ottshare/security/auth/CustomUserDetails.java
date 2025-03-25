package project.ottshare.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.ottshare.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class CustomUserDetails implements UserDetails, OAuth2User {
    private final Optional<User> user;
    private final Map<String, Object> attributes;

    public CustomUserDetails(Optional<User> user) {
        this.user = user;
        this.attributes = null;
    }

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = Optional.ofNullable(user);
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() -> "ROLE_" + user.get().getRole());

        return collection;
    }

    @Override
    public String getPassword() {
        return user.get().getPassword();
    }

    @Override
    public String getUsername() {
        return user.get().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return user.get().getName();
    }

    public Long userId(){
        return user.get().getId();
    }

    public String getNickname(){
        return user.get().getNickname();
    }

    public String getEmail(){
        return user.get().getEmail();
    }
}
