package com.back.review.service;

import com.back.review.domain.member.MemberEntity;
import com.back.review.domain.member.MemberQueryRepository;
import com.back.review.domain.member.MemberRepository;
import com.back.review.dto.MemberDto;
import com.back.review.enumclass.Role;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.back.review.conf.AppConfig.localDateTimeToString;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;

    @Transactional(rollbackFor = Exception.class)
    public Long signUp(MemberDto memberDto) { // 회원가입

        MemberEntity memberEntity = memberDto.toEntity();
        memberRepository.save(memberEntity);

        return memberEntity.getId();
    }

    // 이메일 중복 체크 함수
    public Boolean checkEmail(String username) {
        return memberRepository.existsByUsername(username);
    }

    // 닉네임 중복 체크 함수
    public Boolean checkNickname(String id, String nickname, String view) throws SQLException {
        boolean result;

        if (view.equals("myInfo")) {
            result = memberQueryRepository.findByNickname(Long.parseLong(id)).getResults().contains(nickname);
        } else {
            result = memberRepository.existsByNickname(nickname);
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateMember(MemberDto memberDto) { // 회원 정보 update

        MemberEntity member = memberRepository.findByUsername(memberDto.getUsername())
                .orElse(null);

        if (memberDto.getPassword().isEmpty()) { // 비밀번호를 수정하지 않은 경우
            memberDto.setPassword(member.getPassword());
        }
        memberDto.setId(member.getId());
        memberDto.setRole(member.getRole() == Role.ADMIN ? Role.ADMIN.getValue() : Role.MEMBER.getValue());
        memberDto.setRegDate(localDateTimeToString(member.getRegDate()));

        MemberEntity updateMemberEntity = memberDto.toEntity();

        memberRepository.save(updateMemberEntity);

        return updateMemberEntity.getId();
    }

    public MemberDto getByUsername(String username) throws SQLException { // 이름으로 회원정보 get

        Optional<MemberEntity> memberEntity = memberRepository.findByUsername(username);
        if (memberEntity.isPresent()) {
            return memberEntity.get().toDto();
        } else {
            throw new SQLException();
        }
    }

    public MemberDto findById(Long id) throws SQLException {
        Optional<MemberEntity> memberEntity = memberRepository.findById(id);
        if (memberEntity.isPresent()) {
            return memberEntity.get().toDto();
        } else {
            throw new SQLException();
        }
    }

    // 모든 ROLE_MEMBER 정보 가져오기
    public HashMap<String, Object> getMemberPagination(Pageable pageable) throws Exception {
        Page<MemberEntity> memberEntities = memberQueryRepository.findAllExceptAdmin(pageable);
        List<MemberDto> memberDtoList = new ArrayList<>();

        for (MemberEntity member : memberEntities.getContent()) {
            memberDtoList.add(member.toDto());
        }

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("memberDtoList", memberDtoList);
        dataMap.put("totalCount", memberEntities.getTotalElements());

        return dataMap;
    }
}