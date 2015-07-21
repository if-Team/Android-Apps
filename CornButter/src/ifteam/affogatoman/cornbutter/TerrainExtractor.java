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

public class TerrainExtractor {
    private static final String SAVE_PATH = "Affogatoman/CornButter/";
    
    
    public static Bitmap getTerrain(byte[] bytes) {
        try {
            int width = TGAReader.getWidth(bytes);
            int height = TGAReader.getHeight(bytes);
            int[] pixels = TGAReader.read(bytes, TGAReader.ARGB);
            Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
            
            return bitmap;
        } catch(Exception e) {
            Log.e("popcorn", e.getMessage());
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        }
    }
    
    public static void cutAndSave(Bitmap bitmap, byte[] bytes) {
        try {
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
                    
                    Bitmap cutBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
                    
                    File file = new File(Environment.getExternalStorageDirectory(), SAVE_PATH+name+"_"+uvsCount+".png");
                    if(!file.getParentFile().exists())
                        file.getParentFile().mkdirs();
                    
                    FileOutputStream fos = new FileOutputStream(file);
                    cutBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                }
            }
            
        } catch(Exception e) {
            Log.e("popcorn", e.toString());
        }
    }
}
