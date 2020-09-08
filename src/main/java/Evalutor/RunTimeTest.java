package Evalutor;

import java.io.*;
import java.util.UUID;

public class RunTimeTest {

    /**
     * 传入文件名以及字符串, 将字符串信息保存到文件中
     *
     * @param strFilename
     * @param strBuffer
     */
    public static void TextToFile(final String strFilename, final String strBuffer) {
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
        public static void main(String[] args) {
            Runtime rt = Runtime.getRuntime();
        try {
           Process process = rt.exec("g++ -O2 -w -static -fmax-errors=3 -std=c11 " + " -lm -o " );
            read(process.getInputStream(),System.out);
            read(process.getErrorStream(),System.err);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Exec End");
    }
    public static void read(InputStream inputStream, PrintStream out)  {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine())!=null){
                out.println(line);
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
    }
}
