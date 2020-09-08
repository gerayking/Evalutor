package Utils;

import Mapper.UserMapper;
import Mapper.submissionMapper;
import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pojo.UserPojo;
import pojo.submissionPojo;

import java.sql.Timestamp;
import java.util.Date;

public class MybatisTest {
    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = MybatisUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        submissionMapper mapper = sqlSession.getMapper(submissionMapper.class);
//        UserPojo test = mapper.getUser("test");
//        System.out.println(test);
        submissionPojo submissionPojo = new submissionPojo();
        submissionPojo.email="test";
        submissionPojo.submitTime = new Timestamp(System.currentTimeMillis());
        submissionPojo.timeUsed=1000;
        submissionPojo.state="1";
        submissionPojo.info="Accept";
        submissionPojo.memoryUsed=10000;
        submissionPojo.normalSubmit=0;
        submissionPojo.pid="1";
        mapper.addSubmission(submissionPojo);
        sqlSession.commit();
        sqlSession.close();
        System.out.println("插入成功");
    }
}
