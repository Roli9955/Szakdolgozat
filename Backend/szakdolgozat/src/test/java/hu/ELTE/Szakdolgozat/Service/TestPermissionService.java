package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Class.Result;
import hu.ELTE.Szakdolgozat.Entity.Permission;
import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.PermissionDetailRepository;
import hu.ELTE.Szakdolgozat.Repository.PermissionRepository;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import hu.ELTE.Szakdolgozat.Util.TestGlobalMocks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TestPermissionService {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionDetailRepository permissionDetailRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PermissionService permissionService;

    private User userNotNull;
    private User userNotNull2;

    private Permission permissionNotNull;
    private Permission permissionNotNull2;

    private PermissionDetail permissionDetailNotNull;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        userNotNull = TestGlobalMocks.userNotNull;
        userNotNull2 = TestGlobalMocks.userNotNull2;

        permissionNotNull = TestGlobalMocks.permissionNotNull;
        permissionNotNull2 = TestGlobalMocks.permissionNotNull2;

        permissionDetailNotNull = TestGlobalMocks.permissionDetailNotNull;
    }

    @Test
    public void testGetAllPermission(){
        TestGlobalMocks.fill();

        List<Permission> list = new ArrayList<>();
        list.add(permissionNotNull);
        list.add(permissionNotNull2);
        Mockito.when(permissionRepository.findAll()).thenReturn(list);

        permissionNotNull.setUsers(new ArrayList<User>());
        permissionNotNull2.setUsers(new ArrayList<User>());

        Iterable<Permission> res = permissionService.getAllPermission();

        assertEquals(list, res);
    }

    @Test
    public void testAddNewPermission(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.of(permissionNotNull);
        Mockito.when(permissionRepository.findByName(permissionNotNull.getName())).thenReturn(oPermission);

        Permission res = permissionService.addNewPermission(permissionNotNull);

        assertEquals(null, res);
    }

    @Test
    public void testAddNewPermission2(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.empty();
        Mockito.when(permissionRepository.findByName(permissionNotNull.getName())).thenReturn(oPermission);
        Mockito.when(permissionRepository.save(permissionNotNull)).thenReturn(permissionNotNull);

        Permission res = permissionService.addNewPermission(permissionNotNull);

        assertEquals(permissionNotNull, res);
    }

    @Test
    public void testDeletePermission(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.empty();
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);

        Permission res = permissionService.deletePermission(permissionNotNull.getId());

        assertEquals(null, res);
    }

    @Test
    public void testDeletePermission2(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.of(permissionNotNull);
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);

        permissionNotNull.setUsers(new ArrayList<User>());

        Permission res = permissionService.deletePermission(permissionNotNull.getId());

        assertEquals(permissionNotNull, res);
    }

    @Test
    public void testEditPermission(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.empty();
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);

        Result<Permission, String> fvres = permissionService.editPermission(permissionNotNull);
        Result<Permission, String> res = new Result<>(null, "Hiányzó jogosultsági csoport");

        assertEquals(res.getRes1(), fvres.getRes1());
        assertEquals(res.getRes2(), fvres.getRes2());
    }

    @Test
    public void testEditPermission2(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.of(permissionNotNull);
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);
        Optional<Permission> oPermissionNameCheck = Optional.of(permissionNotNull);
        Mockito.when(permissionRepository.findByName(permissionNotNull.getName())).thenReturn(oPermissionNameCheck);

        Result<Permission, String> fvres = permissionService.editPermission(permissionNotNull);
        Result<Permission, String> res = new Result<>(null, "Ilyen névvel már van jogosultsági csoport");

        assertEquals(res.getRes1(), fvres.getRes1());
        assertEquals(res.getRes2(), fvres.getRes2());
    }

    @Test
    public void testEditPermission3(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.of(permissionNotNull);
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);
        Optional<Permission> oPermissionNameCheck = Optional.empty();
        Mockito.when(permissionRepository.findByName(permissionNotNull.getName())).thenReturn(oPermissionNameCheck);
        Mockito.when(permissionRepository.save(permissionNotNull)).thenReturn(permissionNotNull);

        Result<Permission, String> fvres = permissionService.editPermission(permissionNotNull);
        Result<Permission, String> res = new Result<>(permissionNotNull, "");

        assertEquals(res.getRes1(), fvres.getRes1());
        assertEquals(res.getRes2(), fvres.getRes2());
    }

    @Test
    public void testGetUsersByPermissionId(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.empty();
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);

        List<User> res = permissionService.getUsersByPermissionId(permissionNotNull.getId());

        assertEquals(null, res);
    }

    @Test
    public void testGetUsersByPermissionId2(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.of(permissionNotNull);
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);

        permissionNotNull.setUsers(new ArrayList<User>());

        List<User> fvRes = permissionService.getUsersByPermissionId(permissionNotNull.getId());
        List<User> res = new ArrayList<>();

        assertEquals(res, fvRes);
    }

    @Test
    public void testEditPermissionDetail(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.empty();
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);

        Permission res = permissionService.editPermissionDetail(permissionNotNull);

        assertEquals(null, res);
    }

    @Test
    public void testEditPermissionDetail2(){
        TestGlobalMocks.fill();

        permissionNotNull.setDetails(new ArrayList<>());
        Optional<Permission> oPermission = Optional.of(permissionNotNull);
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);
        Mockito.when(permissionRepository.save(permissionNotNull)).thenReturn(permissionNotNull);

        Permission res = permissionService.editPermissionDetail(permissionNotNull);

        assertEquals(permissionNotNull, res);
    }

    @Test
    public void testEditPermissionUsers(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.empty();
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);

        List<User> users = new ArrayList<>();
        users.add(userNotNull);
        users.add(userNotNull2);

        Permission res = permissionService.editPermissionUsers(permissionNotNull.getId(), users);

        assertEquals(null, res);
    }

    @Test
    public void testEditPermissionUsers2(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.of(permissionNotNull);
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);

        List<User> users = new ArrayList<>();
        users.add(userNotNull);
        Mockito.when(userRepository.findByPermissionAndDeletedFalse(permissionNotNull)).thenReturn(users);
        Optional<User> oUser = Optional.empty();
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        Permission res = permissionService.editPermissionUsers(permissionNotNull.getId(), users);

        assertEquals(null, res);
    }

    @Test
    public void testEditPermissionUsers3(){
        TestGlobalMocks.fill();

        Optional<Permission> oPermission = Optional.of(permissionNotNull);
        Mockito.when(permissionRepository.findById(permissionNotNull.getId())).thenReturn(oPermission);

        List<User> users = new ArrayList<>();
        users.add(userNotNull);
        Mockito.when(userRepository.findByPermissionAndDeletedFalse(permissionNotNull)).thenReturn(users);
        Optional<User> oUser = Optional.of(userNotNull);
        Mockito.when(userRepository.findById(userNotNull.getId())).thenReturn(oUser);

        Permission res = permissionService.editPermissionUsers(permissionNotNull.getId(), users);

        assertEquals(permissionNotNull, res);
    }

}
