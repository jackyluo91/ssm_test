package cache;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ssm.service.test.CacheTestService;

@RunWith(SpringJUnit4ClassRunner.class) // 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = { "classpath:spring/spring-cache.xml", "classpath:spring/spring-mvc.xml",
		"classpath:spring/spring-mybatis.xml" })
/**
 * 测试spring整合guava cache方式
 * 
 * @author Peng
 * @Date2016年12月11日上午11:52:56
 */
public class CacheTest {

	@Resource
	private CacheTestService cachceTest;

	/**
	 * 测试查询
	 */
	@Test
	public void test1() {
		int i = cachceTest.get();
		int k = cachceTest.getDefault();
		Assert.assertTrue(i == 0);
		Assert.assertTrue(k == 0);
	}

	@Test
	public void test2() {
		cachceTest.add();
		int i = cachceTest.getDefault();
		Assert.assertTrue(i == 0);
		try {
			Thread.sleep(2000);
			int k = cachceTest.get();
			i = cachceTest.getDefault();
			Assert.assertTrue(k == 1);
			Assert.assertTrue(i == 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
