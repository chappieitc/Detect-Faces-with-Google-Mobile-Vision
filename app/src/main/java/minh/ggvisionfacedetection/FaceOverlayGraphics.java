package minh.ggvisionfacedetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.vision.face.Face;

class FaceOverlayGraphics extends CameraOverlay.OverlayGraphic {
    private static final float FACE_POSITION_RADIUS = 5.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int COLOR_CHOICES[] = {
            Color.RED,
            Color.WHITE,
    };
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;

    FaceOverlayGraphics(CameraOverlay overlay) {
        super(overlay);
        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void setId(int id) {
        mFaceId = id;
    }

    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);
        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);


        canvas.drawText("ID: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;

        //Very Simple bounding box around the face
        //canvas.drawRect(left, top, right, bottom, mBoxPaint);

        //Left - Bottom
        canvas.drawLine(left,bottom,left+xOffset/3,bottom,mBoxPaint);
        canvas.drawLine(left,bottom,left,bottom-yOffset/3,mBoxPaint);
        //Left - Top
        canvas.drawLine(left,top,left+xOffset/3,top,mBoxPaint);
        canvas.drawLine(left,top,left,top+yOffset/3,mBoxPaint);
        //Right - Bottom
        canvas.drawLine(right,bottom,right-xOffset/3,bottom,mBoxPaint);
        canvas.drawLine(right,bottom,right,bottom-yOffset/3,mBoxPaint);
        //Right - Top
        canvas.drawLine(right,top,right-xOffset/3,top,mBoxPaint);
        canvas.drawLine(right,top,right,top+yOffset/3,mBoxPaint);
        //Point in bounding box corners
        canvas.drawCircle(left, bottom, FACE_POSITION_RADIUS, mFacePositionPaint);
        canvas.drawCircle(left, top, FACE_POSITION_RADIUS, mFacePositionPaint);
        canvas.drawCircle(right, top, FACE_POSITION_RADIUS, mFacePositionPaint);
        canvas.drawCircle(right, bottom, FACE_POSITION_RADIUS, mFacePositionPaint);

        if(face.getIsSmilingProbability()>0.3){
            canvas.drawText("Smile",x ,top,mIdPaint);
        }
    }
}

