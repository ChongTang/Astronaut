package edu.virginia.cs.Synthesizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ct4ew
 */
public class FileOperation {

    private String prefix = "";

    public FileOperation() {
    }

    public static String createSpecFile(String fileContent) {
        String fileName = "";
        FileWriter fWriter = null;
        try {
            fileName = getRandomString(20);
            fileName = fileName + ".als";
            fWriter = new FileWriter("Parser/" + fileName);
            PrintWriter out = new PrintWriter(fWriter);
            out.write(fileContent);
            out.close();
            fWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(FileOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileName;
    }

    public static String getFileContent(String fileName) {
        String fileContents = "";
        try {
            File file = new File(fileName);
            FileInputStream fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            fin.read(fileContent);
//            byte[] contents = FileUtils.readFileToByteArray(file);
            fileContents = new String(fileContent);
            fin.close();
        } catch (IOException ex) {
            Logger.getLogger(FileOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileContents;
    }

    public static String getMappingRun(String specFile) {
        FrontParser fp = new FrontParser(specFile);
        fp.eraseComment();
        fp.parseFile();
        String mappingRun = fp.createMappingRun(specFile);
        return mappingRun;
    }

    public static boolean deleteFile(String fileName) {
        File f1 = new File(fileName);
        boolean success = f1.delete();
        return success;
    }

    public static void deleteDir(File f){// throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                deleteDir(c);
            }
        }
        if (!f.delete()) {
            //throw new FileNotFoundException("Failed to delete file: " + f);
        }
        f.delete();
    }

    public static String getRandomString(int length) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabsdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            b.append(base.charAt(random.nextInt(base.length())));
        }
        return b.toString();
    }
}
