package com.study.ldap;

import com.study.ldap.api.Result;
import com.study.ldap.entity.Person;
import com.study.ldap.request.LoginRequest;
import com.study.ldap.service.PersonService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * LdapDemoApplicationTest
 *
 * @author fxbin
 * @version v1.0
 * @since 2019-08-26 1:06
 */
@SpringBootTest
public class LdapDemoApplicationTests {

    @Resource
    private PersonService personService;

    @Test
    public void contextLoads() {
    }

    /**
     * 测试查询单个
     */
    @Test
    public void loginTest() {
        LoginRequest loginRequest = LoginRequest.builder().username("wangwu").password("123456").build();
        Result login = personService.login(loginRequest);
        System.out.println(login);
    }

    /**
     * 测试查询列表
     */
    @Test
    public void listAllPersonTest() {
        Result result = personService.listAllPerson();
        System.out.println(result);
    }

    /**
     * 测试保存
     */
    @Test
    public void saveTest() {
        Person person = new Person();

        person.setUid("zhaosi");

        person.setSurname("赵");
        person.setGivenName("四");
        person.setUserPassword("123456");

        // required field
        person.setPersonName("赵四");
        person.setUidNumber("666");
        person.setGidNumber("666");
        person.setHomeDirectory("/home/zhaosi");
        person.setLoginShell("/bin/bash");

        personService.save(person);
    }

    /**
     * 测试删除
     */
    @Test
    public void deleteTest() {
        Person person = new Person();
        person.setUid("zhaosi");

        personService.delete(person);
    }

}
