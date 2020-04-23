package com.gmimg.multicampus.springboot.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import com.gmimg.multicampus.springboot.member.Item;
import com.gmimg.multicampus.springboot.member.Member;

@Mapper
public interface IMemMapper {
			
	@Select("select * from member where memId = #{memId} and memPw = #{memPw}")
	Member findMem(@Param("memId") String memId, @Param("memPw") String memPw);
	
	@Insert("insert into member (memId, memPw) values (#{memId}, #{memPw})")
	void insertMem(@Param("memId") String memId, @Param("memPw") String memPw);
	
	@Select("select memId from member where memId = #{memId}")
	Member idCheckMem(@Param("memId") String memId);
	




	@Insert("insert into item (memIdx, filename) values (#{memIdx}, #{filename})")
	Item insertItem(@Param("memId") int memIdx, @Param("filename") String filename);

	@Select("select * from item where memIdx = #{memIdx}")
	List<Item> findItem(@Param("memIdx") int memIdx);

}
