package ifteam.affogatoman.cornbutter;

import java.io.*;
import java.util.zip.*;
import android.util.*;
import java.util.*;

public class TexturePackParser {
    public static final int TERRAIN = 0;
    public static final int META = 1;
    
    public static byte[] getBytesFromTexturePack(File file, int type) {
        try {
            ZipFile zipfile = new ZipFile(file);
            
            Enumeration<? extends ZipEntry> entries = zipfile.entries();
            while(entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if(type == TERRAIN && entry.getName().endsWith("terrain-atlas.tga")) {
                    InputStream is = zipfile.getInputStream(entry);
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    is.close();
                    
                    return bytes;
                } else if(type == META && entry.getName().endsWith("terrain.meta")) {
                    InputStream is = zipfile.getInputStream(entry);
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    is.close();

                    return bytes;
                }
            }
        } catch(Exception e) {
            Log.e("popcorn", e.toString());
        }
        
        return null;
    }
}
