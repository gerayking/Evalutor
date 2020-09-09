package Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pojo.submissionPojo;

@Mapper
public interface submissionMapper {
    public void addSubmission(submissionPojo submission);
    public void updateSubmission(submissionPojo submissionPojo);
    public void addAC(@Param("email") String email,@Param("cid")String cid);
}
