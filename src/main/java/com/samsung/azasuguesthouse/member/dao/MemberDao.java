package com.samsung.azasuguesthouse.member.dao;

import com.samsung.azasuguesthouse.entity.member.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
    Member selectByLoginId(String loginId);
}
