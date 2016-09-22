package xobyx.xcontactj.until;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by xobyx on 1/25/2016.
 * For xobyx.xcontactj.until/XContactj
 */
public class scale_animation extends Animation {
    private Camera camera;
    private int mwith;
    private int mhi;


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degrees = 5 * interpolatedTime;
        final Matrix matrix = t.getMatrix();


        int l =(int)((interpolatedTime / getDuration()) * 2f);




        t.setAlpha((float)0.1*interpolatedTime);
        camera.save();
        //camera.translate(0,0,0);
        camera.translate((mwith/20)* (1.0f - interpolatedTime), (mhi/2)* (1.0f - interpolatedTime), -300* (1.0f - interpolatedTime));
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(0, 0);
        matrix.postTranslate(0, 0);

       // t.
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
        mwith=width;
        mhi=height;

    }
}
