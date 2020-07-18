import bmp_io.bmp_io;

import java.io.IOException;

public class Main {


    public static void wave_io_test(){
        String ext = ".wav";
        String filename = "musik_Marcel_Pulletz";
        String filename2 = "sprache_Marcel_Pulletz";
        String sinewave_hi = "sine_hi03";
        String sinewave_lo = "sine_lo00";

        String filename3 = "musik_Marcel_Pulletz_reduced8bit";
        String filename4 = "sprache_Marcel_Pulletz_9bitreduced";

        String filename_new = filename+"_new";
        String filename2_new = filename+"_new";

        //String folder2 = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\";
        String folder = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\audio\\originals\\";

        /*
        System.out.println("Musik:");
        String input = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\ue1\\"+filename+ext;
        //String output = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\"+filename_new+ext;
        wave_io.wave_io.main(new String[]{input});
        */
        //System.out.println("Sprache:");


        //String input2 = folder+sinewave_lo+ext;
        //String output2 = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\"+sinewave_lo+"_downsample3"+ext;

        String music_original = folder + "musik_mono.wav";
        String rauschen_original = folder + "rauschen.wav";

        wave_io.wave_io.main2(new String[]{music_original});
    }
    public static void bmp_io_test() throws IOException {
        String ext = ".bmp";

        String infile = "manmade_Marcel_Pulletz.bmp";
        String outfile = "manmade.bmp";
        String file1 = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\Ue4\\1\\"+infile; //manmade_Marcel_Pulletz.bmp"; //grating_H10.bmp";
        String outfile_full = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\Ue4\\1\\"+outfile;
        bmp_io.main(new String[]{file1, outfile_full});
        String infile2 = "nature_Marcel_Pulletz.bmp";
        String outfile2 = "nature.bmp";
        String file2 = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\Ue4\\1\\"+infile2; //manmade_Marcel_Pulletz.bmp"; //grating_H10.bmp";
        String outfile_full2 = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\Ue4\\1\\"+outfile2;
        bmp_io.main(new String[]{file2, outfile_full2});

    }
    public static void main( String[] args ) throws IOException {
        //bmp_io_test();
        wave_io_test();

    }
}
