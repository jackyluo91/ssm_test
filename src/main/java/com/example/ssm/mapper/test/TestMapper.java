package com.example.ssm.mapper.test;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface TestMapper {
	@Insert("${sql}")
	int insert(@Param("sql") String sql);

	@Delete("${sql}")
	int delete(@Param("sql") String sql);
}
