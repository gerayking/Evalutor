package Evalutor;

import Mapper.submissionMapper;
import Singleton.ContainersManager;
import Singleton.DockerClinetManager;
import Utils.MybatisUtils;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ExecCreation;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pojo.submissionPojo;

import java.io.*;
import java.util.Random;

public class RunCP implements Runnable {
    private Submission submission;
    private String uuid;

    public RunCP(Submission submission) {
        this.submission = submission;
        this.uuid =String.valueOf(System.currentTimeMillis())+String.valueOf(Math.abs(new Random().nextInt()));
    }

    @Override
    public void run() {
//        test();
        Storecode();
        String compileString = compile();
        if(!compileString.equals("1")){
            submissionPojo submissionPojo = new submissionPojo();
            submissionPojo.submitTime=submission.submitTime;
            submissionPojo.email=submission.email;
            submissionPojo.pid=submission.pid;
            submissionPojo.info=compileString;
            submissionPojo.state="CE";
            SqlSessionFactory sqlSessionFactory = MybatisUtils.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            submissionMapper mapper = sqlSession.getMapper(submissionMapper.class);
            mapper.updateSubmission(submissionPojo);
            sqlSession.commit();
            sqlSession.close();
            return ;
        }else{
            System.out.println("编译成功");
        }
        try {
          execInDocker();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (DockerException e) {
            e.printStackTrace();
        }
        // STOPSHIP: 2020/9/9
    }
    public void test(){
        this.submission = new Submission();
        this.uuid = String.valueOf(System.currentTimeMillis())+ Math.abs(new Random().nextInt());
        this.submission.lid=2;
        this.submission.memoryLimit=32768;
        this.submission.pid="1";
        this.submission.code ="\n" +
                "#include <iostream>\n" +
                "#include <fcntl.h>\n" +
                "#include <string>\n" +
                "int main() {\n" +
                "    int a,b;\n" +
                "    std::cin>>a>>b;\n" +
                "    std::cout<<a+b<<\"\\n\";\n" +
                "    return 0;\n" +
                "}\n";
        this.submission.timeLimit =1000;
        this.submission.email=submission.email;

    }
    public static String read(InputStream inputStream)  {
        String info = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine())!=null){
                info+=line+"\n";
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return info;
    }
    private static void TextToFile(final String strFilename, final String strBuffer) {
        try {
            // 创建文件对象
            File fileText = new File(strFilename);
            // 向文件写入对象写入信息
            FileWriter fileWriter = new FileWriter(fileText);

            // 写文件
            fileWriter.write(strBuffer);
            // 关闭
            fileWriter.close();
        } catch (IOException e) {
            //
            e.printStackTrace();
        }
    }
    private void Storecode(){
        if(submission.lid==1)
        TextToFile("/home/geray/resources/"+uuid+".c", submission.code);
        else if(submission.lid==2)
            TextToFile("/home/geray/resources/"+uuid+".cpp", submission.code);
        System.out.println("存储成功");
    }
    private String compile(){
        Runtime runtime = Runtime.getRuntime();
        if(submission.lid==1){
            //C
            try {
                Process process = runtime.exec("gcc -O2 -w -static -fmax-errors=3 -std=c11 /home/greay/resources/" + uuid + ".c -lm -o " + "/home/geray/code/" + uuid);
                read(process.getErrorStream());
                if(process.waitFor() != 0){
                    return read(process.getErrorStream());
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }else if(submission.lid==2){
            try {
                Process process = runtime.exec("g++ -O2 -w -static -fmax-errors=3 -std=c11 /home/geray/resources/" + uuid + ".cpp -lm -o " + "/home/geray/code/" + uuid);
                read(process.getErrorStream());
                if(process.waitFor() != 0){
                 return read(process.getErrorStream());
                }
            }catch(IOException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return "1";
    }
    private int execLocal() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String cmd = "/home/geray/桌面/judger/cmake-build-debug/parse -t 1000 -m 5120000 -p 1 "+uuid;
        System.out.println(cmd);
        Process exec = runtime.exec(cmd);
        System.out.println(read(exec.getInputStream()));
        System.out.println(read(exec.getErrorStream()));
        return 0;
    }
    private void ParseAndInsert(String str){
        submissionPojo submissionPojo = new submissionPojo();
        submissionPojo.submitTime=submission.submitTime;
        submissionPojo.email=submission.email;
        submissionPojo.pid=submission.pid;
        String[] strs = str.split("&");
        for (String s : strs) {
            if(s.indexOf("ex_code")!=-1){
                int i = Integer.parseInt(s.substring(8));
                switch (i){
                    case 1 :
                        submissionPojo.state="TLE";
                        break;
                    case 3 :
                        submissionPojo.state = "MLE";
                        break;
                    case 4:
                        submissionPojo.state="WA";
                        break;
                    case 5:
                        submissionPojo.state="AC";
                        break;
                    case 6:
                        submissionPojo.state="RE";
                        break;
                }

            }
            if(s.indexOf("timeuse")!=-1)submissionPojo.timeUsed=Integer.parseInt(s.substring(8));
            if(s.indexOf("memoryuse")!=-1)submissionPojo.memoryUsed=Integer.parseInt(s.substring(10));
        }
        SqlSessionFactory sqlSessionFactory = MybatisUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        submissionMapper mapper = sqlSession.getMapper(submissionMapper.class);
        mapper.updateSubmission(submissionPojo);
        mapper.addAC(submissionPojo.email,submission.cid);
        sqlSession.commit();
        sqlSession.close();
    }
    private int execInDocker() throws DockerException, InterruptedException {
        DockerClient dockerClient = DockerClinetManager.getDockerClient();
        Container containertarget = null;
        while(containertarget == null) containertarget = ContainersManager.getCainer();// http通讯有延迟，所以忙等待，等待docker关闭
        String runexec = "/data/parse -t " + submission.timeLimit + " -m " + submission.memoryLimit + " -p "+ submission.pid +" /code/" + uuid;
        String[] cmd = {"sh", "-c", runexec};
        ExecCreation execCreation = dockerClient.execCreate(containertarget.id(), cmd,
                DockerClient.ExecCreateParam.attachStdout(),
                DockerClient.ExecCreateParam.attachStderr());
        final LogStream output = dockerClient.execStart(execCreation.id());
        final String execOutput = output.readFully();
        dockerClient.killContainer(containertarget.id());
        System.out.println(execOutput);//输出的信息,进行解析，然后插入数据库
        ParseAndInsert(execOutput);
        return 1;
    }
    private void clean_env(){
        return ;
    }
}
