package com.chiahao.callbacknet.domain;

import java.util.List;

/**
 * Created by chiahao on 2015/11/22.
 */
public class Person {

    /**
     * person : [{"name":"2.0","age":"18","sex":"男"},{"name":"2.0","age":"18","sex":"男"},{"name":"2.0","age":"18","sex":"男"},{"name":"2.0","age":"18","sex":"男"},{"name":"2.0","age":"18","sex":"男"},{"name":"2.0","age":"18","sex":"男"},{"name":"2.0","age":"18","sex":"男"},{"name":"2.0","age":"18","sex":"男"}]
     */

    private List<PersonEntity> person;

    public void setPerson(List<PersonEntity> person) {
        this.person = person;
    }

    public List<PersonEntity> getPerson() {
        return person;
    }

    public static class PersonEntity {
        /**
         * name : 2.0
         * age : 18
         * sex : 男
         */

        private String name;
        private String age;
        private String sex;

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public String getAge() {
            return age;
        }

        public String getSex() {
            return sex;
        }
    }
}
