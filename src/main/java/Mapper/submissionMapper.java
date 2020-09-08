package Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import pojo.submissionPojo;

@Mapper
public interface submissionMapper {
    public void addSubmission(submissionPojo submission);
}
