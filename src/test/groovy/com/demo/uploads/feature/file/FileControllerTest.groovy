package com.demo.uploads.feature.file

import com.demo.uploads.BaseTest
import com.demo.uploads.TestSecurityUtils
import com.demo.uploads.feature.file.share.ShareTestFactory
import com.demo.uploads.feature.user.CreateUserDto
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired

import java.nio.charset.StandardCharsets

class FileControllerTest extends BaseTest {

    @Autowired
    private FileService fileService

    def "Should not get file unauthorized"() {
        expect:
        testApiClient.getForResponse(url: "/api/file/123").status == 401
    }

    def "Should not get not existing file"() {
        when:
        def response = testApiClient.getForResponse(url: "/api/file/123", auth: true)

        then:
        response.status == 404
    }

    def "Should not get not own nor shared file"() {
        given:
        def fileId = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true, email: "other@users.file").getContentAsString(StandardCharsets.UTF_8)

        expect:
        testApiClient.getForResponse(url: "/api/file/"+fileId, auth: true).status == 403
    }

    def "Should get own existing file"() {
        given:
        def fileId = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true).getContentAsString(StandardCharsets.UTF_8)

        when:
        def response = testApiClient.getForResponse(url: "/api/file/"+fileId, auth: true)

        then:
        response.status == 200
        response.getContentAsString(StandardCharsets.UTF_8) == new String(FileTestFactory.FILE_CONTENT)
    }

    def "Should get shared file"() {
        given:
        def myEmail = "my@e.mail"
        testApiClient.postForResponse(url: "/register", content: new CreateUserDto(
                email: myEmail,
                password: TestSecurityUtils.PASSWORD
        ))
        def otherUserEmail = "other@users.file"
        def sharedFileId = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true, email: otherUserEmail).getContentAsString(StandardCharsets.UTF_8)
        testApiClient.postForResponse(url: "/api/share", content: ShareTestFactory.newShareDto(fileId: sharedFileId, email: myEmail), auth: true, email: otherUserEmail)

        when:
        def response = testApiClient.getForResponse(url: "/api/file/"+sharedFileId, auth: true, email: myEmail)

        then:
        response.status == 200
        response.getContentAsString(StandardCharsets.UTF_8) == new String(FileTestFactory.FILE_CONTENT)
    }

    def "Should get list of own and shared files"() {
        given: "I upload two files"
        def myEmail = RandomStringUtils.randomAlphanumeric(8) + "my@e.mail"
        def file1Id = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true, email: myEmail).getContentAsString(StandardCharsets.UTF_8)
        def file2Id = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true, email: myEmail).getContentAsString(StandardCharsets.UTF_8)

        and: "otherUser shares his two files with me"
        def otherUserEmail = RandomStringUtils.randomAlphanumeric(8) + "@aa.aa"
        def sharedFile1Id = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true, email: otherUserEmail).getContentAsString(StandardCharsets.UTF_8)
        def sharedFile2Id = testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile(), auth: true, email: otherUserEmail).getContentAsString(StandardCharsets.UTF_8)
        testApiClient.postForResponse(url: "/api/share", content: ShareTestFactory.newShareDto(fileId: sharedFile1Id, email: myEmail), auth: true, email: otherUserEmail)
        testApiClient.postForResponse(url: "/api/share", content: ShareTestFactory.newShareDto(fileId: sharedFile2Id, email: myEmail), auth: true, email: otherUserEmail)

        when:
        def response = testApiClient.getForResponse(url: "/api/file", auth: true, email: myEmail)

        then:
        response.status == 200
        def files = testObjectMapper.fromJson(response.getContentAsString(StandardCharsets.UTF_8), MyFilesDto)
        files
        files.owned
        files.owned.size() == 2
        files.owned[0].id == file1Id
        files.owned[1].id == file2Id
        files.shared
        files.shared.size() == 2
        files.shared[0].id == sharedFile1Id
        files.shared[1].id == sharedFile2Id
    }

    def "Should get 401 for unauthorized upload"() {
        expect:
        testApiClient.uploadForResponse(content: FileTestFactory.newMultipartFile()).status == 401
    }

    def "Should upload file"() {
        given:
        def multipartFile = FileTestFactory.newMultipartFile()

        when:
        def response = testApiClient.uploadForResponse(content: multipartFile, auth: true)

        then:
        response.status == 201
        fileService.getOne(response.getContentAsString(StandardCharsets.UTF_8))
    }
}
