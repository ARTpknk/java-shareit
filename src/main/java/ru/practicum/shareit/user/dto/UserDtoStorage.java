package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.user.exception.ShareItNotFoundException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserDtoStorage {
    private final HashMap<Integer, UserDto> users = new HashMap<>();
    private final Set<String> emailSet = new HashSet<>();

    private Integer id = 1;

    public UserDto create(UserDto user){
        if(emailSet.contains(user.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        Integer id = getNextId();
        UserDto newUser = user.withId(id);
        users.put(id,newUser);
        emailSet.add(user.getEmail());
        return newUser;
    }

    public List<UserDto> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public UserDto getUserBy(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ShareItNotFoundException(String.format("Пользователь с id: %d не найден.", id));
        }
    }

    public void deleteUserBy(Integer id) {
        try{
            UserDto user = users.get(id);
            emailSet.remove(user.getEmail());
            users.remove(id);
        }
        catch (Exception e){
            throw new ShareItNotFoundException("User not found");
        }
    }

    private Integer getNextId() {
        return id++;
    }

    public UserDto update(UserDto user) {
        int id = user.getId();

        if (users.containsKey(id)) {
            if(user.getName()==null){
                user.setName(users.get(id).getName());
            }

            if(user.getEmail()==null){
                user.setEmail(users.get(id).getEmail());
            }
            else{     //проверяем, свободен ли email, который хотят обновить
                String newEmail = user.getEmail();
                String oldEmail = users.get(id).getEmail();
                if(!oldEmail.equals(newEmail)){
                    if(emailSet.contains(newEmail)){
                        throw new EmailAlreadyExistsException("Email already exists");
                    }
                    else{
                        emailSet.remove(oldEmail);
                        emailSet.add(newEmail);
                    }
                }
            }
            users.put(id, user);
            return user;
        } else {
            throw new ShareItNotFoundException(String.format("Пользователь с таким id: %d не найден.", user.getId()));
        }
    }
}
