package com.curator.common.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.digest.MD5;

/**
 * security 工具
 *
 * @author Jun
 * @date 2020/9/27
 */
public class SecurityUtil {

    /**
     * 密码加密
     *
     * @param password 原始密码
     * @param salt     盐
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, String salt) {
        String builder = salt + password + salt;
        return new MD5(salt.getBytes()).digestHex(builder);
    }

    public static void main(String[] args) {

        String pattern = "/auth/.*";
        System.out.println(ReUtil.isMatch(pattern,"/auth/login"));
        System.out.println("bASKZ53wE23xtVSURpgEJzQvqZtR9hQY9PxruEQcrU2blacwrx".length());
        System.out.println(RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER + RandomUtil.BASE_CHAR.toUpperCase(),50));
    }
}
