package minh.ggvisionfacedetection;

import android.os.Build;
import android.view.View;

public class HideSysUI {
    public void HideSysUI(View view){
        if(view == null) return;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                int uiOpts = View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

                view.setSystemUiVisibility(uiOpts);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
