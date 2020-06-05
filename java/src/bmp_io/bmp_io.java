package bmp_io;

import java.awt.*;
import java.io.*;

public final class bmp_io {
	public static double remap(double i, double lb, double ub, double frm, double to){
		return (i - lb) / (ub - lb) *  (to - frm) + frm;

	}
	public static void main(String[] args) throws IOException {
		String inFilename = null;
		String outFilename = null;
		PixelColor pc = null;
		BmpImage bmp = null;

		if (args.length < 1) 
			System.out.println("At least one filename specified  (" + args.length + ")"); 
		
		// Zugriff auf Pixel mit bmp.image.getRgbPixel(x, y);		

		// Implementierung bei einem Eingabeparamter
		
		inFilename = args[0];
		InputStream in = new FileInputStream(inFilename);
		bmp = BmpReader.read_bmp(in);

		InputStream in2 = new FileInputStream("\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\Uebung 2\\manmade_buffer.bmp");
		BmpImage testbild = BmpReader.read_bmp(in2);

		OutputStream ascii_out = new FileOutputStream("2.1_horizontal.txt");

		// BGR schreiben horizontal 2.1.
		for(int x = 0; x < bmp.image.getWidth(); x++) {
			PixelColor pixel = bmp.image.getRgbPixel(x,0);

			int r = pixel.r >> 1;
			int g = pixel.g >> 1;
			int b = pixel.b >> 1;

			ascii_out.write(("("+pixel.r+"/"+pixel.g+"/"+pixel.b+")").getBytes());


			// ********* ToDo ***************
		}
    		ascii_out.close();

		ascii_out = new FileOutputStream("2.1_vertical.txt");

		// BGR schreiben vertikal 2.1.	
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			PixelColor pixel = bmp.image.getRgbPixel(0,y);
			ascii_out.write(("("+pixel.r+"/"+pixel.g+"/"+pixel.b+")").getBytes());
    		// ********* ToDo ***************
    		
		}
		ascii_out.close();

	    if (args.length == 1) 
			System.exit(0);

		
		// Implementierung bei Ein- und Ausgabeparamter

		outFilename = args[1];
		OutputStream out = new FileOutputStream(outFilename);
				/*

		// erzeuge graustufenbild
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for(int x = 0;x < bmp.image.getWidth(); x++) {
				// ********* ToDo ***************
				//Y= 0,3 x R + 0,6 x G + 0,1 x B
				PixelColor pixel = bmp.image.getRgbPixel(x,y);
				int brightness = (int)(0.3 * pixel.r + 0.6 * pixel.g + 0.1 * pixel.b);
				PixelColor newPixel = new PixelColor(brightness, brightness, brightness);

				bmp.image.setRgbPixel(x, y, newPixel);
				
			}
		}
		*/
		/*
		// downsampling
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for(int x = 0; x < bmp.image.getWidth(); x++) {

				// ********* ToDo ***************
				if (x+1%2 == 0 || y+1%2 == 0) {
					if (x+1%2 == 0 && y+1%2 != 0){
						if (x+1>0){
							bmp.image.setRgbPixel(x,y, bmp.image.getRgbPixel(x-1, y));
						}
					}
					if (y+1%2 == 0 && x+1%2 != 0){
						if (y>0){
							bmp.image.setRgbPixel(x,y, bmp.image.getRgbPixel(x, y-1));
						}
					}
					if (x+1%2 == 0 && y+1%2 == 0){
						if (x>0 && y>0){
							bmp.image.setRgbPixel(x,y, bmp.image.getRgbPixel(x-1, y-1));
						}
					}
				}
			}
		}
		*/
		/*
		// downsampling
		int downsample = 32;
		for(int y = 0; y < bmp.image.getHeight(); y+=downsample) {
			for (int x = 0; x < bmp.image.getWidth(); x+=downsample) {
				PixelColor pixel = bmp.image.getRgbPixel(x,y);

				for(int dsy = 0; dsy<downsample;dsy++){
					for(int dsx = 0; dsx<downsample;dsx++){
						if (x+dsx<bmp.image.width && y+dsy<bmp.image.height){
							bmp.image.setRgbPixel(x+dsx, y+dsy, pixel);

						}
					}
				}

			}
		}
		*/
				/*

		int downsample = 8;
		for(int y = 0; y < bmp.image.getHeight(); y+=downsample) { //+=downsample looked cool
			for (int x = 0; x < bmp.image.getWidth(); x++) { //+=downsample
				PixelColor pixel = bmp.image.getRgbPixel(x,y);

				for(int dsy = 0; dsy<downsample;dsy++){
					if (y+dsy<bmp.image.height){
						testbild.image.setRgbPixel(x, y+dsy, pixel);

					}
				}

			}
		}
		//horizontal downsample
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x,y);
				//int index = y * bmp.image.width + x;
				if (x % 2 != 0){
					PixelColor prev_pixel = bmp.image.getRgbPixel(x-1,y);

					testbild.image.setRgbPixel(x,y, prev_pixel);
				}else{
					testbild.image.setRgbPixel(x,y, pixel);

				}

			}
		}
		*/
		// bitreduzierung
		int reduced_bits = 1;
		//int full_channel_range = (int)Math.pow(2, 8);

		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x,y);

				int brightnessadjust = 1; //(int)Math.pow(2, reduced_bits);
				int r = (pixel.r >> reduced_bits) * brightnessadjust;
				int g = (pixel.g >> reduced_bits) * brightnessadjust;
				int b = (pixel.b >> reduced_bits) * brightnessadjust;

				testbild.image.setRgbPixel(x,y, new PixelColor(r * reduced_bits,g* reduced_bits,b*reduced_bits));
				// ********* ToDo ***************
			
			}
		}

		// bitreduzierung differenz
		int bitsPerColor = 8;
		int maxVal = 255;
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x,y);
				PixelColor reduced_pixel = testbild.image.getRgbPixel(x,y);

				int r = (pixel.r - (reduced_pixel.r * (int)Math.pow(2, bitsPerColor-reduced_bits-1)));
				int g = (pixel.g - (reduced_pixel.g * (int)Math.pow(2, bitsPerColor-reduced_bits-1)));
				int b = (pixel.b - (reduced_pixel.b * (int)Math.pow(2, bitsPerColor-reduced_bits-1)));

				if (Math.abs(Math.max(Math.max(g,b),Math.max(r,g)))>maxVal){
					maxVal = Math.abs(Math.max(Math.max(g,b),Math.max(r,g)));

				}

				testbild.image.setRgbPixel(x,y, new PixelColor(r,g,b));


				// ********* ToDo ***************
				
			}
		}

		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x,y);
				PixelColor reduced_pixel = testbild.image.getRgbPixel(x,y);
				int max_color_range = (int)Math.pow(2, bitsPerColor-reduced_bits-1);

				int r = (pixel.r - (reduced_pixel.r * max_color_range));
				int g = (pixel.g - (reduced_pixel.g * max_color_range));
				int b = (pixel.b - (reduced_pixel.b * max_color_range));
				if (Math.abs(r)>255){
					r = (int)remap(r, -maxVal, maxVal, 0, max_color_range);
					g = (int)remap(g, -maxVal, maxVal, 0, max_color_range);
					b = (int)remap(b, -maxVal, maxVal, 0, max_color_range);

				}

				testbild.image.setRgbPixel(x,y, new PixelColor(r,g,b));


				// ********* ToDo ***************

			}
		}
		
		try {
			BmpWriter.write_bmp(out, testbild);
		} finally {
			out.close();
		}
	}
}
