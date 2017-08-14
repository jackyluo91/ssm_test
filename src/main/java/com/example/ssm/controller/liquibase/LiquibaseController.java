package com.example.ssm.controller.liquibase;

import java.io.IOException;
import java.sql.Connection;

import javax.annotation.Resource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ssm.controller.BaseController;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.resource.ClassLoaderResourceAccessor;

@Controller
@RequestMapping("/liquibase")
public class LiquibaseController extends BaseController {
	@Resource(name = "dataSource")
	BasicDataSource dataSource;

	@RequestMapping("/update")
	@ResponseBody
	public void update(Boolean action, String context) {
		Liquibase lb = getLiquibase();
		if (StringUtils.isEmpty(context)) {
			context = "prod";
		}
		try {
			if (BooleanUtils.isTrue(action)) {
				lb.update(context);
			} else {
				lb.update(context, getResponse().getWriter());
			}
			lb.getDatabase().close();
		} catch (LiquibaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ok("Database Updated");
	}

	@RequestMapping("/reset")
	@ResponseBody
	public void reset(Boolean action, String context) {
		Liquibase lb = getLiquibase();
		if (StringUtils.isEmpty(context)) {
			context = "prod";
		}
		try {
			if (BooleanUtils.isTrue(action)) {
				lb.dropAll();
				lb.update(context);
				ok("Database Reset");
			} else {
				ok("Database Not Reset");
			}
			lb.getDatabase().close();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (LockException e) {
			e.printStackTrace();
		} catch (LiquibaseException e) {
			e.printStackTrace();
		}
	}

	public Liquibase getLiquibase() {
		try {
			Connection conn = dataSource.getConnection();
			Liquibase lb = new Liquibase("liquibase/master.xml", new ClassLoaderResourceAccessor(),
					new JdbcConnection(conn));
			return lb;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
