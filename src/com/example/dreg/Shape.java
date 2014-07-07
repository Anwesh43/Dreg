package com.example.dreg;
import android.graphics.*;
public class Shape {
public Shape(Path path)
{
	path.moveTo(30, 0);
	path.lineTo(60,-30);
	path.lineTo(60,-60);
	path.lineTo(-60, -60);
	path.lineTo(-60, -30);
	path.lineTo(-30,0);
	path.lineTo(-60,30);
	path.lineTo(-60,60);
	path.lineTo(60,60);
	path.lineTo(60,30);
	path.lineTo(30,0);
}
}
