package com.example.dreg;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.hardware.*;
public class Stomper extends Activity implements SensorEventListener{
	MyView m;
	Thread t1;
	Bitmap b;
	float x1=0,y1=0;
	float x[]=new float[1000],y[]=new float[1000],t[]=new float[1000],k[]=new float[1000];
	int j=0;
	boolean isr=true;
	GestureDetector gd;
public void onCreate(Bundle sis)
{
	super.onCreate(sis);
	m=new MyView(this);
	setContentView(m);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	x1=getIntent().getExtras().getFloat("x");
	y1=getIntent().getExtras().getFloat("y");
	b=(Bitmap)getIntent().getExtras().getParcelable("b");
	SensorManager sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
	sm.registerListener(this,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
	gd=new GestureDetector(this,new Gd());
}
public void onSensorChanged(SensorEvent event)
{
	float sy=event.values[1];
	if(j!=0)
	{
		if(sy<-4)
			k[j-1]=-10;
		if(sy>2)
			k[j-1]=10;
	}
}
public void onAccuracyChanged(Sensor s,int acc)
{
	
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
class Gd extends GestureDetector.SimpleOnGestureListener
{
	public boolean onDown(MotionEvent event)
	{
		return true;
	}
	public boolean onSingleTapUp(MotionEvent event)
	{
		return true;
	}
	public boolean onFling(MotionEvent e1,MotionEvent e2,float velx,float vely)
	{
		x[j]=e1.getX();
		y[j]=e1.getY();
		t[j]=0;
		if(e1.getX()<e2.getX())
			k[j]=10;
		if(e1.getX()>e2.getX())
			k[j]=-10;
		j++;
		return true;
	}
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
			for(int i=0;i<j;i++)
			{
				canvas.save();
				canvas.translate(x[i],y[i]);
				canvas.rotate(t[i]);
				Path path=new Path();
				Shape shape=new Shape(path);
				canvas.clipPath(path);
				canvas.drawBitmap(b,new Rect(0,0,b.getWidth(),b.getHeight()),new RectF(-x1,-y1,-x1+canvas.getWidth(),-y1+canvas.getHeight()),p);
				canvas.restore();
				t[i]+=k[i];
				x[i]+=k[i];
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
		return gd.onTouchEvent(event);
	}
}
}
