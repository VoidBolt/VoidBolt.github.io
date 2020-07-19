package wave_io;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static java.lang.Math.abs;



public class wave_io 
{
	public static WavFile wav_open(String input) throws IOException, WavFileException {
		return WavFile.read_wav(input);
	}
	public static void wav_save(WavFile wavFile, String path) throws IOException, WavFileException {
		int validBits = wavFile.getValidBits();
		long sampleRate = wavFile.getSampleRate();
		long numFrames = wavFile.getNumFrames();
		int channels = wavFile.getNumChannels();
		int samples = (int)numFrames*channels;
		WavFile.write_wav(path, channels, numFrames, validBits, sampleRate, wavFile.sound);
	}
	public static void wav_save(int validBits, long sampleRate, long numFrames, int channels, int samples, short[] wav, String path) throws IOException, WavFileException {
		WavFile.write_wav(path, channels, numFrames, validBits, sampleRate, wav);
	}
	/*
	1 a) -> Kirrfaktor tabelle für [3,6,9]db
	 */
	public static WavFile verstaerken(WavFile wavFile, int db){

		int validBits = wavFile.getValidBits();
		long sampleRate = wavFile.getSampleRate();
		long numFrames = wavFile.getNumFrames();
		int channels = wavFile.getNumChannels();
		int samples = (int)numFrames*channels;

		short upper_bounds = 32767;
		short lower_bounds = -32768;

		for (int i = 0; i < samples;i++){
			short sample = wavFile.sound[i];
			double verstaerkungsFaktor = Math.pow(10, ((double)db/20));
			double verstaerkung = sample * verstaerkungsFaktor;

			short new_sample = (short)Math.max(lower_bounds, Math.min(upper_bounds, verstaerkung));

			wavFile.sound[i] = new_sample;
		}
		return wavFile;
	}

	/*
	2 a) -> mono echo und stereo echo
	 */
	public static WavFile mono_echo(WavFile wavFile, double delay) throws IOException, WavFileException {
		int validBits = wavFile.getValidBits();
		long sampleRate = wavFile.getSampleRate();
		long numFrames = wavFile.getNumFrames();
		int channels = wavFile.getNumChannels();
		int samples = (int)numFrames*channels;

		int N = (int)((delay/1000)*sampleRate);
		double verstaerkung = 0.6;

		short[] echo = new short[wavFile.sound.length];
		for(int i = 0; i < samples; i++){
			if (i >= N){
				echo[i] = (short) ((wavFile.sound[i]*0.5) + (0.5 * verstaerkung * wavFile.sound[i-N]));
			}else{
				echo[i] = (short)(wavFile.sound[i]*0.5);
			}
		}

		wav_save(validBits, sampleRate, numFrames, channels, samples, echo,"C:\\tmp\\mono_echo.wav");
		return wav_open("C:\\tmp\\mono_echo.wav");
	}
	public static WavFile stereo_echo(WavFile wavFile, double delay) throws IOException, WavFileException {
		int validBits = wavFile.getValidBits();
		long sampleRate = wavFile.getSampleRate();
		long numFrames = wavFile.getNumFrames();
		int channels = wavFile.getNumChannels();
		int samples = (int)numFrames*channels;

		int N = (int)((delay/1000)*sampleRate);
		N *= 2;
		double verstaerkung = 0.6;

		short[] echo = new short[wavFile.sound.length];
		for(int i = 0; i < samples; i++){
			if (i >= N){
				echo[i] = (short) ((wavFile.sound[i]*0.5) + (0.5 * verstaerkung * wavFile.sound[i-N]));
			}else{
				echo[i] = (short)(wavFile.sound[i]*0.5);
			}
		}

		wav_save(validBits, sampleRate, numFrames, channels, samples, echo,"C:\\tmp\\stereo_echo.wav");
		return wav_open("C:\\tmp\\stereo_echo.wav");
	}
	/*
	3 a)
	 */
	public static WavFile filter_add_mono(WavFile wavFile) throws IOException, WavFileException {
		int validBits = wavFile.getValidBits();
		long sampleRate = wavFile.getSampleRate();
		long numFrames = wavFile.getNumFrames();
		int channels = wavFile.getNumChannels();
		int samples = (int)numFrames*channels;

		short[] wav = wavFile.sound;
		short[] filtered_wav = new short[wav.length];

		int N = 1;
		for (int i = N; i < wav.length; i++){
			filtered_wav[i] = (short) (wav[i] * 0.5 + 0.45 * wav[i-N]);
		}

		wav_save(validBits, sampleRate, numFrames, channels, samples, filtered_wav,"C:\\tmp\\filter_add.wav");
		return wav_open("C:\\tmp\\filter_add.wav");
	}
	public static WavFile filter_sub_mono(WavFile wavFile) throws IOException, WavFileException {
		int validBits = wavFile.getValidBits();
		long sampleRate = wavFile.getSampleRate();
		long numFrames = wavFile.getNumFrames();
		int channels = wavFile.getNumChannels();
		int samples = (int)numFrames*channels;

		short[] wav = wavFile.sound;
		short[] filtered_wav = new short[wav.length];

		int N = 1;
		for (int i = N; i < wav.length; i++){
			filtered_wav[i] = (short) (wav[i] * 0.5 - 0.45 * wav[i-N]);
		}

		wav_save(validBits, sampleRate, numFrames, channels, samples, filtered_wav,"C:\\tmp\\filter_sub.wav");
		return wav_open("C:\\tmp\\filter_sub.wav");
	}

	public static void main2(String[] args){
		if (args.length < 1) {
			try { throw new WavFileException("At least one filename specified  (" + args.length + ")"); }
			catch (WavFileException e1) { e1.printStackTrace(); }
		}
		String inFilename = args[0];
		try {
			System.out.println("Loading File from "+inFilename);
			WavFile wavFile = wav_open(inFilename);
			WavFile rauschen = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\rauschen.wav");
			WavFile Noise = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\Noise.wav");
			WavFile sinewave1khz = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\sine_1k.wav");

			WavFile music = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\musik_Marcel_Pulletz.wav");
			WavFile speech = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\musik_Marcel_Pulletz.wav");

			/* verstärke music 3,6,9, 12db */
			int increase = 3;
			String outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\music_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile verstarkt3db = verstaerken(wavFile, increase);
			wav_save(verstarkt3db, outFilePath);

			//reset
			wavFile = wav_open(inFilename);
			increase = 6;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\music_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile verstarkt6db = verstaerken(wavFile, increase);
			wav_save(verstarkt6db, outFilePath);

			//reset
			wavFile = wav_open(inFilename);
			increase = 9;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\music_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile verstarkt9db = verstaerken(wavFile, increase);
			wav_save(verstarkt9db, outFilePath);

			//reset
			wavFile = wav_open(inFilename);
			increase = 12;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\music_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile verstarkt12db = verstaerken(wavFile, increase);
			wav_save(verstarkt12db, outFilePath);

			/* verstärke noise 3,6,9db */
			//WavFile Noise = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\Noise.wav");
			increase = 3;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\Noise_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile Noise_verstarkt3db = verstaerken(Noise, increase);
			wav_save(Noise_verstarkt3db, outFilePath);

			Noise = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\Noise.wav");
			increase = 6;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\Noise_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile Noise_verstarkt6db = verstaerken(Noise, increase);
			wav_save(Noise_verstarkt6db, outFilePath);

			Noise = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\Noise.wav");
			increase = 9;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\Noise_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile Noise_verstarkt9db = verstaerken(Noise, increase);
			wav_save(Noise_verstarkt9db, outFilePath);

			Noise = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\Noise.wav");
			increase = 12;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\Noise_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile Noise_verstarkt12db = verstaerken(Noise, increase);
			wav_save(Noise_verstarkt12db, outFilePath);

			/* verstärke 1khz sine wave 3,6,9, 12db */
			sinewave1khz = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\sine_1k.wav");

			increase = 3;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\sinewave1khz_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile sine_verstarkt3db = verstaerken(sinewave1khz, increase);
			wav_save(sine_verstarkt3db, outFilePath);

			sinewave1khz = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\sine_1k.wav");

			increase = 6;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\sinewave1khz_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile sine_verstarkt6db = verstaerken(sinewave1khz, increase);
			wav_save(sine_verstarkt6db, outFilePath);

			sinewave1khz = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\sine_1k.wav");

			increase = 9;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\sinewave1khz_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile sine_verstarkt9db = verstaerken(sinewave1khz, increase);
			wav_save(sine_verstarkt9db, outFilePath);

			sinewave1khz = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\sine_1k.wav");

			increase = 12;
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\sinewave1khz_verstarkt"+increase+"db.wav";
			System.out.println("Saving File to " + outFilePath);

			WavFile sine_verstarkt12db = verstaerken(sinewave1khz, increase);
			wav_save(sine_verstarkt12db, outFilePath);
			/*Aufgabe 2*/
			double delay = 10;
			for (double d : new double[]{10,100,200,1000}){
				music = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\musik_Marcel_Pulletz.wav");
				WavFile mono_echo_music = mono_echo(music, d);
				outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\mono_echo_music"+d+".wav";
				wav_save(mono_echo_music, outFilePath);

				music = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\musik_Marcel_Pulletz.wav");
				WavFile stereo_echo_music = stereo_echo(music,d);
				outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\stereo_echo_music"+d+".wav";
				wav_save(stereo_echo_music, outFilePath);

				speech = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\sprache_Marcel_Pulletz.wav");
				WavFile mono_echo_speech = mono_echo(speech, d);
				outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\mono_echo_speech_"+d+"ms.wav";
				wav_save(mono_echo_speech, outFilePath);

				speech = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\sprache_Marcel_Pulletz.wav");
				WavFile stereo_echo_speech = stereo_echo(speech, d);
				outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\stereo_echo_speech_"+d+"ms.wav";
				wav_save(stereo_echo_speech, outFilePath);
			}



			/*Aufgabe 3*/

			rauschen = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\rauschen.wav");
			WavFile filtered_rauschen_add = filter_add_mono(rauschen);
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\filtered_rauschen_add.wav";
			wav_save(filtered_rauschen_add, outFilePath);

			rauschen = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\rauschen.wav");
			WavFile filtered_rauschen_sub = filter_sub_mono(rauschen);
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\filtered_rauschen_sub.wav";
			wav_save(filtered_rauschen_sub, outFilePath);


			Noise = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\Noise.wav");
			WavFile filtered_Noise_add = filter_add_mono(Noise);
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\filtered_Noise_add.wav";
			wav_save(filtered_Noise_add, outFilePath);

			Noise = wav_open("C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\Noise.wav");
			WavFile filtered_Noise_sub = filter_sub_mono(Noise);
			outFilePath = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\test\\filtered_Noise_sub.wav";
			wav_save(filtered_Noise_sub, outFilePath);



		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (WavFileException e1) {
			e1.printStackTrace();
		}
	}
	public static void main(String[] args) 
	{

		int samples=0;
		int validBits=0;
		long sampleRate=0;
		long numFrames=0; // frames/bytes per second
		int numChannels=0;

		String inFilename = null;
		String outFilename = null;

		short[] new_samples;
		short[] down_samples;
		short[] bit_reduced_samples;
		short[] bit_reduced_difference;

		if (args.length < 1) {
			try { throw new WavFileException("At least one filename specified  (" + args.length + ")"); }
			catch (WavFileException e1) { e1.printStackTrace(); }
		}
	
		// Samples in dem Array readWavFile.sound
		
		inFilename=args[0];
		
		// Implementierung bei einem Eingabeparameter

	     WavFile readWavFile = null;
		try {
			readWavFile = WavFile.read_wav(inFilename);
			
			// headerangaben
			numFrames = readWavFile.getNumFrames(); //
			numChannels = readWavFile.getNumChannels();
			samples = (int)numFrames*numChannels; //number of samples, we need to know the length of the signal
			validBits = readWavFile.getValidBits(); // N =^ multiple of 8 usually
			sampleRate = readWavFile.getSampleRate(); //period length used to "observe" the signal

			//ValidBits determine the range of the discrete signal which [-1V, 1V] -> [-2^(validBits-1),2^(validBits-1)-1]
			//the discrete signal would not have any meaning without the previously created header data, because
			//we need to know the SampleRate and the encoded bitrange (validbits) to make sense of the discrete data

			//also necessary to know is the amount of used channels, f.e.: channels=2 which would mean that the data comes
			//in from channel 1 and then from channel 2; alternating -> different channel but same time k=0
			//so for each channel that exists we need to take the information of the specific channel to make sense
			//of the timeframe in our analog audio file and understand the discrete data as well.
			// 2a Samples schreiben

			String s = "";
			int fa_max = 0;
			new_samples = new short[samples];
			String path = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\ue1";
			Writer fileWriter = new FileWriter(path+"\\out.txt");

			for (int i=0; i < samples;i++) {
				short val = readWavFile.sound[i];
				fileWriter.write(val+System.lineSeparator());
				if (abs(val)>fa_max){
					fa_max = abs(val);
				}
			}
			fileWriter.close();
			System.out.println("Samples: " +samples + ", sampleRate: "+sampleRate);
			long t0 = samples*sampleRate;
			System.out.println("T0 = samples*sampleRate = "+t0);

			System.out.println("Fa_max = "+fa_max);
			long fa = fa_max*2 *4;
			long f0 = 1/t0;
			System.out.println("f0 = "+f0+" => f0 = Fa_max/samples = "+ fa/samples);

			System.out.println();
		    
		    if (args.length == 1) 
				System.exit(0);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (WavFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		// Implementierung bei Ein-und Ausgabeparameter
		
		outFilename=args[1];
		try {
			int down_sample = 6;
			down_samples = new short[(int)(samples/down_sample)];
			// 2e Downsampling
			for (int i=0; i+down_sample < samples;i+=down_sample) {
				//DownSampling indem wir jeden zweiten wert verwerfen und ein neues signal mit der hälte der gesampleten daten zu erhalten
				short val = readWavFile.sound[i];
				down_samples[i/down_sample] = val;
			}
			WavFile.write_wav(outFilename, numChannels, numFrames/down_sample, validBits, sampleRate/down_sample, down_samples);//sampleRate,readWavFile.sound);

 			// 3b Bitreduzierung
			int valid_range = (int)Math.pow(2.0, validBits);
			int upper_bound = valid_range - (int)valid_range/2;
			int lower_bound = -upper_bound;
			System.out.println("lower/upper bound: "+ lower_bound+" "+upper_bound);

			int reduced_bits = 9;
			int max_freq = (int)Math.pow(2, validBits-reduced_bits)/2;
			System.out.println("Max Freq: "+ max_freq);
			bit_reduced_samples = new short[samples];

			for (int i=0; i < samples;i++) {
				short val = readWavFile.sound[i];
				val /= Math.pow(2,reduced_bits);
				val *= reduced_bits;
				/*while (Math.abs(val) > max_freq){
					val /= 2;
				}*/
				bit_reduced_samples[i] = val;
			}


			// 3e Bitreduzierung Differenz
			bit_reduced_difference = new short[samples];
			reduced_bits = 8;
			for (int i=0; i < samples;i++) {
				short val = readWavFile.sound[i];
				short new_val = (short)(val / Math.pow(2,reduced_bits));

				short diff = (short)((val-new_val) * Math.pow(2,reduced_bits));
				bit_reduced_difference[i] = diff;

			}
			
			//WavFile.write_wav(outFilename, numChannels, numFrames, validBits, sampleRate, bit_reduced_samples);//sampleRate,readWavFile.sound);
			//WavFile.write_wav(outFilename, numChannels, numFrames, validBits, sampleRate, bit_reduced_difference);//sampleRate,readWavFile.sound);

		}			
		catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
}
