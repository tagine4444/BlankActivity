package coursea.project.com.myapplication.domain.services;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

/**
 * Created by chidra on 1/26/18.
 */

public class TestService {

    private final static String LOG_TAG = TestService.class.getSimpleName();


    public InputStream openFile(String fileName) throws FileNotFoundException, IOException {


        File root = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

        //Get the text file
        File file = new File(root, fileName);

        InputStream is = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr, 8192);
        StringBuilder text = new StringBuilder();
        try {
            String test;
            while (true) {
                test = br.readLine();
                // readLine() returns null if no more lines in the file
                if (test == null) break;
                text.append(test);
                text.append('\n');

            }
            return new FileInputStream(file);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG,"Erro While Reading Json from device. File: "+fileName ,e);
            throw e;
        }finally {

            if(isr!=null){
                isr.close();
            }
            if(is!=null){
                is.close();
            }
             if(br!=null){
                 br.close();
            }
        }

    }

    public boolean fileExists(String fileName){
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        File file = new File(root, fileName);

        return file.exists();
    }
    public void saveTestToDevice(InputStream fileIs, String fileName) throws IOException {

        FileWriter fw = null;

        try {

            if (!isExternalStorageWritable()) {
                return;
            }
            // Get the directory for the user's public pictures directory.
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

            if (!root.exists()) {
                root.mkdirs();
            }


            File file = new File(root, fileName);


            if (file.exists()) {
                file.delete();
            }
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            final String streamString = getTestJsonAsString(fileIs);

            fw = new FileWriter(file);
            fw.write(streamString);


        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG,"Error While Saving Json to device. File: "+fileName ,e);
            throw e;
        } finally {

            if (fw != null) {
                fw.close();
            }

        }
    }

    private String getTestJsonAsString(InputStream fileIs) {
        java.util.Scanner s = new java.util.Scanner(fileIs);
        s.useDelimiter("\\A");
        String streamString = s.hasNext() ? s.next() : "";
        s.close();
        return streamString;
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
