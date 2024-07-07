package hk.myles.CopyAndPaste.service;

import hk.myles.CopyAndPaste.model.User;
import hk.myles.CopyAndPaste.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUserById(String id) {
        // generate random username
        Faker faker = new Faker();
        User user = User.builder()
                .id(id)
                .name(faker.name().firstName())
                .lastSeen(new Date()).build();
        return userRepository.save(user);
    }

    public Optional<User> fetchUserById(String id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        String id = user.getId();
        User userDB = userRepository.findById(id).get();

        if (!"".equalsIgnoreCase(user.getName())) {
            userDB.setName(user.getName());
        }

        if (user.getLastSeen() != null) {
            userDB.setLastSeen(user.getLastSeen());
        }

        return userRepository.save(userDB);
    }

    @Override
    public void removeUserById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> fetchUsers() {
        return (List<User>) userRepository.findAll();
    }
}
