package hk.myles.CopyAndPaste.service;

import hk.myles.CopyAndPaste.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUserById(String id);

    Optional<User> fetchUserById(String id);

    User updateUser(User user);

    List<User> fetchUsers();

    void removeUserById(String id);
}
