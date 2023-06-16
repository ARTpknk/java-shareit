package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.exceptions.model.ShareItNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else {
            throw new OwnerNotFoundException("User with Id: " + id + " not found");
        }
    }

    @Override
    public User update(User user) {
        int id = user.getId();
        if (repository.findById(id).isPresent()) {
            User updateUser = repository.findById(id).get();
            String name = user.getName();
            String email = user.getEmail();

            if (name != null && !name.isBlank()) {
                updateUser.setName(name);
            }

            if (email != null && !email.isBlank()) {
                updateUser.setEmail(email);
            }
            return repository.save(updateUser);
        } else {
            throw new ShareItNotFoundException(String.format("Пользователь с таким id: %d не найден.", id));
        }
    }

    @Override
    public void deleteUserById(Integer id) {
        repository.deleteById(id);
    }
}
