package bmp_io;

import java.awt.*;
import java.io.*;
import java.util.HashMap;

public final class bmp_io {

	public static void save_histo_data(int[] data) throws IOException{
		FileWriter writer = new FileWriter(	"C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\Ue4\\1\\histo_data.txt");
		for (int d : data){
			writer.write(data+"\r\n");
		}
		writer.close();
		System.out.println("Saved Histogramm data [line by line].");
	}
	public static double remap(double i, double lb, double ub, double frm, double to){
		return (i - lb) / (ub - lb) *  (to - frm) + frm;

	}
	public static BmpImage rect(BmpImage bmp, int x, int y, int w, int h, PixelColor color){
		for(int dy = y; dy < y+h; dy++) {
			for (int dx = x; dx < x+w; dx++) {
				bmp.image.setRgbPixel(dx,dy, color);
			}
		}
		return bmp;
	}
	public static BmpImage Histogramm(BmpImage bmp, int[] datas){

		//determine maxval
		int maxVal = -1;
		for (int i = 0; i< datas.length; i++){
			int data = datas[i];
			if (data > maxVal){
				maxVal = data;
			}
		}
		int col_width = (int)bmp.image.width/datas.length;
		for (int i = 0; i< datas.length; i++){
			int data = datas[i];
			int col_height = (int)remap(data, 0, maxVal, 0, bmp.image.height-1);
			rect(bmp, i * col_width, bmp.image.height-1 - col_height, col_width, col_height, new PixelColor(0,255,0));
		}
		for (int data : datas){ //hihi

		}
		return bmp;
	}
	public static int[] BrightnessHistogramm(BmpImage bmp){
		//HashMap<Double, Integer> histogramm = new HashMap<Double, Integer>();
		int[] histogramm = new int[256];
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);
				double brightness_of_pixel = perceived_brightness(pixel);//(pixel.r+pixel.g+pixel.b)/3.0; //naive...
				histogramm[(int)brightness_of_pixel]++;
			}
		}
		return histogramm;
	}

	public static BmpImage red_channel_only(BmpImage bmp){

		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);
				pixel.g = 0;
				pixel.b = 0;
			}
		}
		return bmp;
	}
	public static BmpImage green_channel_only(BmpImage bmp){
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);
				pixel.r = 0;
				pixel.b = 0;
			}
		}
		return bmp;
	}
	public static BmpImage blue_channel_only(BmpImage bmp){
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);
				pixel.r = 0;
				pixel.g = 0;
			}
		}
		return bmp;
	}

	public static PixelColor RGB_to_YCbCr(PixelColor p){

		int Y  = (int)(0.299  * p.r +0.587 * p.g + 0.114 * p.b);
		int Cb = (int)(-0.169 * p.r -0.331 * p.g + 0.5   * p.b + 128);
		int Cr = (int)(0.5    * p.r -0.419 * p.g - 0.081 * p.b + 128);

		return new PixelColor(Y, Cb, Cr);
	}
	public static PixelColor YCbCr_to_RGB(PixelColor p){
		double Y  = p.r;
		double Cb = p.g;
		double Cr = p.b;

		int r = (int) (1.403 * (Cr - 128) + Y);
		int g = (int) ((-0.344 * (Cb - 128))+(-0.714 *(Cr-128)) + Y);
		int b = (int) ( 1.773*(Cb-128) + Y);

		r = Math.max(0, Math.min(255, r));
		g = Math.max(0, Math.min(255, g));
		b = Math.max(0, Math.min(255, b));
		return new PixelColor(r, g, b);
	}
	public static BmpImage lumin(BmpImage bmp){
		for(int y = 0; y < bmp.image.getHeight();y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor p = bmp.image.getRgbPixel(x, y);
				double luminance = 0.299 * p.r + 0.587 * p.g + 0.114 * p.b;

				int l = (int) Math.max(0, Math.min(255, luminance));
				bmp.image.setRgbPixel(x, y, new PixelColor(l, l, l));
			}
		}
		return bmp;
	}
	public static BmpImage chromacity_blue(BmpImage bmp){
		for(int y = 0; y < bmp.image.getHeight();y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor p = bmp.image.getRgbPixel(x, y);
				int red   = (int)(-0.169 * p.r +128);
				int green = (int)(-0.331 * p.g +128);
				int blue  = (int)(0.5    * p.b +128);
				int cb = red + green + blue - 256;
				int chromacity_blue = Math.max(0, Math.min(255, cb));



				//bmp.image.setRgbPixel(x, y, new PixelColor(chromacity_blue, chromacity_blue, chromacity_blue));
				bmp.image.setRgbPixel(x, y, new PixelColor(red, green, blue));

			}
		}
		return bmp;
	}
	public static BmpImage chromacity_red(BmpImage bmp){
		for(int y = 0; y < bmp.image.getHeight();y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor p = bmp.image.getRgbPixel(x, y);
				int red   = (int)(0.5    * p.r +128);
				int green = (int)(-0.419 * p.g +128);
				int blue  = (int)(-0.081 * p.b +128);
				int cr = red + green + blue - 256;
				int chromacity_red = Math.max(0, Math.min(255, cr));


				//bmp.image.setRgbPixel(x, y, new PixelColor(chromacity_red, chromacity_red, chromacity_red));
				bmp.image.setRgbPixel(x, y, new PixelColor(red, green, blue));

			}
		}
		return bmp;
	}

	public static BmpImage convert_YCbCR_to_RGB(BmpImage bmp){
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);
				bmp.image.setRgbPixel(x, y, YCbCr_to_RGB(pixel));
			}
		}
		return bmp;
	}
	public static BmpImage convert_RGB_to_YCbCr(BmpImage bmp){
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);
				bmp.image.setRgbPixel(x, y, RGB_to_YCbCr(pixel));
			}
		}
		return bmp;
	}
	public static double perceived_brightness(PixelColor pixel){
		return (0.2 * pixel.r) + (0.72 * pixel.g) + (0.07 * pixel.b);
	}
	public static double brightness(PixelColor p){
		return (p.r+p.g+p.b)/3.0;
	}
	public static PixelColor change_contrast(PixelColor p, double k){
		double contrast_correction_factor = k;//259*(k+255)/255*(259-k);
		double bright = brightness(p);
		double red   = contrast_correction_factor*(bright-128)+128;
		double green = contrast_correction_factor*(bright-128)+128;
		double blue  = contrast_correction_factor*(bright-128)+128;
		int r = (int)Math.max(0, Math.min(255, red));
		int g = (int)Math.max(0, Math.min(255, green));
		int b = (int)Math.max(0, Math.min(255, blue));
		return new PixelColor(r, g, b);
	}

	public static BmpImage change_contrast(BmpImage bmp, double k){
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);

				bmp.image.setRgbPixel(x, y, change_contrast(pixel, k));
			}
		}
		return bmp;
	}
	public static PixelColor change_brightness(PixelColor p, int h){
		int r = Math.max(0, Math.min(255, p.r+h));
		int g = Math.max(0, Math.min(255, p.g+h));
		int b = Math.max(0, Math.min(255, p.b+h));
		return new PixelColor(r, g, b);
	}

	public static BmpImage change_brightness(BmpImage bmp, int h){
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);

				bmp.image.setRgbPixel(x, y, change_brightness(pixel, h));
			}
		}
		return bmp;
	}

	public static BmpImage bit_reduction(BmpImage bmp, int reduced_bits){
		BmpImage img = new BmpImage();

		// bitreduzierung
		//int reduce_bits = 1;
		//int full_channel_range = (int)Math.pow(2, 8);

		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x,y);

				int brightnessadjust = (int)Math.pow(2, reduced_bits);
				int r = (pixel.r >> reduced_bits) * brightnessadjust;
				int g = (pixel.g >> reduced_bits) * brightnessadjust;
				int b = (pixel.b >> reduced_bits) * brightnessadjust;

				img.image.setRgbPixel(x,y, new PixelColor(r * reduced_bits,g* reduced_bits,b*reduced_bits));
			}
		}
		return img;
	}

	public static int Kontrast(BmpImage bmp){
		int mittlereHelligkeit = MittlereHelligkeit(bmp);
		double total = 0;
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);
				total += Math.pow(brightness(pixel)-mittlereHelligkeit,2);
			}
		}
		return (int)Math.sqrt(total/(bmp.image.getWidth()*bmp.image.getHeight()));
	}
	public static int MittlereHelligkeit(BmpImage bmp){
		double total = 0;
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x, y);
				total += brightness(pixel);
			}
		}
		return (int)(total/(bmp.image.getWidth()*bmp.image.getHeight()));
	}

	public static BmpImage difference_img(BmpImage bmp, int reduced_bits){
		BmpImage diff = new BmpImage();
		int maxVal = 255; //something is wrong in the bit reduction and the difference image
		BmpImage testbild = bit_reduction(bmp, reduced_bits);
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for (int x = 0; x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x,y);
				PixelColor reduced_pixel = testbild.image.getRgbPixel(x,y);
				int max_color_range = (int)Math.pow(2, 8-reduced_bits-1);

				int r = (pixel.r - (reduced_pixel.r * max_color_range));
				int g = (pixel.g - (reduced_pixel.g * max_color_range));
				int b = (pixel.b - (reduced_pixel.b * max_color_range));
				if (Math.abs(r)>255){
					r = (int)remap(r, -maxVal, maxVal, 0, max_color_range);
					g = (int)remap(g, -maxVal, maxVal, 0, max_color_range);
					b = (int)remap(b, -maxVal, maxVal, 0, max_color_range);

				}

				testbild.image.setRgbPixel(x,y, new PixelColor(r,g,b));
			}
		}
		return diff;
	}

	public static BmpImage downsample(BmpImage bmp, int downsample){
		//int downsample = 32;
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
		return bmp;
	}
	public static BmpImage grayscale(BmpImage bmp){
		for(int y = 0; y < bmp.image.getHeight(); y++) {
			for(int x = 0;x < bmp.image.getWidth(); x++) {
				PixelColor pixel = bmp.image.getRgbPixel(x,y);
				int brightness = (int)(0.3 * pixel.r + 0.6 * pixel.g + 0.1 * pixel.b);
				PixelColor newPixel = new PixelColor(brightness, brightness, brightness);
				bmp.image.setRgbPixel(x, y, newPixel);

			}
		}
		return bmp;
	}

	public static void ascii_to_txt(BmpImage bmp, String filename, boolean vertical) throws IOException{
		OutputStream ascii_out = new FileOutputStream(filename);
			if (!vertical){
				// BGR schreiben horizontal 2.1.
				for(int x = 0; x < bmp.image.getWidth(); x++) {
					PixelColor pixel = bmp.image.getRgbPixel(x,0);
					ascii_out.write(("("+pixel.r+"/"+pixel.g+"/"+pixel.b+")").getBytes());
				}
				ascii_out.close();
			}else{
				// BGR schreiben vertikal 2.1.
				for(int y = 0; y < bmp.image.getHeight(); y++) {
					PixelColor pixel = bmp.image.getRgbPixel(0,y);
					ascii_out.write(("("+pixel.r+"/"+pixel.g+"/"+pixel.b+")").getBytes());
				}
				ascii_out.close();
			}

	}

	public static BmpImage open_bmp(String filename) throws IOException{
		InputStream in = new FileInputStream(filename);
		return BmpReader.read_bmp(in);
	}

	public static void save_bmp(BmpImage bmp, String outFilename) throws IOException{
		OutputStream out = new FileOutputStream(outFilename);

		try {
			BmpWriter.write_bmp(out, bmp);
		} finally {
			out.close();
		}
	}

	public static void main(String[] args) throws IOException {
		String inFilename = null;
		String outFilename = null;
		PixelColor pc = null;
		BmpImage bmp = null;
		// Implementierung bei Ein- und Ausgabeparamter

		if (args.length < 1) 
			System.out.println("At least one filename specified  (" + args.length + ")");

		if (args.length == 1)
			System.exit(0);
		// Zugriff auf Pixel mit bmp.image.getRgbPixel(x, y);		

		// Implementierung bei einem Eingabeparamter

		bmp = open_bmp(args[0]);
		//InputStream in2 = new FileInputStream("\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\Uebung 2\\manmade_buffer.bmp");
		//BmpImage testbild = BmpReader.read_bmp(in2);

		//ascii_to_txt(bmp,"2.1_horizontal.txt", false);
		//ascii_to_txt(bmp, "2.1_vertical.txt", true);

		outFilename = args[1];
		PixelColor YCbCr_pixel = RGB_to_YCbCr(new PixelColor(255, 255, 255));
		System.out.println(YCbCr_pixel.r + ", "+YCbCr_pixel.g+", "+YCbCr_pixel.b);
		PixelColor back = YCbCr_to_RGB(YCbCr_pixel);
		System.out.println(back.r + ", "+back.g+", "+back.b);

		int brightness_inc = 255;
		//bmp = convert_YCbCR_to_RGB(bmp);

		String save = args[1].split("\\.")[0];
		save+="_brightness"+brightness_inc;
		save+='.'+args[1].split("\\.")[1];
		save_bmp(change_brightness(bmp,brightness_inc), save);

		//reset bmp
		bmp = open_bmp(args[0]);
		int contrast_inc = 30;

		save = args[1].split("\\.")[0];
		save+="_contrast"+contrast_inc;
		save+='.'+args[1].split("\\.")[1];
		save_bmp(change_contrast(bmp,contrast_inc), save);

		//reset bmp
		bmp = open_bmp(args[0]);

		bmp = convert_RGB_to_YCbCr(bmp);
		save = args[1].split("\\.")[0];
		save+="_converted";
		save+='.'+args[1].split("\\.")[1];
		save_bmp(bmp, save);

		bmp = convert_YCbCR_to_RGB(bmp);
		save = args[1].split("\\.")[0];
		save+="_converted_and_back";
		save+='.'+args[1].split("\\.")[1];
		save_bmp(bmp, save);

		//bmp = convert_YCbCR_to_RGB(bmp);


		//reset bmp
		bmp = open_bmp(args[0]);

		bmp = chromacity_blue(bmp);
		save = args[1].split("\\.")[0];
		save+="_chrominanceCb_all_channels";
		save+='.'+args[1].split("\\.")[1];
		save_bmp(bmp, save);

		//reset bmp
		bmp = open_bmp(args[0]);

		bmp = chromacity_red(bmp);
		save = args[1].split("\\.")[0];
		save+="_chrominanceCr_all_channels";
		save+='.'+args[1].split("\\.")[1];
		save_bmp(bmp, save);

		//reset bmp
		bmp = open_bmp(args[0]);

		bmp = convert_RGB_to_YCbCr(bmp);
		red_channel_only(bmp);
		save = args[1].split("\\.")[0];
		save+="_YCbCr_only_Y";
		save+='.'+args[1].split("\\.")[1];
		save_bmp(bmp, save);

		//reset bmp
		bmp = open_bmp(args[0]);

		bmp = convert_RGB_to_YCbCr(bmp);
		green_channel_only(bmp);
		save = args[1].split("\\.")[0];
		save+="_YCbCr_only_Cb";
		save+='.'+args[1].split("\\.")[1];
		save_bmp(bmp, save);

		//reset bmp
		bmp = open_bmp(args[0]);

		bmp = convert_RGB_to_YCbCr(bmp);
		blue_channel_only(bmp);
		save = args[1].split("\\.")[0];
		save+="_YCbCr_only_Cr";
		save+='.'+args[1].split("\\.")[1];
		save_bmp(bmp, save);

		//reset bmp
		bmp = open_bmp(args[0]);

		bmp = lumin(bmp);
		String lumin_save = args[1].split("\\.")[0];
		lumin_save+="_luminance_all_channels";
		lumin_save+='.'+args[1].split("\\.")[1];
		save_bmp(bmp, lumin_save);
		/* Aufgabe 2 Y-Only Image Histogramm */

		BmpImage lumi = open_bmp(lumin_save);
		int[] datas = BrightnessHistogramm(lumi);
		save_histo_data(datas);
		Histogramm(lumi, datas);
		save = args[1].split("\\.")[0];
		save+="_Histogramm";
		save+='.'+args[1].split("\\.")[1];

		save_bmp(lumi, save);

		/*Aufgabe 3*/
		//reset
		lumi = open_bmp(lumin_save);
		System.out.println("Mittlere Helligkeit: "+MittlereHelligkeit(lumi));

		System.out.println("Kontrast: "+Kontrast(lumi));

		int[] hs = new int[]{-80,-60,-40,-20,20,40,60,80};
		for (int i = 0; i<hs.length; i++){
			int h = hs[i];
			//reset
			lumi = open_bmp(lumin_save);
			change_brightness(lumi, h);
			save = args[1].split("\\.")[0];
			save+="_Brightness_"+h;
			save+='.'+args[1].split("\\.")[1];

			save_bmp(lumi, save);
			if (i == 0 || i==hs.length-1){
				Histogramm(lumi, BrightnessHistogramm(lumi));
				save = args[1].split("\\.")[0];
				save+="_HistogrammBrightness_"+h;
				save+='.'+args[1].split("\\.")[1];

				save_bmp(lumi, save);

			}

		}
		/*Kontrast */
		double[] ks = new double[]{0.2,0.4,0.8,1.0,1.5,2.5,5.0,10.0};
		for (int i = 0; i<ks.length; i++){
			double k = ks[i];
			//reset
			lumi = open_bmp(lumin_save);
			change_contrast(lumi, k);
			save = args[1].split("\\.")[0];
			save+="_Kontrast_"+k;
			save+='.'+args[1].split("\\.")[1];

			save_bmp(lumi, save);
			if (i == 0 || i==hs.length-1){
				Histogramm(lumi, BrightnessHistogramm(lumi));
				save = args[1].split("\\.")[0];
				save+="_HistogrammKontrast_"+k;
				save+='.'+args[1].split("\\.")[1];

				save_bmp(lumi, save);

			}

		}


		/*

		bmp = convert_RGB_to_YCbCr(bmp);
		bmp = convert_YCbCR_to_RGB(bmp);
		save_bmp(red_channel_only(bmp), args[1].split("\\.")[0]+"_RGB_YCbCr_RGB."+args[1].split("\\.")[1]);

		//BmpImage bmp_gray = grayscale(bmp);
		save_bmp(red_channel_only(bmp), args[1].split("\\.")[0]+"_only_red."+args[1].split("\\.")[1]);
		bmp = open_bmp(args[0]);

		save_bmp(green_channel_only(bmp), args[1].split("\\.")[0]+"_only_green."+args[1].split("\\.")[1]);
		bmp = open_bmp(args[0]);

		save_bmp(blue_channel_only(bmp), args[1].split("\\.")[0]+"_only_blue."+args[1].split("\\.")[1]);
		bmp = open_bmp(args[0]);

		 */

	}
}
