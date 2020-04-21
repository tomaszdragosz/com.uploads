package com.demo.uploads.feature.user

import com.demo.uploads.BaseTest
import com.demo.uploads.TestSecurityUtils
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired

import java.nio.charset.StandardCharsets

class UserControllerTest extends BaseTest {

    private static final EMAIL = "Mail@mail.com"

    @Autowired
    private UserRepository repository

    def "Should register new user"() {
        given: "create user"
        def createUserBaseDto = new CreateUserDto(
                        email: RandomStringUtils.randomAlphanumeric(8) + EMAIL,
                        password: TestSecurityUtils.PASSWORD
                )

        when:
        def response = testApiClient.postForResponse(url: "/register", content: createUserBaseDto)

        then:
        response.status == 201
        Long userId = Long.valueOf(response.getContentAsString(StandardCharsets.UTF_8))
        repository.getOne(userId)

        expect: "cannot register same user again"
        testApiClient.postForResponse(url: "/register", content: createUserBaseDto).status == 400
    }
}
