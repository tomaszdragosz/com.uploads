package com.demo.uploads.feature.file.share

import com.demo.uploads.BaseTest
import com.demo.uploads.TestSecurityUtils
import com.demo.uploads.feature.file.FileTestFactory
import com.demo.uploads.feature.user.CreateUserDto
import com.demo.uploads.feature.user.UserService
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired

import java.nio.charset.StandardCharsets

class ShareControllerTest extends BaseTest {

    @Autowired
    ShareService shareService
    @Autowired
    UserService userService

    def "Should not share unauthorized"() {
        expect:
        testApiClient.postForResponse(url: "/api/share", content: ShareTestFactory.newShareDto()).status == 401
    }

    def "Should not share not existing file"() {
        expect:
        testApiClient.postForResponse(url: "/api/share", content: ShareTestFactory.newShareDto(), auth: true).status == 404
    }

    def "Should not share file with not existing user"() {
        given:
        def fileId = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true).getContentAsString(StandardCharsets.UTF_8)

        expect:
        testApiClient.postForResponse(url: "/api/share", content: ShareTestFactory.newShareDto(fileId: fileId), auth: true).status == 400
    }

    def "Should not share not own file"() {
        given:
        def email = RandomStringUtils.randomAlphanumeric(8) + "@aa.aa"
        testApiClient.postForResponse(url: "/register", content: new CreateUserDto(
                email: email,
                password: TestSecurityUtils.PASSWORD
        ))
        def fileId = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true).getContentAsString(StandardCharsets.UTF_8)

        expect:
        testApiClient.postForResponse(url: "/api/share", content: ShareTestFactory.newShareDto(fileId: fileId, email: email), auth: true, email: "some@other.user").status == 403
    }

    def "Should share own file"() {
        given:
        def email = RandomStringUtils.randomAlphanumeric(8) + "@aa.aa"
        testApiClient.postForResponse(url: "/register", content: new CreateUserDto(
                email: email,
                password: TestSecurityUtils.PASSWORD
        ))
        def fileId = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true).getContentAsString(StandardCharsets.UTF_8)

        expect:
        testApiClient.postForResponse(url: "/api/share", content: ShareTestFactory.newShareDto(fileId: fileId, email: email), auth: true).status == 200
        shareService.findAllByUser(userService.findByEmail(email)).size() == 1
    }
}
