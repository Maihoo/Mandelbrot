package gameClasses;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.concurrent.ForkJoinPool;
/*
import java.util.ArrayList;
import java.awt.Color;
import api.Game;
*/
import java.util.concurrent.RecursiveAction;

public class Mandelbrot {

	private boolean multithreaded;
	private int width, height, widthd2, heightd2;
    private int colorscheme[];
    private int colorscheme2[];
    private int NUM_ITERATIONS = 50;
    private int SEQ_CUTOFF = 128;
    public double scale = 1;
    public double extraX = 0;
    public double extraY = 0;
    
    Rule rule;
   
    Complex c = new Complex(0,0);
	Complex n = new Complex(0,0);
	int[] fractal;
	Image img;
	private ForkJoinPool fjPool = new ForkJoinPool();
	
	// how long to test for orbit divergence
    
    public Mandelbrot(int width, int hight, int maxIt, int maxSize, Rule rule, boolean mult) {
    	this.height = hight;
    	this.heightd2 = hight/2;
    	this.width = width;
    	this.widthd2 = width/2;
    	this.NUM_ITERATIONS = maxIt;
    	this.SEQ_CUTOFF = maxSize;
    	this.rule = rule;
    	this.multithreaded = mult;
    	
    	this.fractal = new int[width*hight];
    	this.colorscheme = new int[NUM_ITERATIONS+1];
    	this.colorscheme2 = new int[NUM_ITERATIONS+1];
    	
        // fill array with color palette going from Red over Green to Blue
        int colorScale = (255 * 2) / NUM_ITERATIONS;

        // going from Red to Green
        for (int i = 0; i < (NUM_ITERATIONS/2); i++)
        //               Alpha=255  | Red                   | Green       | Blue=0
        colorscheme[i] = 0xFF << 24 | (255 - i*colorScale) << 16 | i*colorScale << 8;

        // going from Green to Blue
        for (int i = 0; i < (NUM_ITERATIONS/2); i++)
        //                         Alpha=255 		 | Red=0 | Green              | Blue
        colorscheme[i+NUM_ITERATIONS/2] = 0xFF000000 | (255-i*colorScale) << 8 | i*colorScale;

        // convergence color
        colorscheme[NUM_ITERATIONS] = -0xFFFFFFF;	// Black
        
        
        
        
        //Red Green Blue
        
        // fill array with color palette going from Red over Green to Blue
        colorScale = (255 * 3) / NUM_ITERATIONS;

        for (int i = 0; i < (NUM_ITERATIONS/3); i++) {
        	colorscheme2[i] 						= (255-i*colorScale*1 )	<< 24 | 0xFF					<< 16 | i*colorScale*1	 		<< 8 | 0x000000FF;
        }

        for (int i = 0; i < (NUM_ITERATIONS/3); i++) {
        	colorscheme2[i+NUM_ITERATIONS/3] 		= i*colorScale*1		<< 24 | (255-i*colorScale*1) 	<< 16 | 0xFF 					<< 8 | 0x000000FF;
        	
        }
        
        for (int i = 0; i < (NUM_ITERATIONS/3); i++) {
        	colorscheme2[i+2*(NUM_ITERATIONS/3)] 	= 0xFF					<< 24 | i*colorScale*1			<< 16 | (255-i*colorScale*1)	<< 8 | 0x000000FF;
        	
        }
        
        // convergence color
        colorscheme2[NUM_ITERATIONS] = -0xFFFFFFF;	// Black
    }

    public void render(Graphics g) {	
    	if(multithreaded) fjPool.invoke(new FractalTask(-widthd2, -heightd2, width, height));
    	else calcMandelBrot(-widthd2, -heightd2, width, height);
    	this.img = getImageFromArray(fractal, width, height);
    	g.drawImage(img, 0, 0, null);
    } 
    
    void calcMandelBrot(int srcx, int srcy, int wi, int hi) {
    	
        double x, y, t, cx, cy;
        int k;    
        
        // loop over specified rectangle grid
        for (int px = srcx; px < srcx + wi; px++) {
        	for (int py = srcy; py < srcy + hi; py++) {
        		x=0; 
        		y=0;
        		
        		// convert pixels into complex coordinates between (-2, 2)
        		//	   |pnt	|scaling |factor  |movement
                cx =  (px / scale * 4.0) / wi -extraX * (width/wi);
                cy = -(py / scale * 4.0) / hi +extraY * (height/hi);
                
        		// test for divergence
        		for (k = 0; k < NUM_ITERATIONS; k++) {
        			t = x*x-y*y+cx;
        			y = 2*x*y+cy;
        			x = t;
        			if (x*x+y*y > 4) break;
        		}
            fractal[ (width/2 + px) + width * (height/2 + py)] = colorscheme[k];
        	}
        }
	}
    
    private class FractalTask extends RecursiveAction {
        /**
		 * 
		 */
		private static final long serialVersionUID = -8851280135990517770L;
		final int srcx;
        final int srcy;
        final int wi;
        final int hi;
        
        public FractalTask(int sx, int sy, int wid, int hit) {
          srcx = sx; srcy = sy; wi = wid; hi = hit;
        }
        @Override
        protected void compute() {
        	if (wi < SEQ_CUTOFF) calcMandelBrot(srcx, srcy, wi, hi);
        	else {        		
        		
        	  	FractalTask ul = new FractalTask(srcx		, srcy, 	 wi/2, hi/2);
            	FractalTask ur = new FractalTask(srcx+wi/2	, srcy, 	 wi/2, hi/2);
            	FractalTask ll = new FractalTask(srcx,        srcy+hi/2, wi/2, hi/2);
            	FractalTask lr = new FractalTask(srcx+wi/2	, srcy+hi/2, wi/2, hi/2);
            	
            	// forks and immediately joins the four subtasks
            	invokeAll(ul, ur, ll, lr);
          	}
        }
    }
    
    private static Image getImageFromArray(int[] pixels, int width, int hight) {
        // RGBdefault expects 0x__RRGGBB packed pixels
        ColorModel cm = DirectColorModel.getRGBdefault();
        SampleModel sampleModel = cm.createCompatibleSampleModel(width, hight);
        DataBuffer db = new DataBufferInt(pixels, hight, 0);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, db, null);
        BufferedImage image = new BufferedImage(cm, raster, false, null);
        
        return image;
      }
}