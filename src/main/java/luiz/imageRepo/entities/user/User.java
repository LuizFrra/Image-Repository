package luiz.imageRepo.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import luiz.imageRepo.entities.image.Image;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Table(name = "tbl_users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(columnDefinition = "Decimal(10,2) default 10000.00")
    @Setter(value = AccessLevel.NONE)
    private double cash = 10000.00;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Authoritie> authorities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    public Set<Image> images;

    public User(String email, String password, ArrayList<SimpleGrantedAuthority> authorities) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.authorities == null) return null;

        return this.authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthorite().getRole()))
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    public boolean subtractFromCash(double value) {
        if(cash >= value) {
            cash -= value;
            return true;
        }

        return false;
    }

    public boolean addToCash(double value) {
        cash += value;
        return true;
    }

    public boolean addAuthorite(ROLES role) {
        if(authorities == null)
            authorities = new HashSet<>();

        return authorities.add(new Authoritie(role, this));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
