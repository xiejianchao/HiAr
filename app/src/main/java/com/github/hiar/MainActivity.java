package com.github.hiar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private ArFragment mArFragment;

    private ModelRenderable mRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        ModelRenderable.builder()
            .setSource(this, R.raw.mosaic_ball.sfb)
            .build()
            .thenAccept(renderable -> mRenderable = renderable)
            .exceptionally(
                throwable -> {
                    Toast toast =
                        Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });


        mArFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                if (mRenderable == null) {
                    return;
                }

                Toast.makeText(MainActivity.this, "Tap", Toast.LENGTH_SHORT).show();

                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(mArFragment.getArSceneView().getScene());

                TransformableNode transformableNode = new TransformableNode(mArFragment
                    .getTransformationSystem());
                transformableNode.setParent(anchorNode);
                transformableNode.setRenderable(mRenderable);
                transformableNode.select();

            }
        });
    }
}
