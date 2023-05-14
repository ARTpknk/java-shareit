package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.exceptions.model.ShareItNotFoundException;
import ru.practicum.shareit.user.dto.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserDtoStorageImpl implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emailSet = new HashSet<>();

    private Integer id = 1;

    public User create(User user) {
        if (emailSet.contains(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        Integer id = getNextId();
        User newUser = user.withId(id);
        users.put(id, newUser);
        emailSet.add(user.getEmail());
        return newUser;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new OwnerNotFoundException(String.format("Пользователь с id: %d не найден.", id));
        }
    }

    public void deleteUserById(Integer id) {

        User user = users.remove(id);
        emailSet.remove(user.getEmail());
    }

    public Integer getNextId() {
        return id++;
    }

    public User update(User user) {
        int id = user.getId();
        if (users.containsKey(id)) {
            User updateUser = users.get(id);
            String name = user.getName();
            String email = user.getEmail();

            if (name != null && !name.isBlank()) {
                updateUser.setName(name);
            }

            if (email != null && !email.isBlank()) {
                String oldEmail = updateUser.getEmail();
                if (!oldEmail.equals(email)) {
                    if (emailSet.contains(email)) {
                        throw new EmailAlreadyExistsException("Email already exists");
                    } else {
                        emailSet.remove(oldEmail);
                        emailSet.add(email);
                        updateUser.setEmail(email);
                    }
                }
            }
            return updateUser;
        } else {
            throw new ShareItNotFoundException(String.format("Пользователь с таким id: %d не найден.", user.getId()));
        }
    }


    public User getUser(int id) {
        return users.get(id);
    }
}