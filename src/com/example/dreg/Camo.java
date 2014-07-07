package com.example.dreg;

import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.*;
import android.content.pm.*;
import android.content.res.Configuration;
import android.graphics.*;
import android.content.*;
import android.hardware.*;
import android.app.Activity;
import android.view.Menu;
import android.provider.MediaStore;
public class Camo extends Activity {
Thread t1;
boolean isr=true;
MyView m;
float x=0,y=0;
int rc=1;
Bitmap b;
boolean isdown=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m=new MyView(this);
        setContentView(m);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,rc);
    }
    public void onConfigurationChanged(Configuration config)
    {
    	super.onConfigurationChanged(config);
    }
    public void onPause()
    {
    	super.onPause();
    	isr=false;
    	while(true)
    	{
    		try
    		{
    			t1.join();
    			break;
    		}
    		catch(Exception ex)
    		{
    			
    		}
    	}
    }
    public void onResume()
    {
    	super.onResume();
    	isr=true;
    	t1=new Thread(m);
    	t1.start();
    }
    class MyView extends SurfaceView implements Runnable
    {
    	SurfaceHolder sh;
    	Canvas canvas;
    	Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
    	public void run()
    	{
    	while(isr)
    	{
    		if(!sh.getSurface().isValid())
    			continue;
    		canvas=sh.lockCanvas();
    		canvas.drawColor(Color.WHITE);
    		if(b!=null)
    		{
    			ColorMatrix cm=new ColorMatrix();
    			ColorMatrix cm1=new ColorMatrix();
    			cm.setSaturation(0);
    			cm1.setSaturation(1);
    			ColorMatrixColorFilter cf=new ColorMatrixColorFilter(cm);
    			ColorMatrixColorFilter cf1=new ColorMatrixColorFilter(cm1);
    			p.setColorFilter(cf);
    			canvas.drawBitmap(b,new Rect(0,0,b.getWidth(),b.getHeight()),new RectF(0,0,canvas.getWidth(),canvas.getHeight()),p);
    			canvas.save();
    			canvas.translate(x,y);
    			Path path=new Path();
    			Shape shape=new Shape(path);
    			canvas.clipPath(path);
    			p.setColorFilter(cf1);
    			canvas.drawBitmap(b,new Rect(0,0,b.getWidth(),b.getHeight()),new RectF(-x,-y,-x+canvas.getWidth(),-y+canvas.getHeight()),p);
    			canvas.restore();
    			
    		}
    		sh.unlockCanvasAndPost(canvas);
    		try
    		{
    			Thread.sleep(100);
    		}
    		catch(Exception ex)
    		{
    			
    		}
    	}
    	}
    	public MyView(Context context)
    	{
    		super(context);
    		sh=getHolder();
    		t1=new Thread(this);
    		t1.start();
    	}
    	public boolean onTouchEvent(MotionEvent event)
    	{
    		if(event.getAction()==MotionEvent.ACTION_DOWN)
    		{
    			if(!isdown)
    			{
    			x=event.getX();
    			y=event.getY();
    			isdown=true;
    			}
    		}
    		if(event.getAction()==MotionEvent.ACTION_MOVE)
    		{
    			if(isdown)
    			{
    				x=event.getX();
    				y=event.getY();
    			}
    		}
    		if(event.getAction()==MotionEvent.ACTION_UP)
    		{
    			isdown=false;
    			Intent i=new Intent(Camo.this,Stomper.class);
    			i.putExtra("x",x);
    			i.putExtra("y",y);
    			i.putExtra("b",b);
    			startActivity(i);
    		}
    		return true;	
    	}
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
    	if(requestCode==rc)
    		b=(Bitmap)data.getExtras().get("data");
    }
}
