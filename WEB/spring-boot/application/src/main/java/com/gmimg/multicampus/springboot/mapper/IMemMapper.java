package com.gmimg.multicampus.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.gmimg.multicampus.springboot.member.Member;

@Mapper
public interface IMemMapper {
			
	@Select("select * from member where memId = #{memId} and memPw = #{memPw}")
	Member findMem(@Param("memId") String memId, @Param("memPw") String memPw);
	
	@Select("insert into member (memId, memPw) values (#{memId}, #{memPw})")
	Member insertMem(@Param("memId") String memId, @Param("memPw") String memPw);
	
	@Select("select memId from member where memId = #{memId}")
	Member idCheckMem(@Param("memId") String memId);
	
}
