package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPojo {
    private String email;
    private String username;
    private String passworld;
    private int acNum;
    private int waNum;
    private int tleNum;
    private int ceNum;
    private int reNum;
}
