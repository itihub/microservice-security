package xyz.itihub.security.util;

import xyz.itihub.security.user.User;

public class ThreadLocalUtils {

    private static final ThreadLocal<User> currentUser = ThreadLocal.withInitial(() -> null);


    public static void set(User user){
        currentUser.set(user);
    }

    public static User get(){
        return currentUser.get();
    }

    public static void remove(){
        currentUser.remove();
    }

}
