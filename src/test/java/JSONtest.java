import Evalutor.Submission;
import com.alibaba.fastjson.JSONObject;

import java.util.Random;

public class JSONtest {
    public static void main(String[] args) {
        Submission submission = new Submission();
        submission = new Submission();
        submission.lid=2;
        submission.memoryLimit=32768;
        submission.pid="1";
        submission.cid="123sa";
        submission.code ="\n" +
                "#include <iostream>\n" +
                "#include <fcntl.h>\n" +
                "#include <string>\n" +
                "int main() {\n" +
                "    int a,b;\n" +
                "    std::cin>>a>>b;\n" +
                "    std::cout<<a+b<<\"\\n\";\n" +
                "    return 0;\n" +
                "}\n";
        submission.timeLimit =1000;
        submission.email=submission.email;
        String s = JSONObject.toJSONString(submission);
        System.out.println(s);
    }
}
