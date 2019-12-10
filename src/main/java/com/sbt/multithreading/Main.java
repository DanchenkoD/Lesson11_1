package com.sbt.multithreading;

import java.io.*;
import java.util.*;

public class Main{
    static class CommonObject
    {
        int counter = 0;
    }

    static class ListObject1
    {
        LinkedList<String> inName = new LinkedList<>();;
    }

    static class ListObject2
    {
        LinkedList<ArrayList<String>> inFile = new LinkedList<>();;
    }

    static class CounterThread implements Runnable
    {
        CommonObject res;
        String folder;
        String file;
        CounterThread(CommonObject res, String folder, String file)
        {
            this.res = res;
            this.folder=folder;
            this.file=file;
        }
        @Override
        public void run()
        {
            HashMap<String, Integer> words = new HashMap<>();
            try {
                File file_ = new File(folder + "\\" + file);
                //System.out.println(file_.getName() + file_.getPath() + file_.getAbsolutePath());
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file_), "windows-1251"));
                String[] splitted;
                int cnt;
                cnt = 0;
                //LinkedList<String> words = new LinkedList<>();

                String line = reader.readLine();
                while (line != null) {
                    //line=line.replace(",","").replace(".","");
                    splitted = line.replace(",", "").replace(".", "").replace("  ", " ").split(" ");
                    for (int i = 0; i < splitted.length; i++) {
                        if (words.containsKey(splitted[i].toLowerCase())) {
                            int value = words.get(splitted[i].toLowerCase());
                            words.put(splitted[i].toLowerCase(), value + 1);
                        } else {
                            words.put(splitted[i].toLowerCase(), 1);

                        }
                        cnt++;
                    }
                    line = reader.readLine();
                }
                //System.out.println("Количество слов: " + cnt + "; различных слов: " +words.size() + "; поток: " +Thread.currentThread().getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized(res) {
                res.counter+=words.size();
            }
            //return words.size();
        }


    }
    public static int Run(String folder, String file) {
        //HashSet<String> words = new HashSet<>();
        HashMap<String, Integer> words = new HashMap<>();
        try {
            File file_ = new File(folder + "\\" + file);
            //System.out.println(file_.getName() + file_.getPath() + file_.getAbsolutePath());
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file_), "windows-1251"));
            String[] splitted;
            int cnt;
            cnt = 0;
            //LinkedList<String> words = new LinkedList<>();

            String line = reader.readLine();
            while (line != null) {
                //line=line.replace(",","").replace(".","");
                splitted = line.replace(",", "").replace(".", "").replace("  ", " ").split(" ");
                for (int i = 0; i < splitted.length; i++) {
                    if (words.containsKey(splitted[i].toLowerCase())) {
                        int value = words.get(splitted[i].toLowerCase());
                        words.put(splitted[i].toLowerCase(), value + 1);
                    } else {
                        words.put(splitted[i].toLowerCase(), 1);

                    }
                    cnt++;
                }
                line = reader.readLine();
            }
            //System.out.println("Количество слов: " + cnt + "; различных слов: " +words.size() );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words.size();
    }


    static class CounterThreadReader implements Runnable {

        String folder;
        String file = "";
        ListObject1 inFile;
        ListObject2 outFile;
        ArrayList<String> fileStr;
        //List<String> fileStr;

        CounterThreadReader( String folder, ListObject1 inFile, ListObject2 outFile/*, String file*/) {
            this.folder = folder;
            //this.file=file;
            //this.fileStr ;
            this.inFile=inFile;
            this.outFile=outFile;

        }

        @Override
        public void run() {
            //System.out.println(" поток:" + Thread.currentThread().getName()+";   ");
            synchronized (inFile) {
                if (!inFile.inName.isEmpty()) {
                    this.file = inFile.inName.get(0);
                    inFile.inName.remove(0);
                }
            }
            //System.out.println(" поток:" + Thread.currentThread().getName()+";  Файл: "+file);
            //Thread.sleep(1000L);


            while (file != "") {
                fileStr = new ArrayList();
                //fileStr.clear();
                try {
                    File file_ = new File(folder + "\\" + file);
                    //System.out.println(file_.getName() + file_.getPath() + file_.getAbsolutePath());
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(file_), "windows-1251"));
                    int cnt;
                    cnt = 0;
                    //LinkedList<String> words = new LinkedList<>();

                    String line = reader.readLine();
                    fileStr.add(file);
                    while (line != null) {
                        fileStr.add(line);
                                cnt++;
                        line = reader.readLine();
                    }

                    //System.out.println("Количество строк: " + cnt +"; Файл: " + Thread.currentThread().getName()+"; поток: "+file);
                    file="";
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*synchronized (res) {
                    res.counter += words.size();
                }*/
                synchronized (outFile) {
                    outFile.inFile.add(fileStr);
                   /* System.out.println( outFile.inFile.size()+" " + fileStr.get(0));
                    for (int i = 0; i < outFile.inFile.size(); i++) {
                        System.out.println("reader : "+outFile.inFile.get(i).get(0) +"  i = "+i);
                    }*/

                }
                synchronized (inFile) {
                    if (!inFile.inName.isEmpty()) {
                        this.file = inFile.inName.get(0);
                        inFile.inName.remove(0);
                    }
                }

                //return words.size();
            }

        }
    }





    static class CounterThreadWorker implements Runnable
    {
        CommonObject res;
        ListObject2 outFile;
        ArrayList<String> fileStr;
        CounterThreadWorker(CommonObject res,  ListObject2 outFile)
        {
            this.res = res;
            this.outFile=outFile;
        }
        @Override
        public void run()
        {
            HashMap<String, Integer> words ;

                String[] splitted;
                int cnt;
                cnt = 0;
                //LinkedList<String> words = new LinkedList<>();
            /*for (int i = 0; i < outFile.inFile.size(); i++) {
                System.out.println("outFile.inFile.size(): "+outFile.inFile.get(i).get(0) +"  i = "+i);
            }*/
            synchronized (outFile) {
                if (!outFile.inFile.isEmpty()) {
                    this.fileStr = outFile.inFile.get(0);
                    outFile.inFile.remove(0);
                    //System.out.println("outFile.inFile.size(): " + outFile.inFile.size() +"  " + outFile.inFile.get(0).get(0));
                }
            }
            //System.out.println("Имя файла: " + fileStr.get(0) + "; поток: " +Thread.currentThread().getName());
                //String line = reader.readLine();

            while (fileStr != null) {
                cnt = 0;
                words = new HashMap<>();
                for (int j = 1; j < fileStr.size(); j++) {


                    //line=line.replace(",","").replace(".","");
                    splitted = fileStr.get(j).replace(",", "").replace(".", "").replace("  ", " ").split(" ");
                    for (int i = 0; i < splitted.length; i++) {
                        if (words.containsKey(splitted[i].toLowerCase())) {
                            int value = words.get(splitted[i].toLowerCase());
                            words.put(splitted[i].toLowerCase(), value + 1);
                        } else {
                            words.put(splitted[i].toLowerCase(), 1);

                        }
                    }
                    cnt++;
                }
                //System.out.println("Количество строк: " + cnt + "; различных слов: " + words.size() + "; поток: " + Thread.currentThread().getName() + "; Имя файла: " + fileStr.get(0));

                synchronized (res) {
                    res.counter += words.size();
                }
                fileStr=null;
                synchronized (outFile) {
                    if (!outFile.inFile.isEmpty()) {
                        this.fileStr = outFile.inFile.get(0);
                        outFile.inFile.remove(0);
                        //System.out.println("outFile.inFile.size(): " + outFile.inFile.size() +"  " + outFile.inFile.get(0).get(0));
                    }
                }

            }
            //return words.size();
        }


    }

    public static void main(String[] args) throws IOException, InterruptedException {

        String folderName = "src\\main\\resources";
        File folder = new File(folderName);

        String[] files = folder.list(new FilenameFilter() {

            @Override
            public boolean accept(File folder, String name) {
                return name.endsWith(".txt");
            }

        });

        int allWord, wordCnt;
        allWord = 0;
        long start = System.currentTimeMillis();
        for (String fileName : files) {
            wordCnt = Run(folderName, fileName);
            System.out.println("File: " + fileName + " слов:" + wordCnt);
            allWord += wordCnt;
        }
        System.out.println("Всего слов:" + allWord);
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Затрачено времени:" + timeConsumedMillis);

        //int allWord, wordCnt;
        CommonObject commonObject= new CommonObject();
        allWord = 0;
        start = System.currentTimeMillis();
        int i;
        i=0;
        List<Thread> threads = new LinkedList<>();
        for (String fileName : files) {
            Thread t;
            t = new Thread(new CounterThread(commonObject,folderName, fileName));
            t.setName("Поток " + i);
            threads.add(t);
            i++;
            t.start();
            //t.join();
        }
        for ( Thread t : threads )
            t.join();

        System.out.println("Всего слов многопоточно:" + commonObject.counter);
        finish = System.currentTimeMillis();
        timeConsumedMillis = finish - start;
        System.out.println("Затрачено времени многопоточно: " + timeConsumedMillis);

/*************************************************************************************************************/
        commonObject.counter=0;
        ListObject1 listObject1 = new ListObject1();
        ListObject2 listObject2 = new ListObject2();
        allWord = 0;
        start = System.currentTimeMillis();
        //int i;
        i=0;
        //List<Thread> threads = new LinkedList<>();
        threads.clear();
        for (String fileName : files) {
            listObject1.inName.add(fileName);
        }
        for (int j = 0; j < 15; j++) {

            Thread t;
            t = new Thread(new CounterThreadReader(folderName,listObject1,listObject2));
            t.setName("Поток " + i);
            //System.out.println("Поток " + i);
            threads.add(t);
            i++;
            t.start();
            //t.join();
        }

        for (int j = 0; j < 15; j++) {

            Thread t;
            t = new Thread(new CounterThreadWorker (commonObject,listObject2));
            t.setName("Поток " + i);
            //System.out.println("Поток " + i);
            threads.add(t);
            i++;
            t.start();
            //t.join();
        }


        for ( Thread t : threads )
            t.join();

        System.out.println("Всего слов многопоточно 2:" + commonObject.counter);
        finish = System.currentTimeMillis();
        timeConsumedMillis = finish - start;
        System.out.println("Затрачено времени многопоточно 2: " + timeConsumedMillis);

    }
}
