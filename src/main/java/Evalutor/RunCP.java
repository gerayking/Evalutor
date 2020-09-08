package Evalutor;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.Image;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class RunCP implements Runnable {
    private Submission submission;
    private String uuid;

    public RunCP(Submission submission) {
        this.submission = submission;
        this.uuid =String.valueOf(System.currentTimeMillis());
    }

    public RunCP() {

    }

    @Override
    public void run() {
        test();
        Storecode();
        if(compile()==0){
            // 编译失败
            return ;
        }else{
            System.out.println("编译成功");
        }
        try {
            execLocal();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test(){
        this.submission = new Submission();
        this.uuid = String.valueOf(System.currentTimeMillis());
        this.submission.lid=2;
        this.submission.memoLim="32768";
        this.submission.pid=1;
        this.submission.resources="#include<iostream>\n" +
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
        this.submission.timeLim="1000";
        this.submission.uid=1;

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
    private void init_env(){}
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
        TextToFile("/home/geray/resources/"+uuid+".c", submission.resources);
        else if(submission.lid==2)
            TextToFile("/home/geray/resources/"+uuid+".cpp", submission.resources);

    }
    private int compile(){
        Runtime runtime = Runtime.getRuntime();
        if(submission.lid==1){
            //C
            try {
                Process process = runtime.exec("gcc -O2 -w -static -fmax-errors=3 -std=c11 /home/greay/resources/" + uuid + ".c -lm -o " + "/home/geray/code/" + uuid);
                read(process.getErrorStream());
                if(process.waitFor() != 0){
                    // 编译错误
                    // 插入数据库
                    return 0;
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
                 // 编译错误
                 // 插入数据库
                    return 0;
                }
            }catch(IOException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return 1;
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
    private int execInDocker() throws DockerException, InterruptedException {
        DockerClient dockerClient = DockerClinetManager.getDockerClient();
        Container containertarget = null;
        synchronized (this){
                List<Image> images = dockerClient.listImages(DockerClient.ListImagesParam.allImages());
            List<Container> containers = dockerClient.listContainers(DockerClient.ListContainersParam.allContainers());
            synchronized (this) {
                for (Container container : containers) {
                    if(container.state().equals("running"))continue;
                    containertarget=container;
                    dockerClient.startContainer(containertarget.id());
                    break;
                }
            }
        }
        String runexec = "./evaluator -t "+submission.timeLim+" -m "+submission.memoLim+"/code/"+uuid;
        String[]cmd = {"sh","-c",runexec};
        ExecCreation execCreation = dockerClient.execCreate(containertarget.id(), cmd,
                DockerClient.ExecCreateParam.attachStdout(),
                DockerClient.ExecCreateParam.attachStderr());
        final LogStream output = dockerClient.execStart(execCreation.id());
        final String execOutput = output.readFully();
        System.out.println(execOutput);//输出的信息,想用json来处理
        dockerClient.killContainer(containertarget.id());
        return 1;
    }
    private void clean_env(){
        return ;
    }
}
