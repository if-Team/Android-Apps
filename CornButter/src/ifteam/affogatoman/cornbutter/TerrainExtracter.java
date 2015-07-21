package ifteam.affogatoman.cornbutter;

import android.graphics.*;
import android.net.*;
import android.os.*;
import android.util.Log;

import java.io.*;
import java.util.*;

import ifteam.affogatoman.cornbutter.TGAReader;
import org.json.*;
import android.widget.*;

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
            
            JSONArray metas = new JSONArray(metaStr);
            
            for(int count = 0; count < metas.length(); count++) {
                JSONObject obj = metas.getJSONObject(count);
                JSONArray uvs = obj.getJSONArray("uvs");
                String name = obj.getString("name");
                
                for(int uvsCount = 0; uvsCount < uvs.length(); uvsCount++) {
                    JSONArray uvsAt = uvs.getJSONArray(uvsCount);
                    int x = (int) Math.round(uvsAt.getDouble(0) * bitmap.getWidth());
                    int y = (int) Math.round(uvsAt.getDouble(1) * bitmap.getHeight());
                    int width = (int) Math.round(uvsAt.getDouble(2) * bitmap.getWidth()) - x;
                    int height = (int) Math.round(uvsAt.getDouble(3) * bitmap.getHeight()) - y;
                    
                    Log.i("popcorn", name+"_"+uvsCount+" : "+x+", "+y+", "+width+", "+height);
                }
            }
            
        } catch(Exception e) {
            Log.e("popcorn", e.toString());
        }
    }
}
