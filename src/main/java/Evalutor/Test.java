package Evalutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
        Submission submission = new Submission();
        submission.lid=2;
        submission.memoLim="32768";
        submission.pid=1;
        submission.resources="#include<iostream>\n" +
                "#include<string>\n" +
                "#include<unistd.h>\n" +
                "long long a[100000005];\n" +
                "int main(){\n" +
                "    a[1]=10;\n" +
                "    for(int i=2;i<100000001;i++){\n" +
                "        a[i]=a[i-1]+2;\n" +
                "    }\n" +
                "    printf(\"%d\\n\",a[500000]);\n" +
                "    return 0;\n" +
                "}\n";
        submission.timeLim="1000";
        submission.uid=1;
        fixedThreadPool.execute(new RunCP(submission));
        submission.resources="#include<iostream>\n" +
                "#include<string>\n" +
                "#include<unistd.h>\n" +
                "long long a[100000005];\n" +
                "int main(){\n" +
                "    a[1]=10;\n" +
                "    for(int i=2;i<100000001;i++){\n" +
                "        a[i]=a[i-2]+2;\n" +
                "    }\n" +
                "    printf(\"%d\\n\",a[500000]);\n" +
                "    return 0;\n" +
                "}\n";

        fixedThreadPool.execute(new RunCP(submission));
        for(int i=0;i<10;i++){
            fixedThreadPool.execute(new RunCP(submission));
        }
    }
}
