package hu.ELTE.Szakdolgozat.Controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Class.Result;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.Permission;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Service.PermissionService;
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
@WebMvcTest(value = PermissionController.class)
public class TestPermissionController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PermissionService permissionService;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private BCryptPasswordEncoder encoder;

    private User userNotNull;

    private Permission permissionNotNull;
    private Permission permissionNotNull2;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;

        permissionNotNull = TestGlobalMocks.permissionNotNull;
        permissionNotNull2 = TestGlobalMocks.permissionNotNull2;

        String password = userNotNull.getPassword();
        Mockito.when(authenticatedUser.getUser()).thenReturn(userNotNull);
        Mockito.when(encoder.encode(userNotNull.getPassword())).thenReturn(password);

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testGetAllPermissionReturnOk() throws Exception{
        List<Permission> lPermission = new ArrayList<>();
        lPermission.add(permissionNotNull);
        lPermission.add(permissionNotNull2);
        Iterable<Permission> res = lPermission;
        doReturn(res).when(permissionService).getAllPermission();
        this.mvc.perform(get("/permission").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testGetAllPermissionsReturnBadRequest() throws Exception{
        Iterable<Permission> res = null;
        doReturn(res).when(permissionService).getAllPermission();
        this.mvc.perform(get("/permission").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testPostNewPermissionReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(permissionNotNull);
        doReturn(permissionNotNull).when(permissionService).addNewPermission(Mockito.any(Permission.class));
        this.mvc.perform(post("/permission/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testPostNewPermissionReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(permissionNotNull);
        doReturn(null).when(permissionService).addNewPermission(Mockito.any(Permission.class));
        this.mvc.perform(post("/permission/add").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testDeletePermissionReturnOk() throws Exception{
        doReturn(permissionNotNull).when(permissionService).deletePermission(1);
        this.mvc.perform(delete("/permission/delete/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testDeletePermissionReturnBadRequest() throws Exception{
        doReturn(null).when(permissionService).deletePermission(1);
        this.mvc.perform(delete("/permission/delete/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testPutEditPermissionReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(permissionNotNull);
        Result<Permission, String> res = new Result<>(permissionNotNull, "");
        doReturn(res).when(permissionService).editPermission(Mockito.any(Permission.class));
        this.mvc.perform(put("/permission/edit").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testPutEditPermissionReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(permissionNotNull);
        Result<Permission, String> res = new Result<>(null, "Hiányzó jogosultsági csoport");
        doReturn(res).when(permissionService).editPermission(Mockito.any(Permission.class));
        this.mvc.perform(put("/permission/edit").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testPutEditPermissionReturnBadRequest2() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(permissionNotNull);
        Result<Permission, String> res = new Result<>(null, "Ilyen névvel már van jogosultsági csoport");
        doReturn(res).when(permissionService).editPermission(Mockito.any(Permission.class));
        this.mvc.perform(put("/permission/edit").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testGetPermissionsUsersReturnOk() throws Exception{
        List<User> lUsers = new ArrayList<>();
        lUsers.add(userNotNull);
        doReturn(lUsers).when(permissionService).getUsersByPermissionId(Mockito.any(Integer.class));
        this.mvc.perform(get("/permission/1/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testGetPermissionsUsersReturnOk2() throws Exception{
        doReturn(null).when(permissionService).getUsersByPermissionId(Mockito.any(Integer.class));
        this.mvc.perform(get("/permission/1/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testPutEditPermissionDetailReturnOk() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(permissionNotNull);
        doReturn(permissionNotNull).when(permissionService).editPermissionDetail(Mockito.any(Permission.class));
        this.mvc.perform(put("/permission/edit/permission-detail").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testPutEditPermissionDetailReturnBadRequest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(permissionNotNull);
        doReturn(null).when(permissionService).editPermissionDetail(Mockito.any(Permission.class));
        this.mvc.perform(put("/permission/edit/permission-detail").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = {"ROLE_PERMISSION_ADMIN"})
    public void testPutEditPermissionUsersReturnBadRequest() throws Exception{
        List<User> users = new ArrayList<>();
        users.add(userNotNull);
        Iterable<User> res = users;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = mapper.writeValueAsString(res);
        doReturn(permissionNotNull).when(permissionService).editPermissionUsers(1, res);
        this.mvc.perform(put("/permission/edit/1/user").content(jsonString).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}
