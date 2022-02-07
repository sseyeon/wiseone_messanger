package com.messanger.engine.uc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class FileUtils {

    private FileUtils() {}

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    /**
     * 
     * @param size
     * @return
     */
    public static String byteCountToDisplaySize(long size) {
        String displaySize;

        if (size / ONE_GB > 0) {
            displaySize = String.valueOf(size / ONE_GB) + " GB";
        } else if (size / ONE_MB > 0) {
            displaySize = String.valueOf(size / ONE_MB) + " MB";
        } else if (size / ONE_KB > 0) {
            displaySize = String.valueOf(size / ONE_KB) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }

        return displaySize;
    }

    /**
     * 
     * @param url
     * @return
     */
    public static File toFile(URL url) {
        if (url.getProtocol().equals("file") == false) {
            return null;
        } else {
            String filename = url.getFile().replace('/', File.separatorChar);
            return new File(filename);
        }
    }

    /**
     * 
     * @param source
     * @param destinationDirectory
     * @throws IOException
     */
    public static void copyFileToDirectory(File source, File destinationDirectory) throws IOException {
        if (destinationDirectory.exists() && !destinationDirectory.isDirectory()) {
            throw new IllegalArgumentException("Destination is not a directory");
        }

        copy(source, new File(destinationDirectory, source.getName()));
    }

    /**
     * 
     * @param directory
     * @throws IOException
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }
    
    /**
     * 
     * @param path
     * @return
     */
    public static boolean existDirectory(String path) {
        File file = new File(path);
        return existDirectory(file);
    }
    
    public static boolean existDirectory(File path) {
        return path.exists() && path.isDirectory();
    }

    /**
     * 
     * @param directory
     * @throws IOException
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        IOException exception = null;

        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * 파일을 쓴다.
     * @param file
     * @param bytes
     * @return
     * @throws IOException
     */
    public static boolean writeFile(File file, byte[] bytes) throws Exception {
        int len = 0;
        FileChannel fc = null;
        ByteBuffer buf = null;
        FileOutputStream fOut = null;

        try {
        	buf = ByteBuffer.wrap(bytes);
            fOut = new FileOutputStream(file);
            fc = fOut.getChannel();
            len = fc.write(buf);
        } finally {
            if (buf != null)
                buf.clear();
            if (fc != null)
                fc.close();
            if (fOut != null)
                fOut.close();
        }

        return bytes.length == len;
    }

    /**
     * 파일을 읽어 스트링으로 반환
     * @param file
     * @param encoding
     * @return
     * @throws Exception
     */
    public static byte[] readFile(File file) throws Exception {
        long fSize;
        byte[] bytes = null;
        ByteBuffer buf = null;
        FileChannel fc = null;
        FileInputStream fIn = null;

        try {
            fIn = new FileInputStream(file);
            fc = fIn.getChannel();
            fSize = fc.size();
            buf = ByteBuffer.allocate((int) fSize);
            fc.read(buf);
            buf.rewind();
            bytes = buf.array();//, Charset.forName(encoding));
        } finally {
            if (buf != null)
                buf.clear();
            if (fc != null)
                fc.close();
            if (fIn != null)
                fIn.close();
        }

        return bytes;
    }
    
    /**
     * 
     * @param file
     * @throws IOException
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist: " + file);
            }
            if (!file.delete()) {
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * 
     * @param file
     * @throws IOException
     */
    public static void forceDeleteOnExit(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }
    }

    /**
     * 
     * @param directory
     * @throws IOException
     */
    private static void deleteDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectoryOnExit(directory);
        directory.deleteOnExit();
    }

    /**
     * 
     * @param directory
     * @throws IOException
     */
    private static void cleanDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        IOException exception = null;

        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDeleteOnExit(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * 
     * @param path
     * @throws IOException
     */
    public static void forceMkdir(String path) throws IOException {
        FileUtils.forceMkdir(new File(path));
    }
    
    /**
     * 
     * @param directory
     * @throws IOException
     */
    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (directory.isFile()) {
                String message = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else {
            if (false == directory.mkdirs()) {
                String message = "Unable to create directory " + directory;
                throw new IOException(message);
            }
        }
    }

    /**
     * 
     * @param directory
     * @return
     */
    public static long sizeOfDirectory(File directory) {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        long size = 0;

        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory()) {
                size += sizeOfDirectory(file);
            } else {
                size += file.length();
            }
        }

        return size;
    }

    /**
     * 
     * @param file
     * @param reference
     * @return
     */
    public static boolean isFileNewer(File file, File reference) {
        if (reference == null) {
            throw new IllegalArgumentException("No specified reference file");
        }
        if (!reference.exists()) {
            throw new IllegalArgumentException("The reference file '" + file + "' doesn't exist");
        }

        return isFileNewer(file, reference.lastModified());
    }

    /**
     * 
     * @param file
     * @param date
     * @return
     */
    public static boolean isFileNewer(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        }
        return isFileNewer(file, date.getTime());
    }

    /**
     * 
     * @param file
     * @param timeMillis
     * @return
     */
    public static boolean isFileNewer(File file, long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        if (!file.exists()) {
            return false;
        }

        return file.lastModified() > timeMillis;
    }

    /**
     * 
     * @param file
     * @return
     */
    public static String getSansExtension(File file) {
        String name = file.getName();
        int i = name.lastIndexOf('.');

        if (i >= 0) {
            return name.substring(i);
        } else {
            return "";
        }
    }

    /**
     * 
     * @param file
     * @return
     */
    public static String getNameExtension(File file) {
        String name = file.getName();
        int i = name.lastIndexOf('.');

        if (i >= 0) {
            return name.substring(0, i);
        } else {
            return name;
        }
    }

    /**
     * 
     * @param source
     * @param target
     * @throws IOException
     */
    public static void copy(File source, File target) throws IOException {
        FileChannel input = null;
        FileChannel output = null;

        try {
            input = new FileInputStream(source).getChannel();
            output = new FileOutputStream(target).getChannel();

            MappedByteBuffer buffer = input.map(FileChannel.MapMode.READ_ONLY, 0, input.size());
            output.write(buffer);
        } finally {
            if (input != null) {
                input.close();
            }

            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 
     * @param files
     */
    public static void deleteAll(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                deleteAll(file.listFiles());
            }

            file.delete();
        }
    }

}
