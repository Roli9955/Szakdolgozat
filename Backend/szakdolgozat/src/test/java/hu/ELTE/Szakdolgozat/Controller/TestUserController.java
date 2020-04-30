package hu.ELTE.Szakdolgozat.Controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Service.UserService;
import hu.ELTE.Szakdolgozat.Util.TestGlobalMocks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class TestUserController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private BCryptPasswordEncoder encoder;

    private User userNotNull;
    private User userNotNull2;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;
        userNotNull = TestGlobalMocks.userNotNull2;

        String password = userNotNull.getPassword();
        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
        Mockito.when(encoder.encode(userNotNull.getPassword())).thenReturn(password);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER_ADMIN", "ROLE_PERMISSION_ADMIN", "ROLE_ADD_TASK", "ROLE_PROJECT_ADMIN", "ROLE_LISTING"})
    public void testGetAllUsersReturnOk() throws Exception{
        List<User> users = new ArrayList<>();
        users.add(userNotNull);
        users.add(userNotNull2);
        Iterable<User> res = users;
        doReturn(res).when(userService).getAllUser();
        this.mvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER_ADMIN", "ROLE_PERMISSION_ADMIN", "ROLE_ADD_TASK", "ROLE_PROJECT_ADMIN", "ROLE_LISTING"})
    public void testGetAllUsersReturnOk2() throws Exception{
        List<User> users = new ArrayList<>();
        Iterable<User> res = users;
        doReturn(res).when(userService).getAllUser();
        this.mvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER_ADMIN"})
    public void testPostUserReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(userNotNull);
        doReturn(userNotNull).when(userService).adduser(Mockito.any(User.class));
        this.mvc.perform(post("/user/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_HOLIDAY_ADMIN"})
    public void testGetUsersForHolidayReturnOk() throws Exception{
        List<User> users = new ArrayList<>();
        users.add(userNotNull);
        users.add(userNotNull2);
        Iterable<User> res = users;
        doReturn(res).when(userService).getUsersForHoliday();
        this.mvc.perform(get("/user/holiday").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_HOLIDAY_ADMIN"})
    public void testGetUsersForHolidayReturnBadRequest() throws Exception{
        Iterable<User> res = null;
        doReturn(res).when(userService).getUsersForHoliday();
        this.mvc.perform(get("/user/holiday").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER_ADMIN"})
    public void testDeleteUserReturnOk() throws Exception{
        doReturn(userNotNull).when(userService).deleteUser(Mockito.any(Integer.class));
        this.mvc.perform(delete("/user/delete/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER_ADMIN"})
    public void testDeleteUserReturnBadRequest() throws Exception{
        doReturn(null).when(userService).deleteUser(Mockito.any(Integer.class));
        this.mvc.perform(delete("/user/delete/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER_ADMIN"})
    public void testPutEditUserReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(userNotNull);
        doReturn(userNotNull).when(userService).editUser(Mockito.any(User.class));
        this.mvc.perform(put("/user/edit").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER_ADMIN"})
    public void testPutEditUserReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(userNotNull);
        doReturn(null).when(userService).editUser(Mockito.any(User.class));
        this.mvc.perform(put("/user/edit").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}
