package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Service.UserWorkGroupService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserWorkGroupController.class)
public class TestUserWorkGroupController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserWorkGroupService userWorkGroupService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private BCryptPasswordEncoder encoder;

    private User userNotNull;

    private UserWorkGroup userWorkGroupNotNull;
    private UserWorkGroup userWorkGroupNotNull2;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;

        userWorkGroupNotNull = TestGlobalMocks.userWorkGroupNotNull;
        userWorkGroupNotNull2 = TestGlobalMocks.userWorkGroupNotNull2;

        String password = userNotNull.getPassword();
        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
        Mockito.when(encoder.encode(userNotNull.getPassword())).thenReturn(password);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {})
    public void testGetUserWorkGroupsReturnOkWithNull() throws Exception{
        Iterable<UserWorkGroup> iUserWorkGroup = null;
        doReturn(iUserWorkGroup).when(userWorkGroupService).getUserWorkGroupByDate(2020, 04, 26);
        this.mvc.perform(get("/user-work-group/year/2020/month/4/day/25").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {})
    public void testGetUserWorkGroupsReturnOk() throws Exception{
        List<UserWorkGroup> uwg = new ArrayList<>();
        uwg.add(userWorkGroupNotNull);
        uwg.add(userWorkGroupNotNull2);

        Iterable<UserWorkGroup> iUserWorkGroup = uwg;
        doReturn(iUserWorkGroup).when(userWorkGroupService).getUserWorkGroupByDate(2020, 04, 26);
        this.mvc.perform(get("/user-work-group/year/2020/month/4/day/25").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {})
    public void testGetUserWorkGroupsByUserReturnOk() throws Exception{
        List<UserWorkGroup> uwg = new ArrayList<>();
        uwg.add(userWorkGroupNotNull);
        uwg.add(userWorkGroupNotNull2);

        Iterable<UserWorkGroup> iUserWorkGroup = uwg;
        doReturn(iUserWorkGroup).when(userWorkGroupService).getWorkGroupyByUser(Mockito.any(Integer.class));
        this.mvc.perform(get("/user-work-group/user/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {})
    public void testGetUserWorkGroupsByUserReturnBadRequest() throws Exception{
        Iterable<UserWorkGroup> iUserWorkGroup = null;
        doReturn(iUserWorkGroup).when(userWorkGroupService).getWorkGroupyByUser(Mockito.any(Integer.class));
        this.mvc.perform(get("/user-work-group/user/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}
