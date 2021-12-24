package org.ensaf.blocker.engine;


public class Test {
    public static void main(String[] args) {


//        List<String> paths = new ArrayList<>();
//        paths.add("C:\\Users\\abdel\\Desktop\\Les Rapport\\document1.pdf");
//        ArrayList<ArrayList<Plagiarism>> result = new ArrayList<>();
//        ArrayList<String> filenames = new ArrayList<>();
//        File file1 = new File("C:\\Users\\abdel\\Desktop\\Les Rapport\\document1.pdf");
//
//        int i = 1;
//        for (String path : paths) {
//            File file2 = new File(path);
//            filenames.add(file2.getName());
//
//            try (PDDocument loader1 = PDDocument.load(file1); PDDocument loader2 = PDDocument.load(file2)) // try with-resource (Initiate the PDFBox library)
//            {
//                PDFTextStripper txtStripper1 = new PDFTextStripper();
//
//                String content1 = txtStripper1.getText(loader1);
//                String content2 = txtStripper1.getText(loader2);
//
//                // Checking for plagiarism (Problems)
//                if (content1.length() < content2.length()) {
//                    result.add(Paragraph.check(content1, content2));
//                } else
//                    result.add(Paragraph.check(content2, content1));
//
//                i++;
//            } catch (IOException exception) {
//                System.out.println("I/O Error : " + exception);
//            }
//        }
    }
}
