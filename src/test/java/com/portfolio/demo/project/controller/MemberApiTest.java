package com.portfolio.demo.project.controller;

import com.google.gson.JsonObject;
import com.portfolio.demo.project.entity.member.Member;
import com.portfolio.demo.project.model.MemberTestDataBuilder;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ResourceBundle;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MemberApiTest {

    @Autowired
    private MockMvc mockMvc;

    private String asJsonString(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Test @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 관리자_등록() throws Exception {
        Member admin = MemberTestDataBuilder.admin().build();
        // `mockMvc.perform()` return type : ResultActions
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(admin))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.memNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.regDate").exists());
    }

    @Test
    void 일반_회원_가입() throws Exception {
        Member user = MemberTestDataBuilder.randomIdentifierUser()
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/member")
                                .content(asJsonString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.memNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.regDate").exists());
    }

    @Test
    void 소셜_회원_가입() throws Exception {
        Member user = MemberTestDataBuilder.naverUser()
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/member")
                                .content(asJsonString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.memNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.regDate").exists());
    }

    @Test
    void 일반_회원_조회() throws Exception {
        Member user = MemberTestDataBuilder.randomIdentifierUser().build();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/member")
                                .content(asJsonString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String resultStr = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        Member createdMember = objectMapper.readValue(resultStr, Member.class);;

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/member/" + createdMember.getMemNo())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.memNo").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.memNo").value(createdMember.getMemNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.regDate").exists());
    }

    @Test
    void 인증번호_메세지_전송() throws Exception {
        ResourceBundle resource = ResourceBundle.getBundle("Res_ko_KR_keys");
        String samplePhoneNumber = resource.getString("aws.sample.phone");

        JsonObject obj = new JsonObject();
        obj.addProperty("phone", samplePhoneNumber);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/cert-message")
                        .content(obj.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        String resultStr = result.getResponse().getContentAsString();
        System.out.println(resultStr);
    }
}
