package com.samsung.azasuguesthouse.member.service;

import com.samsung.azasuguesthouse.common.crypt.CryptUtil;
import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.entity.member.Salt;
import com.samsung.azasuguesthouse.member.dao.MemberDao;
import com.samsung.azasuguesthouse.member.dao.SaltDao;
import com.samsung.azasuguesthouse.member.dto.LoginInfo;
import com.samsung.azasuguesthouse.member.dto.SignupInfo;
import com.samsung.azasuguesthouse.member.exception.InvalidInputException;
import com.samsung.azasuguesthouse.member.exception.InvalidPasswordException;
import com.samsung.azasuguesthouse.member.exception.LoginUnavailableException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class MemberService {
    MemberDao memberDao;
    SaltDao saltDao;

    public MemberService(MemberDao memberDao, SaltDao saltDao) {
        this.memberDao = memberDao;
        this.saltDao = saltDao;
    }

    public Member login(LoginInfo info) {
        Member member = memberDao.selectByLoginId(info.getLoginId());
        if (member == null || member.getId() < 1 || member.getPassword() == null || member.getPassword().isBlank()) {
            throw new InvalidInputException("invalid_login_id");
        }
        if (member.getTryCount() >= 5) {
            if (member.getLastTryAt() == null) {
                throw new InvalidInputException("invalid_last_try_at");
            }
            if (LocalDateTime
                    .now(ZoneId.of("Asia/Seoul"))
                    .isBefore(member.getLastTryAt().plusSeconds(30))) {
                throw new LoginUnavailableException("try_count_exceeded");
            }
        }
        Salt salt = saltDao.selectByMemberId(member.getId());
        if (salt == null || salt.getSalt() == null || salt.getSalt().isBlank()) {
            throw new InvalidInputException("invalid_login_id");
        }
        String passwordHash = CryptUtil.sha256(info.getPassword(), salt.getSalt());
        if (!member.getPassword().equals(passwordHash)) {
            if (member.getTryCount() > 4) {
                member.setTryCount(0);
            }
            member.setTryCount(member.getTryCount() + 1);
            member.setLastTryAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            memberDao.updateTry(member);
            throw new InvalidPasswordException("invalid_password", member.getTryCount());
        }
        if (member.getTryCount() > 0) {
            member.setTryCount(0);
            memberDao.updateTry(member);
        }

        return member;
    }

    public void signup(SignupInfo info) {
        int exist = memberDao.countByLoginId(info.getLoginId());
        if (exist > 0) {
            throw new InvalidInputException("duplicate_login_id");
        }
        Member member = new Member(info);
        memberDao.insert(member);
        Salt salt = new Salt(member.getId(), UUID.randomUUID().toString());
        saltDao.insert(salt);
        String passwordHash = CryptUtil.sha256(info.getPassword(), salt.getSalt());
        member.setPassword(passwordHash);
        memberDao.updatePassword(member);
    }
}
