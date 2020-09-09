package Evalutor;

import lombok.Data;

import java.util.Date;

@Data
public class Submission {
    public int lid;//language id 1-c,2-cpp
    public String pid;//program id
    public String code;// c or c++ resources
    public String email;// user id
    public Date submitTime;
    public int timeLimit;
    public String cid;
    public int memoryLimit;
}
