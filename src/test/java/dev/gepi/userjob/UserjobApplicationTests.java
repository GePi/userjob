package dev.gepi.userjob;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@AutoConfigureMockMvc(printOnlyOnFailure = false)
@AutoConfigureMockMvc
class UserjobApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void itAllProcessTest() throws Exception {
        callCreate();
        callGet();
        callUpdate();
        callGetAfterUpdate();
    }

    void callCreate() throws Exception {
        var requestBuilder = post("/api/v1/create-userjob")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestContent());
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated());
    }

    void callGet() throws Exception {
        var requestBuilder = get("/api/v1/get-userjob?userId=1")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(getResponseContent()));
    }

    void callUpdate() throws Exception {
        var requestBuilder = patch("/api/v1/update-userjob")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestContent());
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(updateResponseContent()));
    }

    void callGetAfterUpdate() throws Exception {
        var requestBuilder = get("/api/v1/get-userjob?IdCompany=1")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(getAfterUpdateResponseContent()));
    }

    private String createRequestContent() {
        //TODO переделать на ObjectMapper
        return """
                {
                  "users": {
                    "userId": 1,
                    "familyName": "test_c1690f513ef0",
                    "middleName": "test_c169993c0a48",
                    "firstName": "test_96ac0e061579",
                    "birthday": "2018-01-14",
                    "gender": "test_cc2f6b4fadf1",
                    "age": 99,
                    "description": "test_f733ad29b8cb",
                    "isBlocked": false
                  },
                  "userJobInfo": {
                    "description": "test_232f0a66b7d6",
                    "isActivity": true
                  },
                  "company": {
                    "idCompany": 1,
                    "companyName": "test_dab8b73d8cdc",
                    "description": "test_dc57b9d3f26b",
                    "isActivity": true
                  }
                }
                """;
    }

    private String getResponseContent() {
        //TODO переделать на ObjectMapper
        return """
                {
                   "user": {
                    "userId": 1,
                    "familyName": "test_c1690f513ef0",
                    "middleName": "test_c169993c0a48",
                    "firstName": "test_96ac0e061579",
                    "birthday": "2018-01-14",
                    "gender": "test_cc2f6b4fadf1",
                    "age": 99,
                    "description": "test_f733ad29b8cb",
                    "isBlocked": false
                   },
                   "companies": [
                     {
                        "idCompany": 1,
                        "companyName": "test_dab8b73d8cdc",
                        "description": "test_dc57b9d3f26b",
                        "isActivity": true
                     }
                   ]
                 }
                """;
    }

    private String getAfterUpdateResponseContent() {
        //TODO переделать на ObjectMapper
        return """
                {
                    "company": {
                        "idCompany": 1,
                        "companyName": "Gulliver Inc",
                        "description": "test_dc57b9d3f26b",
                        "isActivity": true
                    },
                    "users": [
                      {
                        "userId": 1,
                        "familyName": "test_c1690f513ef0",
                        "middleName": "test_c169993c0a48",
                        "firstName": "test_96ac0e061579",
                        "birthday": "2018-01-14",
                        "gender": "test_cc2f6b4fadf1",
                        "age": 99,
                        "description": "test_f733ad29b8cb",
                        "isBlocked": false
                      }
                    ]
                  }
                """;
    }

    private String updateRequestContent() {
        //TODO переделать на ObjectMapper
        return """
                {
                  "users": {
                    "userId": 1,
                    "familyName": "test_c1690f513ef0",
                    "middleName": "test_c169993c0a48",
                    "firstName": "test_96ac0e061579",
                    "birthday": "2018-01-14",
                    "gender": "test_cc2f6b4fadf1",
                    "age": 99,
                    "description": "test_f733ad29b8cb",
                    "isBlocked": false
                  },
                  "userJobInfo": {
                    "description": "test_232f0a66b7d6",
                    "isActivity": true
                  },
                  "company": {
                    "idCompany": 1,
                    "companyName": "Gulliver Inc",
                    "description": "test_dc57b9d3f26b",
                    "isActivity": true
                  }
                }
                """;
    }

    private String updateResponseContent() {
        //TODO переделать на ObjectMapper
        return "[\"Company.companyName\"]";
    }
}
