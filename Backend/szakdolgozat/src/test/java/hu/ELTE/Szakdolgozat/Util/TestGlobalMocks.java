package hu.ELTE.Szakdolgozat.Util;

import hu.ELTE.Szakdolgozat.Entity.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TestGlobalMocks {

    public static Activity activityNotNull;
    public static Activity activityNotNull2;
    public static Activity activityNotNull3;

    public static Activity task;
    public static Activity task2;
    public static Activity task3;

    public static User userNotNull;
    public static User userNotNull2;

    public static ActivityGroup activityGroupNotNull;
    public static ActivityGroup activityGroupNotNull2;
    public static ActivityGroup activityGroupNotNull3;

    public static ActivityGroup activityGroupNoEasyLoginNotNull;
    public static ActivityGroup activityGroupNoEasyLoginNotNull2;

    public static WorkGroup workGroupNotNull;
    public static WorkGroup workGroupNotNull2;

    public static Permission permissionNotNull;
    public static Permission permissionNotNull2;

    public static PermissionDetail permissionDetailNotNull;
    public static PermissionDetail permissionDetailNotNull2;

    public static Holiday holidayNotNull;
    public static Holiday holidayNotNull2;

    public static UserWorkGroup userWorkGroupNotNull;
    public static UserWorkGroup userWorkGroupNotNull2;

    private static List<ActivityGroup> listActivityGroup = new ArrayList<>();

    static {
        fill();
    }

    public static void fill(){
        permissionNotNull = maketTestPermission(1, "Adminisztráció");
        permissionNotNull2 = maketTestPermission(2, "Alkalmazott");

        permissionDetailNotNull = makeTestPermissionDetail(1, "Felhasználó", "ROLE_USER");
        permissionDetailNotNull2 = makeTestPermissionDetail(2, "Karbantartás", "ROLE_MAINTENANCE");

        workGroupNotNull = makeTestWorkGroup(1, "Fejlesztés", 100, false);
        workGroupNotNull2 = makeTestWorkGroup(2, "Tervezés", 100, false);

        activityGroupNotNull = makeTestActivityGroup(1, "Tervezés", null, true, false, false);
        activityGroupNotNull2 = makeTestActivityGroup(2, "Tervezés", null, true, false, false);
        activityGroupNotNull3 = makeTestActivityGroup(3, "Tervezés2", null, true, false, false);

        activityGroupNoEasyLoginNotNull = makeTestActivityGroup(3, "Munka kezdése", null, false, true, false);
        activityGroupNoEasyLoginNotNull2 = makeTestActivityGroup(3, "Munka kezdése", null, false, true, false);

        userNotNull = makeTestUser(1, "Roland", "Kotroczó", "k.roli9955@gmail.com", "admin", "admin", new java.util.Date(), 20, true, false, permissionNotNull, new ArrayList<>());
        userNotNull2 = makeTestUser(2, "Pista", "Kiss", "teszt@gmail.com", "admin", "admin", new java.util.Date(), 25, true, false, permissionNotNull2, new ArrayList<>());

        activityNotNull = makeTestActivity(1, "Teszt üzenet", false, 90, Date.valueOf("2020-04-25"), userNotNull, userNotNull, activityGroupNotNull, workGroupNotNull, false, null);
        activityNotNull2 = makeTestActivity(2, "Teszt üzenet2", false, 90, Date.valueOf("2020-04-25"), userNotNull, userNotNull, activityGroupNotNull, workGroupNotNull, false, null);
        activityNotNull3 = makeTestActivity(3, "Teszt üzenet3", false, 90, Date.valueOf("2020-04-20"), userNotNull, userNotNull, activityGroupNotNull, workGroupNotNull, false, null);

        task = makeTestActivity(1, "Teszt üzenet", true, null, Date.valueOf("2020-04-25"), userNotNull, userNotNull, null, null, false, Date.valueOf("2020-04-25"));
        task2 = makeTestActivity(2, "Teszt üzenet2", true, null, Date.valueOf("2020-04-25"), userNotNull, userNotNull, null, null, true, Date.valueOf("2020-04-25"));
        task3 = makeTestActivity(3, "Teszt üzenet3", true, null, Date.valueOf("2020-04-22"), userNotNull2, userNotNull2, null, null, false, Date.valueOf("2020-04-22"));

        holidayNotNull = makeTestHoliday(1, Date.valueOf("2020-04-20"), Date.valueOf("2020-04-25"), 6, userNotNull, false);
        holidayNotNull2 = makeTestHoliday(2, Date.valueOf("2020-05-20"), Date.valueOf("2020-05-25"), 6, userNotNull, false);

        userWorkGroupNotNull = makeTestUserWorkGroup(1, Date.valueOf("2020-03-01"), Date.valueOf("2020-04-30"), workGroupNotNull, userNotNull);
        userWorkGroupNotNull2 = makeTestUserWorkGroup(2, Date.valueOf("2020-03-01"), Date.valueOf("2020-04-30"), workGroupNotNull, userNotNull2);

        listActivityGroup.add(activityGroupNotNull);
        listActivityGroup.add(activityGroupNotNull2);

        setActivityWorkGroup(workGroupNotNull, listActivityGroup);
        setAllPermissionDetails(userNotNull);
    }

    private static Activity makeTestActivity(Integer id, String description, Boolean isTask, Integer min, Date date, User user, User owner, ActivityGroup activityGroup, WorkGroup workGroup, Boolean isCompleted, Date deadline){
        Activity activity = new Activity();

        activity.setId(id);
        activity.setDescription(description);
        activity.setIsTask(isTask);
        activity.setMin(min);
        activity.setDate(date);
        activity.setUser(user);
        activity.setOwner(owner);
        activity.setActivityGroup(activityGroup);
        activity.setWorkGroup(workGroup);
        activity.setIsCompleted(isCompleted);
        activity.setDeadline(deadline);

        return activity;
    }

    private static User makeTestUser(Integer id, String firstName, String lastName, String email, String loginName, String password, java.util.Date lastLogin, Integer maxHoliday, Boolean canLogIn, Boolean deleted, Permission permission, List<Holiday> holidays){
        User user = new User();

        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setLoginName(loginName);
        user.setPassword(password);
        user.setLastLogin(lastLogin);
        user.setMaxHoliday(maxHoliday);
        user.setCanLogIn(canLogIn);
        user.setDeleted(deleted);
        user.setPermission(permission);
        user.setHolidays(holidays);

        return user;
    }

    private static ActivityGroup makeTestActivityGroup(Integer id, String name, ActivityGroup parent, Boolean canAddChild, Boolean easyLogIn, Boolean deleted){
        ActivityGroup activityGroup = new ActivityGroup();

        activityGroup.setId(id);
        activityGroup.setName(name);
        activityGroup.setParent(parent);
        activityGroup.setCanAddChild(canAddChild);
        activityGroup.setEasyLogIn(easyLogIn);
        activityGroup.setDeleted(deleted);

        return activityGroup;
    }

    private static WorkGroup makeTestWorkGroup(Integer id, String name, Integer scale, Boolean deleted){
        WorkGroup workGroup = new WorkGroup();

        workGroup.setId(id);
        workGroup.setName(name);
        workGroup.setScale(scale);
        workGroup.setDeleted(deleted);

        return workGroup;
    }

    private static void setActivityWorkGroup(WorkGroup workGroup, List<ActivityGroup> lActivityGroup){
        workGroup.setActivityGroup(lActivityGroup);
    }

    private static Permission maketTestPermission(Integer id, String name){
        Permission permission = new Permission();

        permission.setId(id);
        permission.setName(name);

        return permission;
    }


    private static Holiday makeTestHoliday(Integer id, Date from, Date to, Integer days, User user, Boolean deleted){
        Holiday holiday = new Holiday();

        holiday.setId(id);
        holiday.setHolidayFrom(from);
        holiday.setHolidayTo(to);
        holiday.setDays(days);
        holiday.setUser(user);
        holiday.setDeleted(deleted);

        return holiday;
    }

    private static PermissionDetail makeTestPermissionDetail(Integer id, String name, String roleTag){
        PermissionDetail permissionDetail = new PermissionDetail();

        permissionDetail.setId(id);
        permissionDetail.setName(name);
        permissionDetail.setRoleTag(roleTag);

        return permissionDetail;
    }

    private static  UserWorkGroup makeTestUserWorkGroup(Integer id, Date from, Date to, WorkGroup workGroup, User user){
        UserWorkGroup userWorkGroup = new UserWorkGroup();

        userWorkGroup.setId(id);
        userWorkGroup.setInWorkGroupFrom(from);
        userWorkGroup.setInWorkGroupTo(to);
        userWorkGroup.setWorkGroup(workGroup);
        userWorkGroup.setUser(user);

        return userWorkGroup;
    }

    private static void setAllPermissionDetails(User user){
        List<PermissionDetail> details = new ArrayList<>();
        PermissionDetail pd1 = new PermissionDetail();
        pd1.setId(1);
        pd1.setName("");
        pd1.setRoleTag("ROLE_ADD_TASK");
        details.add(pd1);

        PermissionDetail pd2 = new PermissionDetail();
        pd2.setId(2);
        pd2.setName("");
        pd2.setRoleTag("ROLE_HOLIDAY_ADMIN");
        details.add(pd2);

        PermissionDetail pd3 = new PermissionDetail();
        pd3.setId(3);
        pd3.setName("");
        pd3.setRoleTag("ROLE_USER_ADMIN");
        details.add(pd3);

        PermissionDetail pd4 = new PermissionDetail();
        pd4.setId(4);
        pd4.setName("");
        pd4.setRoleTag("ROLE_PROJECT_ADMIN");
        details.add(pd4);

        PermissionDetail pd5 = new PermissionDetail();
        pd5.setId(5);
        pd5.setName("");
        pd5.setRoleTag("ROLE_ACTIVITY_GROUP_ADMIN");
        details.add(pd5);

        PermissionDetail pd6 = new PermissionDetail();
        pd6.setId(6);
        pd6.setName("");
        pd6.setRoleTag("ROLE_PERMISSION_ADMIN");
        details.add(pd6);

        PermissionDetail pd7 = new PermissionDetail();
        pd7.setId(7);
        pd7.setName("");
        pd7.setRoleTag("ROLE_LISTING");
        details.add(pd7);


        Permission p = new Permission();
        p.setDetails(details);
        p.setName("All");
        user.setPermission(p);
    }
}
