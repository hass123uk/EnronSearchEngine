package Utilities;

/**
 *
 * @author oe
 */
import java.io.File;
import java.util.Date;

public class RenameToTxtCrawler {

    static int count = 0;

    public RenameToTxtCrawler() {
//        Scanner console = new Scanner(System.in);
//        System.out.print("Directory to crawl? ");
//        String directoryName = console.nextLine();

        File f = new File("/Users/HassanMahmud/Documents/CrawlerTest/maildir");
        crawl(f);
    }

    public static void crawl(File f) {
        Date start = new Date();

        crawl(f, "");
        Date end = new Date();
        long timeSpan = (end.getTime() - start.getTime()) / 1000;
        System.out.println("Renaming finished in " + timeSpan + "s. " + count + " docs renamed.");
    }

    private static void crawl(File f, String indent) {
        //System.out.println(indent + f.getName());
        if (f.isDirectory()) {
            File[] subFiles = f.listFiles();
            indent += "    ";
            for (int i = 0; i < subFiles.length; i++) {
                crawl(subFiles[i], indent);
            }

        } else if (f.isFile() && !f.isHidden()) {
            f.renameTo(new File(f.getAbsolutePath() + "txt"));
            count++;
        }

    }
}
