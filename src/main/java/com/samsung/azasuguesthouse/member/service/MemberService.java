package com.samsung.azasuguesthouse.member.service;

import com.samsung.azasuguesthouse.common.crypt.CryptUtil;
import com.samsung.azasuguesthouse.entity.member.Member;
import com.samsung.azasuguesthouse.entity.member.Salt;
import com.samsung.azasuguesthouse.member.dao.MemberDao;
import com.samsung.azasuguesthouse.member.dao.SaltDao;
import com.samsung.azasuguesthouse.member.dto.LoginInfo;
import com.samsung.azasuguesthouse.member.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    MemberDao memberDao;
    @Autowired
    SaltDao saltDao;

    public Member login(LoginInfo info) {
        Member member = memberDao.selectByLoginId(info.getLoginId());
        if (member == null || member.getId() < 1 || member.getPassword() == null || member.getPassword().isBlank()) {
            throw new InvalidInputException("invalid_login_id");
        }
        Salt salt = saltDao.selectByMemberId(member.getId());
        if (salt == null || salt.getSalt() == null || salt.getSalt().isBlank()) {
            throw new InvalidInputException("invalid_login_id");
        }
        String passwordHash = CryptUtil.sha256(info.getPassword(), salt.getSalt());
        if (!member.getPassword().equals(passwordHash)) {
            throw new InvalidInputException("invalid_password");
        }

        return member;
    }
}
