package com.qhieco.response.data.api;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/23 下午9:15
 * <p>
 * 类说明：
 *    用户登录返回Reponse类
 */
public class UserLoginRepData {


    /**
     * user : {"id":1,"type":1,"token":"xxxxxx"}
     */

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * id : 1
         * type : 1
         * token : xxxxxx
         */

        private int id;
        private int type;
        private String token;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
