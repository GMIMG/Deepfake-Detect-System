package com.gmimg.multicampus.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.gmimg.multicampus.springboot.member.Item;

@Mapper
public interface IItemMapper {
	
	//view에서 받아온 변수들을 mysql에 insert해줌
	@Insert("insert into item (memIdx, filename) values (#{memIdx}, #{filename})")
	Item insertItem(@Param("memId") int memIdx, @Param("filename") String filename);
	//view에서 받아온 변수들을 mysql에서 select해줌
	@Select("select * from item where memIdx = #{memIdx}")
	List<Item> findItem(@Param("memIdx") int memIdx);
	//view에서 받아온 변수들로 mysql에 update해줌
	@Update("update item set del=1 where iditem = #{iditem}")
	void deleteItem(@Param("iditem") String iditem);
}
