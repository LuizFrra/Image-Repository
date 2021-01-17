package luiz.imageRepo.services.user;

import luiz.imageRepo.entities.user.ROLES;
import luiz.imageRepo.entities.user.User;
import luiz.imageRepo.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOp = findUserByEmail(username);

        if(userOp.isEmpty())
            throw new UsernameNotFoundException("Email not Found.");

        User user = userOp.get();

        return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()), user.getPassword(),
                user.getAuthorities());
    }

    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addAuthorite(ROLES.USER);
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findUserById(long id) {
        return userRepository.findById(id);
    }
}
