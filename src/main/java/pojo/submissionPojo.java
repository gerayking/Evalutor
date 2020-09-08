package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class submissionPojo {
    public String email;
    public String pid;
    public Timestamp submitTime;
    public String state;
    public int normalSubmit;
    public int timeUsed;
    public int memoryUsed;
    public String info;
}
