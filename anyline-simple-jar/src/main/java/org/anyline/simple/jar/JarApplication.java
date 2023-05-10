package org.anyline.simple.jar;

import org.anyline.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class JarApplication {

    public static void main(String[] args) throws Exception{

        File file = new File("D:\\test.jar");
        parse(file);
    }

    private static void parse(File file) {
        if (null == file) {
            return;
        }
        String name = file.getName().toLowerCase();
        if (file.isDirectory()) {
            //目录
            File[] files = file.listFiles();
            for (File item : files) {
                parse(item);
            }
        } else {
            //文件
            if (name.endsWith(".xml")) {
                System.out.println("一级:"+file.getAbsolutePath());
            } else if (name.endsWith(".jar")) {
                //第一级jar
                try {
                    FileInputStream fin = new FileInputStream(file);
                    JarInputStream jis = new JarInputStream(fin);
                    ZipEntry entry = null;
                    while ((entry = jis.getNextEntry()) != null) {
                        //第一级jar中的文件 过滤出xml与下一级jar
                        String epath1 = entry.getName().toLowerCase();
                        String ename1 = FileUtil.getSimpleFileName(epath1);
                        if(epath1.endsWith(".xml")){
                            FilterInputStream in = new FilterInputStream(jis) {
                                public void close() throws IOException {
                                    // ignore the close
                                }
                            };
                            System.out.println("jar一级:"+epath1+":"+ename1);
                        }else if(epath1.endsWith(".jar") ){
                            //第二级jar
                            //一级jar name
                            JarInputStream jis2 = new JarInputStream(jis);
                            ZipEntry entry2 = null;
                            while ((entry2 = jis2.getNextEntry()) != null) {
                                // this is bit of a hack to avoid stream closing,
                                String epath2 = entry2.getName();
                                String ename2 = FileUtil.getSimpleFileName(epath2);
                                if(epath2.endsWith("class")){
                                    // since you can't get one for the inner entry
                                    // because you have no JarFile to get it from
                                    FilterInputStream in = new FilterInputStream(jis2) {
                                        public void close() throws IOException {
                                            // ignore the close
                                        }
                                    };
                                    System.out.println("jar二级path:"+epath1+":"+epath2);
                                    System.out.println("jar二级name:"+ename1+":"+epath2);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }
    }
    public static void parse() throws Exception{
        File file = new File("D:\\test.jar");
        FileInputStream fin = new FileInputStream(file);
        JarInputStream jin = new JarInputStream(fin);
        ZipEntry entry = null;
        while ((entry = jin.getNextEntry()) != null) {
            String name = entry.getName();
            if(name.endsWith(".xml")){
                System.out.println(name);
                FilterInputStream in = new FilterInputStream(jin) {
                    public void close() throws IOException {
                        // ignore the close
                    }
                };

                System.out.println(FileUtil.read(in).toString());
            }else if (name.endsWith(".jar")) {
                System.out.println(name);
                JarInputStream jin2 = new JarInputStream(jin);
                ZipEntry ze2 = null;
                while ((ze2 = jin2.getNextEntry()) != null) {
                    // this is bit of a hack to avoid stream closing,
                    System.out.println(ze2.getName());
                    if(!ze2.getName().endsWith("xml")){
                        continue;
                    }
                    // since you can't get one for the inner entry
                    // because you have no JarFile to get it from
                    FilterInputStream in = new FilterInputStream(jin2) {
                        public void close() throws IOException {
                            // ignore the close
                        }
                    };
                    System.out.println(FileUtil.read(in).toString());

                    // now you can process the input stream as needed
                    //JavaClass clazz = new ClassParser(in,"").parse();
                }
            }
        }
    }
}
