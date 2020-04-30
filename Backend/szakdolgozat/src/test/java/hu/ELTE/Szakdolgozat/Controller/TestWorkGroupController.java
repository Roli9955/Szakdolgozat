package hu.ELTE.Szakdolgozat.Controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Service.WorkGroupService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = WorkGroupController.class)
public class TestWorkGroupController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WorkGroupService workGroupService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private BCryptPasswordEncoder encoder;

    private User userNotNull;

    private WorkGroup workGroupNotNull;
    private WorkGroup workGroupNotNull2;

    private UserWorkGroup userWorkGroupNotNull;
    private UserWorkGroup userWorkGroupNotNull2;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        workGroupNotNull = TestGlobalMocks.workGroupNotNull;
        workGroupNotNull2 = TestGlobalMocks.workGroupNotNull2;

        userWorkGroupNotNull = TestGlobalMocks.userWorkGroupNotNull;
        userWorkGroupNotNull2 = TestGlobalMocks.userWorkGroupNotNull2;

        userNotNull = TestGlobalMocks.userNotNull;

        String password = userNotNull.getPassword();
        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
        Mockito.when(encoder.encode(userNotNull.getPassword())).thenReturn(password);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN", "ROLE_ACTIVITY_GROUP_ADMIN", "ROLE_LISTING"})
    public void testGetAllWorkGroupyReturnOkWithNull() throws Exception{
        Iterable<WorkGroup> iWorkGroup = null;
        doReturn(iWorkGroup).when(workGroupService).getWorkGroups();
        this.mvc.perform(get("/work-group").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN", "ROLE_ACTIVITY_GROUP_ADMIN", "ROLE_LISTING"})
    public void testGetAllWorkGroupyReturnOk() throws Exception{
        List<WorkGroup> workGroups = new ArrayList<>();
        workGroups.add(workGroupNotNull);
        workGroups.add(workGroupNotNull2);

        Iterable<WorkGroup> iWorkGroup = workGroups;
        doReturn(iWorkGroup).when(workGroupService).getWorkGroups();
        this.mvc.perform(get("/work-group").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testPostNewWorkGroupReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(workGroupNotNull);
        doReturn(workGroupNotNull).when(workGroupService).addNewWorkGroup(Mockito.any(WorkGroup.class));
        this.mvc.perform(post("/work-group/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testPostNewWorkGroupReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(workGroupNotNull);
        doReturn(null).when(workGroupService).addNewWorkGroup(Mockito.any(WorkGroup.class));
        this.mvc.perform(post("/work-group/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testPostAddUserWorkGroupReturnOk() throws Exception{
        List<UserWorkGroup> uwg = new ArrayList<>();
        uwg.add(userWorkGroupNotNull);
        uwg.add(userWorkGroupNotNull2);
        Iterable<UserWorkGroup> res = uwg;

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(res);
        doReturn(res).when(workGroupService).addUserWorkGroups(Mockito.any(Integer.class), Mockito.any(Iterable.class));
        this.mvc.perform(post("/work-group/add/1/user-work-group").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testPostAddUserWorkGroupReturnBadRequest() throws Exception{
        List<UserWorkGroup> uwg = new ArrayList<>();
        uwg.add(userWorkGroupNotNull);
        uwg.add(userWorkGroupNotNull2);
        Iterable<UserWorkGroup> res = uwg;

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(res);
        doReturn(null).when(workGroupService).addUserWorkGroups(Mockito.any(Integer.class), Mockito.any(Iterable.class));
        this.mvc.perform(post("/work-group/add/1/user-work-group").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testGetUserInWorkGroupReturnOk() throws Exception{
        List<WorkGroup> workGroups = new ArrayList<>();
        workGroups.add(workGroupNotNull);
        workGroups.add(workGroupNotNull2);
        doReturn(workGroups).when(workGroupService).getUsersInWorkGroup(Mockito.any(Integer.class));
        this.mvc.perform(get("/work-group/1/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testGetUserInWorkGroupReturnBadRequest() throws Exception{
        List<WorkGroup> workGroups = null;
        doReturn(workGroups).when(workGroupService).getUsersInWorkGroup(Mockito.any(Integer.class));
        this.mvc.perform(get("/work-group/1/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testDeleteWorkGroupReturnOk() throws Exception{
        doReturn(workGroupNotNull).when(workGroupService).deleteWorkGroup(Mockito.any(Integer.class));
        this.mvc.perform(delete("/work-group/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testDeleteWorkGroupReturnBadRequest() throws Exception{
        doReturn(null).when(workGroupService).deleteWorkGroup(Mockito.any(Integer.class));
        this.mvc.perform(delete("/work-group/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testPutEditWorkGroupReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(workGroupNotNull);
        doReturn(workGroupNotNull).when(workGroupService).editWorkGroup(Mockito.any(WorkGroup.class));
        this.mvc.perform(put("/work-group/edit").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testPutEditWorkGroupReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(workGroupNotNull);
        doReturn(null).when(workGroupService).editWorkGroup(Mockito.any(WorkGroup.class));
        this.mvc.perform(put("/work-group/edit").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testPutEditUserWorkGroupInWorkGroupReturnOk() throws Exception{
        List<UserWorkGroup> uwg = new ArrayList<>();
        uwg.add(userWorkGroupNotNull);
        uwg.add(userWorkGroupNotNull2);
        Iterable<UserWorkGroup> res = uwg;

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(res);
        doReturn(uwg).when(workGroupService).editUserWorkGroups(Mockito.any(Integer.class), Mockito.any(Iterable.class));
        this.mvc.perform(put("/work-group/edit/1/user-work-group").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PROJECT_ADMIN"})
    public void testPutEditUserWorkGroupInWorkGroupReturnBadRequest() throws Exception{
        List<UserWorkGroup> uwg = new ArrayList<>();
        uwg.add(userWorkGroupNotNull);
        uwg.add(userWorkGroupNotNull2);
        Iterable<UserWorkGroup> res = uwg;

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(res);
        doReturn(null).when(workGroupService).editUserWorkGroups(Mockito.any(Integer.class), Mockito.any(Iterable.class));
        this.mvc.perform(put("/work-group/edit/1/user-work-group").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}
