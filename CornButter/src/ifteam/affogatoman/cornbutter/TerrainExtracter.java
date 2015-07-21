package ifteam.affogatoman.cornbutter;

import android.graphics.*;
import android.util.Log;
import java.io.*;
import ifteam.affogatoman.cornbutter.TGAReader;
import org.json.*;
import android.net.*;
import java.util.*;
import android.os.*;

public class TerrainExtracter {
    public static Bitmap getTerrain(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            
            int width = TGAReader.getWidth(bytes);
            int height = TGAReader.getHeight(bytes);
            int[] pixels = TGAReader.read(bytes, TGAReader.ARGB);
            Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
            
            return bitmap;
        } catch(Exception e) {
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        }
    }
    
    public static void cutAndSave(Bitmap bitmap, File meta) {
        try {
            FileInputStream fis = new FileInputStream(meta);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            
            String metaStr = new String(bytes);
            
            new JSONArray(metaStr);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
