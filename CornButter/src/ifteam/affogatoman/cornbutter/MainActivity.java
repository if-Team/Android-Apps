package ifteam.affogatoman.cornbutter;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import java.io.*;
import android.util.*;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        File test = new File("/sdcard/navercafe/SummmerFields 0_11_1.zip");
        byte[] terrainBytes = TexturePackParser.getBytesFromTexturePack(test, TexturePackParser.TERRAIN);
        byte[] metaBytes = TexturePackParser.getBytesFromTexturePack(test, TexturePackParser.META);
                
        Bitmap terrain = TerrainExtractor.getTerrain(terrainBytes);
        TerrainExtractor.cutAndSave(terrain, metaBytes);
    }
}
