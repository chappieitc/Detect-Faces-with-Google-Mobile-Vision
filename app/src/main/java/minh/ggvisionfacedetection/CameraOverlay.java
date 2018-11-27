package minh.ggvisionfacedetection;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.vision.CameraSource;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by minh on 2018/03/16.
 */

public class CameraOverlay extends View {
    private final Object mLock = new Object();
    private int mPreviewWitdh;
    private float mWidthScaleFactor = 1.0f;
    private int mPreviewHeight;
    private float mHeightScaleFactor = 1.0f;
    private int mFacing = CameraSource.CAMERA_FACING_FRONT;
    private Set<OverlayGraphic> mOverlayGraphics = new HashSet<>();

    public  static abstract class OverlayGraphic{
        private CameraOverlay mOverlay;
        public OverlayGraphic(CameraOverlay overlay){
            mOverlay = overlay;
        }

        public abstract void draw(Canvas canvas);

        public float scaleX(float horizonal)
        {
            return horizonal * mOverlay.mWidthScaleFactor;
        }

        public float scaleY(float vertical){
            return vertical * mOverlay.mHeightScaleFactor;
        }

        public float translateX(float x){
            if(mOverlay.mFacing == CameraSource.CAMERA_FACING_FRONT){
                return mOverlay.getWidth() -scaleX(x);
            }else return scaleX(x);
        }

        public float translateY(float y){
            return scaleY(y);
        }

        public void postInvalidate(){
            mOverlay.postInvalidate();
        }
    }


    public CameraOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void clear(){
        synchronized (mLock){
            mOverlayGraphics.clear();
        }
        postInvalidate();
    }

    public void add(OverlayGraphic overlayGraphic){
        synchronized (mLock){
            mOverlayGraphics.add(overlayGraphic);
        }
        postInvalidate();
    }

    public void remove(OverlayGraphic overlayGraphic) {
        synchronized (mLock) {
            mOverlayGraphics.remove(overlayGraphic);
        }
        postInvalidate();
    }


    public void setCameraInfo(int previewWidth,int previewHeight,int facing){
        synchronized (mLock){
            mPreviewHeight = previewHeight;
            mPreviewWitdh = previewWidth;
            mFacing = facing;
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        synchronized (mLock){
            if(mPreviewWitdh != 0 && mPreviewHeight != 0){
                mWidthScaleFactor = (float) canvas.getWidth() / (float) mPreviewWitdh;
                mHeightScaleFactor = (float) canvas.getHeight() / (float) mPreviewHeight;
            }

            for(OverlayGraphic overlayGraphic:mOverlayGraphics){
                overlayGraphic.draw(canvas);
            }
        }
    }
}
