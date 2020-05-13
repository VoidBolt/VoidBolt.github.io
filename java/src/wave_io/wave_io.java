package wave_io;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static java.lang.Math.abs;

public class wave_io 
{
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
