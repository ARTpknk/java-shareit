package shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shareit.user.dto.UserDto;
import shareit.user.model.User;
import shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController controller;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    private UserDto userDto;
    private UserDto userDto2;
    private User user;
    private User user2;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        userDto = UserDto.builder()
                .id(1)
                .name("user")
                .email("user@email.ru")
                .build();

        userDto2 = UserDto.builder()
                .id(2)
                .name("user2")
                .email("user2@email.ru")
                .build();

        user = User.builder()
                .id(1)
                .name("user")
                .email("user@email.ru")
                .build();

        user2 = User.builder()
                .id(2)
                .name("user2")
                .email("user2@email.ru")
                .build();
    }

    @Test
    void createTest() throws Exception {
        when(userService.create(user))
                .thenReturn(user);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void updateTest() throws Exception {
        user.setName("Игорь");
        userDto.setName("Игорь");
        when(userService.update(user))
                .thenReturn(user);

        mvc.perform(patch("/users/{id}", userDto.getId())
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void findAllTest() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        when(userService.getAllUsers())
                .thenReturn(users);

        mvc.perform(get("/users")
                        .content(objectMapper.writeValueAsString(users))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$[1].id", is(userDto2.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[1].name", is(userDto2.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$[1].email", is(userDto2.getEmail())))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void findUserByIdTest() throws Exception {
        when(userService.getUserById(1))
                .thenReturn(user);

        mvc.perform(get("/users/{id}", userDto.getId())
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void deleteUserById() throws Exception {
        when(userService.getUserById(1))
                .thenReturn(user);
        mvc.perform(delete("/users/{userId}", userDto.getId())
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUnknownUserById() throws Exception {
        when(userService.getUserById(1))
                .thenReturn(null);
        userService.deleteUserById(1);
        mvc.perform(delete("/users/{userId}", userDto.getId())
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}


