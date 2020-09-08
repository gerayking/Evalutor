package Utils;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils{
    public static final SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
    public static SqlSessionFactory getSqlSessionFactory() {
        if(sqlSessionFactory == null){
            String resource = "mybatis-config.xml";
            try {
                InputStream inputStream = Resources.getResourceAsStream(resource);
                SqlSessionFactory sqlFactory = new SqlSessionFactoryBuilder().build(inputStream);
                return sqlFactory;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return sqlSessionFactory;
    }
}
