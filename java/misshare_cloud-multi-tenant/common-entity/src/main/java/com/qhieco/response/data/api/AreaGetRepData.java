package com.qhieco.response.data.api;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/7 上午11:20
 * <p>
 * 类说明：
 *    获取地区的返回数据
 */

public class AreaGetRepData {


    /**
     * name : 广东省
     * id : 1
     * inferior : [{"name":"深圳市","id":2,"inferior":[{"name":"南山区","id":3},{"name":"福田区","id":4}]},{}]
     */

    private String name;
    private int id;
    private List<InferiorBeanX> inferior;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<InferiorBeanX> getInferior() {
        return inferior;
    }

    public void setInferior(List<InferiorBeanX> inferior) {
        this.inferior = inferior;
    }

    public static class InferiorBeanX {
        /**
         * name : 深圳市
         * id : 2
         * inferior : [{"name":"南山区","id":3},{"name":"福田区","id":4}]
         */

        private String name;
        private int id;
        private List<InferiorBean> inferior;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<InferiorBean> getInferior() {
            return inferior;
        }

        public void setInferior(List<InferiorBean> inferior) {
            this.inferior = inferior;
        }

        public static class InferiorBean {
            /**
             * name : 南山区
             * id : 3
             */

            private String name;
            private int id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
