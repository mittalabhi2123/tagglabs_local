//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.channels.FileChannel;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//public class SlideShowImagesCopy {
//    Timer timer;
//
//    public SlideShowImagesCopy() {
//        timer = new Timer();
//        timer.schedule(new RemindTask(), 5*1000, 15*60*1000);
//    }
//
//    class RemindTask extends TimerTask {
//        public void run() {
//            File sourceFile = new File(File.separator+"data");
//            File destFile = new File(File.separator+"slideshow");
//            if(!destFile.exists())
//                destFile.mkdirs();
//            for(File file : sourceFile.listFiles()){
//                if (file.isFile()){
//                    try{
//                        
//                    } catch(Exception e){
//                        e.printStackTrace();
//                        continue;
//                    }
//                }
//            }
//        }
//    }
//
//    public static void main(String args[]) {
//        new SlideShowImagesCopy();
//        System.out.format("Task scheduled.%n");
//    }
//    
//    private static void copyFile(File sourceFile, File destFile)
//		throws IOException {
//	if (!sourceFile.exists()) {
//		return;
//	}
//	if (!destFile.exists()) {
//		destFile.createNewFile();
//	}
//	FileChannel source = null;
//	FileChannel destination = null;
//	source = new FileInputStream(sourceFile).getChannel();
//	destination = new FileOutputStream(destFile).getChannel();
//	if (destination != null && source != null) {
//		destination.transferFrom(source, 0, source.size());
//	}
//	if (source != null) {
//		source.close();
//	}
//	if (destination != null) {
//		destination.close();
//	}
//
//}
//}
