package mybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ssm.mapper.test.TestMapper;
import com.example.ssm.util.UUIDGenerator;

@RunWith(SpringJUnit4ClassRunner.class) // 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = { "classpath:spring/spring-cache.xml", "classpath:spring/spring-mvc.xml",
"classpath:spring/spring-mybatis.xml" })
public class MyBatisTest {
	
	@Autowired
	TestMapper mapper;
	
	@Test
	public void test() {
		String tableName = UUIDGenerator.generateShortUuid();
		mapper.insert("CREATE TABLE " + tableName + " (id int)");
		mapper.delete("DROP TABLE " + tableName);
	}
}
