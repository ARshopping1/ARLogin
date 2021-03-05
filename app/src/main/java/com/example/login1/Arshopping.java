package com.example.login1;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.net.Uri;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.*;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.NodeParent;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Arshopping extends AppCompatActivity
{
    private ArFragment arFragment;
    TextView tv;

    ViewRenderable cardplay;

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arshopping);
        //......................
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        ArSceneView sv = arFragment.getArSceneView();
        Frame frame = sv.getArFrame();
        Scene scene = sv.getScene();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("test", "onClick");
//                addObject(Uri.parse("halo.sfb"));
//                addObject(Uri.parse("halo.sfa"));
//                MaterialFactory.makeTransparentWithColor(getApplicationContext(), new com.google.ar.sceneform.rendering.Color(1, 1, 1,1)).thenAccept(material ->
                MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new com.google.ar.sceneform.rendering.Color(1, 1, 1, 1)).thenAccept(material ->
                {
                    Log.d("test", "creating molecule " + material);
                    try
                    {
                        AnchorNode anchorNode = getAnchorNode(material);
                        // TransformableNode sphereNode = Util.createSphere(arFragment, 0, 0, 0, 0.1f, anchorNode, material);

                        //                        createMolecule(material);

                        //addObject(Uri.parse("Couch.gltf"));
                        String modelfile=getIntent().getStringExtra("modelfile");
                        addObject(Uri.parse(ServiceClient.BASE_URL+"/model/"+modelfile));

                        //read byte [] from file
//                        byte []model= Files.readAllBytes(new File("file:///android_asset/box.sfb").toPath());
                        // InputStream a = getAssets().open("Sofa.sfb");
                        //    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                        //  byte []b=new byte[10240];
                        //  int n;
                        // while((n=a.read(b))!=-1)
                        // {
                        //      baos.write(b,0,n);
                        // }
                        //a.close();
                        // byte []model=baos.toByteArray();
                        // Log.e("Model Length",String.valueOf(model.length));
                        // addObject(model);
                    } catch (Exception e)
                    {
                        Log.e("Model Exception", e.getMessage(), e);
                    }
                });
            }
        });
    }
    private void showChildren(NodeParent n, int depth)
    {
        String s = "";
        for (int i = 0; i < depth; i++)
            s += "\t";
        Log.d("test", s);

        Log.d("test", n.toString());

        for (Node n2 : n.getChildren())
        {
            showChildren(n2, depth + 1);
        }
    }

    private AnchorNode getAnchorNode(Material material)
    {
        //  Log.d("test", "getAnchorNode");
        Frame frame = arFragment.getArSceneView().getArFrame();
        Point point = getScreenCenter();
        Log.d("test", "SC " + point.toString());
        if (frame != null)
        {
            final List<HitResult> hits = frame.hitTest((float) point.x, (float) point.y);
            Log.d("test", "Hits :" + hits.toString());
            if (!hits.isEmpty())
            {
                ModelRenderable sphere1 = ShapeFactory.makeSphere(0f, new Vector3(0, 0, 0), material);
                Anchor a = hits.get(0).createAnchor();
                AnchorNode anchorNode = new AnchorNode(a);
//                anchorNode.setParent();
                arFragment.getArSceneView().getScene().addChild(anchorNode);
                return anchorNode;
            }
        }
        return null;
    }

    public static Node createSphere1(float x, float y, float z, float radius, Node parent, Material material)
    {
        Log.d("test", String.format("Create Sphere : %f %f %f %f", x, y, z, radius));
        Node base = new Node();

        Node sun = new Node();
        sun.setParent(base);
        sun.setLocalPosition(new Vector3(0.0f, 0.5f, 0.0f));

        Node sunVisual = new Node();
        sunVisual.setParent(sun);
//        sunVisual.setRenderable(sunRenderable);
        sunVisual.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));
        return base;
    }
    private TransformableNode createCylinder(float x, float y, float z, float radius, float height, Node parent, Material material)
    {

//        Log.d("test", "createSphere: "+x+", "+y+", "+z+", "+radius+", "+parent);
        ModelRenderable sphere = ShapeFactory.makeCylinder(radius, height, new Vector3(x, y, z), material);
//        Log.d("test", "aftercreateSphere: "+x+", "+y+", "+z+", "+radius+", "+parent);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setRenderable(sphere);
        transformableNode.setParent(parent);
        parent.addChild(transformableNode);
        transformableNode.select();
        return transformableNode;

    }

    @Override
    public String toString()
    {
        return "Arshopping{" +
                "arFragment=" + arFragment +
                '}';
    }

    private void addObject(Uri parse)
    {
        Log.d("test", "adding object");
        Frame frame = arFragment.getArSceneView().getArFrame();
        Point point = getScreenCenter();
        Log.d("test", point.toString());
        if (frame != null)
        {
            final List<HitResult> hits = frame.hitTest((float) point.x, (float) point.y);
            Log.d("test", "Hits :" + hits.toString());

            for (int i = 0; i < hits.size(); i++)
            {
                final int j = i;
                Trackable trackable = hits.get(i).getTrackable();
                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hits.get(i).getHitPose()))
                {
                    placeObject(arFragment, hits.get(i).createAnchor(), parse);
                }
            }
        }
    }

    private void addObject(byte [] model)
    {
        Log.d("test", "adding object");
        Frame frame = arFragment.getArSceneView().getArFrame();
        Point point = getScreenCenter();
        Log.d("test", point.toString());
        if (frame != null)
        {
            final List<HitResult> hits = frame.hitTest((float) point.x, (float) point.y);
            Log.d("test", "Hits :" + hits.toString());

            for (int i = 0; i < hits.size(); i++)
            {
                final int j = i;
                Trackable trackable = hits.get(i).getTrackable();
                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hits.get(i).getHitPose()))
                {

                    placeObject(arFragment, hits.get(i).createAnchor(), model);
                }
            }
        }
    }

    private void placeObject(final ArFragment fragment, final Anchor createAnchor, Uri model)
    {
        Log.d("test", "placing object");
        ModelRenderable.builder().setSource(fragment.getContext(), model).build().thenAccept((new Consumer()
        {
            public void accept(Object var1)
            {
                this.accept((ModelRenderable) var1);
            }

            public final void accept(ModelRenderable it)
            {
                if (it != null)
                    Arshopping.this.addNode(arFragment, createAnchor, it);
                else
                    Log.d("test", "it==null");
            }
        })).exceptionally((new Function()
        {
            public Object apply(Object var1)
            {
                return this.apply((Throwable) var1);
            }

//            @Nullable
            public final Void apply(Throwable it)
            {
//                AlertDialog.Builder builder = new AlertDialog.Builder(Arshopping.this);
//                builder.setMessage(it.getMessage()).setTitle("error!");
//                AlertDialog dialog = builder.create();
//                dialog.show();
                return null;
            }
        }));
    }

    private void placeObject(final ArFragment fragment, final Anchor createAnchor, byte [] model)
    {
        Log.d("test", "placing object");
        ModelRenderable.builder().setSource(fragment.getContext(), ()->new ByteArrayInputStream(model)).build().thenAccept((new Consumer()
        {
            public void accept(Object var1)
            {
                this.accept((ModelRenderable) var1);
            }

            public final void accept(ModelRenderable it)
            {
                if (it != null)
                    Arshopping.this.addNode(arFragment, createAnchor, it);
                else
                    Log.d("test", "it==null");
            }
        })).exceptionally((new Function()
        {
            public Object apply(Object var1)
            {
                return this.apply((Throwable) var1);
            }

//            @Nullable
            public final Void apply(Throwable it)
            {
//                AlertDialog.Builder builder = new AlertDialog.Builder(Arshopping.this);
//                builder.setMessage(it.getMessage()).setTitle("error!");
//                AlertDialog dialog = builder.create();
//                dialog.show();
                return null;
            }
        }));
    }

    private void addNode(ArFragment fragment, Anchor createAnchor, ModelRenderable renderable)
    {
        Log.d("test", "adding node");

        AnchorNode anchorNode = new AnchorNode(createAnchor);
        TransformableNode transformableNode = new TransformableNode(fragment.getTransformationSystem());
        transformableNode.setRenderable(renderable);
        transformableNode.setParent(anchorNode);
        anchorNode.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));
        fragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }

    private Point getScreenCenter()
    {
        View vw = findViewById(android.R.id.content);
        return new Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }

}