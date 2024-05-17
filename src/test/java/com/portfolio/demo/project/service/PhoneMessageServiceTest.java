package com.portfolio.demo.project.service;

import com.portfolio.demo.project.entity.member.Member;
import com.portfolio.demo.project.model.MemberTestDataBuilder;
import com.portfolio.demo.project.dto.MemberParam;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PhoneMessageServiceTest {

    @Autowired
    private PhoneMessageService phoneMessageService;

    @Autowired
    private MemberService memberService;

    @Test
    void sendCertificationMessage() {
        // given
        Member member = MemberTestDataBuilder.user().name("이영현").phone("010-3395-5304").build();
        memberService.updateMember(MemberParam.create(member));

        // when
        phoneMessageService.sendCertificationMessage("010-3395-5304");
    }
}