package com.pajakmedan.pajakmedan;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Created by milha on 3/4/2018.
 */
public class Installation {
    private static String sID = null;

    public synchronized static String id(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), Constants.INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
//        String id = Constants.USER_API_TOKEN;
        out.write(id.getBytes());
        out.close();
    }
}
