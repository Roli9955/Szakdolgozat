package hu.ELTE.Szakdolgozat.Controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
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

import static org.mockito.Mockito.doReturn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MainController.class)
public class TestMainController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private BCryptPasswordEncoder encoder;

    private User userNotNull;

    private ActivityGroup activityGroupNotNull;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;

        activityGroupNotNull = TestGlobalMocks.activityGroupNotNull;

        String password = userNotNull.getPassword();
        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
        Mockito.when(encoder.encode(userNotNull.getPassword())).thenReturn(password);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_USER"})
    public void testLoginReturnOk() throws Exception{
        doReturn(userNotNull).when(userService).login();
        this.mvc.perform(put("/login").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {})
    public void testEasyLoginReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(userNotNull);
        doReturn(null).when(userService).easyLogIn(userNotNull, 1);
        this.mvc.perform(post("/easy-log-in/activity-group/1").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

}
