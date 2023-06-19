package shareit.request.controller;

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
import shareit.request.dto.RequestDto;
import shareit.request.model.Request;
import shareit.request.service.RequestService;
import shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class RequestControllerTest {

    @MockBean
    private RequestService requestService;
    @MockBean
    private UserService userService;

    @InjectMocks
    private RequestController controller;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;
    private Request request;
    private Request request2;
    private RequestDto requestDto;
    private RequestDto requestDto2;
    private final int size = 20;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        request = Request.builder()
                .id(1)
                .description("description1")
                .requestorId(1)
                .created(now)
                .build();

        requestDto = RequestDto.builder()
                .id(1)
                .description("description1")
                .created(now)
                .build();

        requestDto2 = RequestDto.builder()
                .id(2)
                .description("description2")
                .created(now)
                .build();

        request2 = Request.builder()
                .id(2)
                .description("description2")
                .requestorId(2)
                .created(now)
                .build();
    }

    @Test
    void createTest() throws Exception {
        when(requestService.create(any(), anyInt()))
                .thenReturn(request);

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()));
    }

    @Test
    void getMyRequestsTest() throws Exception {
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        requests.add(request2);
        when(requestService.getMyRequests(1))
                .thenReturn(requests);

        mvc.perform(get("/requests")
                        .content(objectMapper.writeValueAsString(requests))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Integer.class))
                .andExpect(jsonPath("$[1].id", is(requestDto2.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[1].description", is(requestDto2.getDescription())))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getUserRequestsTest() throws Exception {
        int from = 0;
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        requests.add(request2);

        when(requestService.getByUserIdAndRequestId(1, from, size))
                .thenReturn(requests);

        mvc.perform(get("/requests/all")
                        .content(objectMapper.writeValueAsString(requests))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Integer.class))
                .andExpect(jsonPath("$[1].id", is(requestDto2.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[1].description", is(requestDto2.getDescription())))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getRequest() throws Exception {
        when(requestService.getRequest(1, 1))
                .thenReturn(request);

        mvc.perform(get("/requests/{requestId}", 1)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()));
    }
}
