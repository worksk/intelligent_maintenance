package com.message.user_message.common;

/**
 * @Auther: wyx
 * @Date: 2019-07-26 10:57
 * @Description: 当前线程保存的信息
 */
public class RequestHolder {

    // 当前用户 ID
    private static final ThreadLocal<Integer> userIdHolder = new ThreadLocal<>();

    public static void addUserId(Integer userId){
        userIdHolder.set(userId);
    }

    public static Integer getCurrentUserId(){
        return userIdHolder.get();
    }

    public static void removeUserId(){
        userIdHolder.remove();
    }

    public static void remove(){
        removeUserId();
    }
}
