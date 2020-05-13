public class Main {

    public static void main( String[] args ) {
        String ext = ".wav";
        String filename = "musik_Marcel_Pulletz";
        String filename2 = "sprache_Marcel_Pulletz";
        String sinewave_hi = "sine_hi03";
        String sinewave_lo = "sine_lo00";

        String filename3 = "musik_Marcel_Pulletz_reduced8bit";
        String filename4 = "sprache_Marcel_Pulletz_9bitreduced";

        String filename_new = filename+"_new";
        String filename2_new = filename+"_new";

        String folder2 = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\";
        String folder = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\ue1\\";

        /*
        System.out.println("Musik:");
        String input = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\ue1\\"+filename+ext;
        //String output = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\"+filename_new+ext;
        wave_io.wave_io.main(new String[]{input});
        */
        System.out.println("Sprache:");


        String input2 = folder+sinewave_lo+ext;
        String output2 = "C:\\Users\\Kathr\\Documents\\Beuth\\SS20\\MedienTechnologien\\webseite\\out\\"+sinewave_lo+"_downsample3"+ext;
        wave_io.wave_io.main(new String[]{input2, output2});
    }
}
