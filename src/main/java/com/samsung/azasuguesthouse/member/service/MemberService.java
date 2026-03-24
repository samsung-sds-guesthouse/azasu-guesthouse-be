package com.samsung.azasuguesthouse.member.service;

import com.samsung.azasuguesthouse.common.crypt.CryptUtil;
import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.entity.member.Salt;
import com.samsung.azasuguesthouse.member.dao.MemberDao;
import com.samsung.azasuguesthouse.member.dao.SaltDao;
import com.samsung.azasuguesthouse.member.dto.ChangePwInfo;
import com.samsung.azasuguesthouse.member.dto.LoginInfo;
import com.samsung.azasuguesthouse.member.dto.SignupInfo;
import com.samsung.azasuguesthouse.member.dto.WithdrawInfo;
import com.samsung.azasuguesthouse.member.exception.InvalidInputException;
import com.samsung.azasuguesthouse.member.exception.InvalidPasswordException;
import com.samsung.azasuguesthouse.member.exception.LoginUnavailableException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
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
        if (info == null) {
            throw new InvalidInputException("invalid_login_info");
        }
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
        log(member.getId(), "login");

        return member;
    }

    public void logout(Member member) {
        if (member == null) {
            return;
        }
        log(member.getId(), "logout");
    }

    public void signup(SignupInfo info) {
        if (info == null) {
            throw new InvalidInputException("invalid_signup_info");
        }
        duplicateId(info.getLoginId());
        Member member = new Member(info);
        memberDao.insert(member);
        Salt salt = new Salt(member.getId(), UUID.randomUUID().toString());
        saltDao.insert(salt);
        String passwordHash = CryptUtil.sha256(info.getPassword(), salt.getSalt());
        if (passwordHash == null) {
            throw new InvalidInputException("invalid_password");
        }
        member.setPassword(passwordHash);
        memberDao.updatePassword(member);
        log(member.getId(), "signup", "name=" + member.getName() + ", phone=" + member.getPhone());
    }

    public void withdraw(Member member, WithdrawInfo info) {
        if (member == null || info == null) {
            throw new InvalidInputException("invalid_withdraw_info");
        }
        Salt salt = saltDao.selectByMemberId(member.getId());
        if (salt == null || salt.getSalt() == null || salt.getSalt().isBlank()) {
            return;
        }
        String passwordHash = CryptUtil.sha256(info.getPassword(), salt.getSalt());
        if (!member.getPassword().equals(passwordHash)) {
            throw new InvalidInputException("invalid_password");
        }
        saltDao.deleteByMemberId(member.getId());
        memberDao.deleteById(member.getId());
        log(member.getId(), "withdraw");
    }

    public void duplicateId(String loginId) {
        if (loginId == null || loginId.length() < 8 || loginId.length() > 15) {
            throw new InvalidInputException("invalid_login_id");
        }
        int exist = memberDao.countByLoginId(loginId);
        if (exist > 0) {
            throw new InvalidInputException("duplicate_login_id");
        }
    }

    public void changePw(Member member, ChangePwInfo info) {
        if (member == null || info == null) {
            throw new InvalidInputException("invalid_change_pw_info");
        }
        Salt salt = saltDao.selectByMemberId(member.getId());
        if (salt == null || salt.getSalt() == null || salt.getSalt().isBlank()) {
            return;
        }
        String oldPasswordHash = CryptUtil.sha256(info.getOldPassword(), salt.getSalt());
        if (!member.getPassword().equals(oldPasswordHash)) {
            throw new InvalidInputException("invalid_old_password");
        }
        String newPasswordHash = CryptUtil.sha256(info.getNewPassword(), salt.getSalt());
        if (newPasswordHash == null) {
            throw new InvalidInputException("invalid_new_password");
        }
        if (oldPasswordHash.equals(newPasswordHash)) {
            throw new InvalidInputException("same_password");
        }
        salt = new Salt(member.getId(), UUID.randomUUID().toString());
        saltDao.insert(salt);
        newPasswordHash = CryptUtil.sha256(info.getNewPassword(), salt.getSalt());
        member.setPassword(newPasswordHash);
        memberDao.updatePassword(member);
        log(member.getId(), "change_pw");
    }

    // log
    private void log(long memberId, String type) {
        memberDao.log(Map.of(
                "member_id", memberId,
                "type", type
        ));
    }

    private void log(long memberId, String type, String extra) {
        memberDao.log(Map.of(
                "member_id", memberId,
                "type", type,
                "extra", extra
        ));
    }
}
