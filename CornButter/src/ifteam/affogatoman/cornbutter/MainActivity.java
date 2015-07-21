package ifteam.affogatoman.cornbutter;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import java.io.*;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Bitmap terrain = TerrainExtracter.getTerrain(new File("/sdcard/SpeedSoftware/Extracted/pkg/assets/images/terrain-atlas.tga"));
        TerrainExtracter.cutAndSave(terrain, new File("/sdcard/SpeedSoftware/Extracted/pkg/assets/images/terrain.meta"));
    }
}
