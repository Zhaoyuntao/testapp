package im.turbo.basetools.file;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileUtil {
    public static final String DOCUMENTS_DIR = "documents";

    public static long getFileSize(String filePath) {
        try {
            File f = new File(filePath);
            if (f.isFile()) {
                return f.length();
            }
            return getFileSize(f);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        } catch (Throwable t) {
        }
        return 0;
    }

    public static long getFileSize(File f) throws Exception//取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        if (null == flist)
            return 0;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static FileInputStream openFileInputStream(String pathName) {
        File file = new File(pathName);
        FileInputStream fis = null;
        if (file.exists()) {
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {

            }
        }
        return fis;
    }

    public static FileOutputStream openFile4Write(String pathName) {
        File file = new File(pathName);
        FileOutputStream fos = null;

        if (file.exists()) {
            file.delete();
        }
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {

        }
        return fos;
    }

    public static FileOutputStream openFile4Write(String pathName, boolean bAppend) {
        File file = new File(pathName);
        FileOutputStream fos = null;

        if (file.exists() && bAppend == false) {
            file.delete();
        }
        try {
            File pf = file.getParentFile();
            pf.mkdirs();//避免进程运行过程中删除SD卡目录
            fos = new FileOutputStream(file, bAppend);
        } catch (Exception e) {

        }
        return fos;
    }

    //返回删除的文件的大小
    public static long deleteFile(String filePath) {
        try {
            return removeDir(filePath);
        } catch (Throwable t) {
        }
        return 0;
    }

    //返回删除的文件的大小
    public static long removeDir(String filePath) {
        File f = new File(filePath);
        return removeFile(f);
    }

    //返回删除的文件的大小
    public static long removeFile(File f) {
        if (null == f)
            return 0;
        long size = 0;
        if (f.isFile()) {
            size = f.length();
            f.delete();
            return size;
        }
        File flist[] = f.listFiles();
        if (null == flist) {
            f.delete();
            return size;
        }
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size += removeFile(flist[i]);
            } else {
                size += flist[i].length();
                flist[i].delete();
            }
        }
        f.delete();
        return size;
    }

    public static boolean renameFile(String ofilePath, String nfilePath) {
        return renameFile(ofilePath, nfilePath, false) != null;
    }

    /**
     * @param ofilePath
     * @param nfilePath
     * @param hasUpdateName 是否需要使用 `(` + index + `)` 规则来重命名， true: 防止覆盖已存在的文件
     * @return
     */
    public static String renameFile(String ofilePath, String nfilePath, boolean hasUpdateName) {
        if (null == ofilePath || null == nfilePath)
            return null;
        try {
            File f = new File(ofilePath);
            File f2 = new File(nfilePath);
            f2.getParentFile().mkdirs();

            if (hasUpdateName) {
                File temp = generateFileName(nfilePath);
                if (temp != null) {
                    f2 = temp;
                }
            }
            String suffix = FileUtil.getFileSuffix(f2);

            boolean bret = false;
            if (!FileUtil.hasImage(suffix) && !FileUtil.hasVideo(suffix)) {
                bret = f.renameTo(f2);
            }

            if (bret)
                return f2.getAbsolutePath();
            if (ofilePath.equals(nfilePath))
                return ofilePath;
            //fix: internal storage rename to SDCard Fail
            f2.delete();
            copyFile(f, f2);
            f.delete();
            return f2.getAbsolutePath();
        } catch (Exception e) {

        } catch (Throwable t) {
        }
        return null;
    }

    public static File[] listFilesSortByModify(String dirPath) {
        File fdir = new File(dirPath);

        if (fdir.exists() == false)
            return null;
        File[] files = fdir.listFiles();
        if (files != null && files.length > 0) {
            Arrays.sort(files, new CompratorByLastModified());
        }

        return files;
    }

    public static File[] listFilesSortBySize(String dirPath) {
        File fdir = new File(dirPath);

        if (fdir.exists() == false)
            return null;
        File[] files = fdir.listFiles();
        Arrays.sort(files, new CompratorBySize());

        return files;
    }

    public static String readAssetFileContent(Context context, String filePath) {
        try {
            InputStream is = context.getAssets().open(filePath);
            int fileLen = is.available();
            byte[] data = new byte[fileLen];
            is.read(data);
            return new String(data);
        } catch (Throwable e) {

        }
        return null;
    }

    public static String readFileContent(String filePath) {
        File file = new File(filePath);
        return readFileContent(file);
    }

    public static List<String> readLines(final InputStream input, final Charset encoding) throws IOException {
        final InputStreamReader reader = new InputStreamReader(input, Charset.forName("UTF-8"));
        return readLines(reader);
    }

    public static List<String> readLines(final Reader input) throws IOException {
        final BufferedReader reader = toBufferedReader(input);
        final List<String> list = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static String readFileContent(File file) {
        StringBuilder text = new StringBuilder();
        FileInputStream fileIS = null;
        try {
            fileIS = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileIS));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                //text.append('\n');
            }
        } catch (Throwable e) {
            // 增加异常处理
        } finally {
            try {
                if (null != fileIS) {
                    fileIS.close();
                }
            } catch (IOException e) {

            }
        }
        return text.toString();
    }

    public static void writeFile(String filePath, String data) {
        FileOutputStream outStream = null;
        String tmpFilePath = filePath + ".tmp";
        boolean bOK = false;

        try {
            File file = new File(tmpFilePath);
            String pathDir = file.getParent();
            File dirFile = new File(pathDir);
            if (dirFile.exists() == false) {
                dirFile.mkdirs();
            }
            file.delete();
            outStream = new FileOutputStream(file);
            outStream.write(data.getBytes());
            outStream.flush();
            bOK = true;
        } catch (Exception e) {


        } finally {
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {

                }
            }
            if (bOK) {
                File file = new File(tmpFilePath);
                file.renameTo(new File(filePath));
            }
        }
    }


    static class CompratorByLastModified implements Comparator<File> {
        public int compare(File o1, File o2) {
            File file1 = (File) o1;
            File file2 = (File) o2;
            long diff = file1.lastModified() - file2.lastModified();
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }

        public boolean equals(Object obj) {
            return true; // 简单做法
        }

    }

    static class CompratorBySize implements Comparator<File> {
        public int compare(File o1, File o2) {
            File file1 = (File) o1;
            File file2 = (File) o2;
            long diff = file1.length() - file2.length();
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }

        public boolean equals(Object obj) {
            return true; // 简单做法
        }
    }

    public static String getFileName(String filePathName) {
        if (filePathName != null && !"".equals(filePathName)) {
            int index = filePathName.lastIndexOf('/');
            if (index != -1 && (index + 1) < filePathName.length()) {
                return filePathName.substring(index + 1);
            }
        }
        return "";
    }


    public static boolean copyFile(File sourceFile, File targetFile)
            throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        targetFile.createNewFile();
        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        int icount = 0;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
            icount++;
            if (icount % 5 == 0) {
                outBuff.flush();
            }
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
        return true;
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }


    public static String getFileSuffix(File file) {
        String name = file.getName();
        String[] suffix = name.split("\\.");
        if (suffix.length > 1) {
            return "." + suffix[suffix.length - 1];
        }

        return "";
    }

    public static String getFileSuffix(String name) {
        String[] suffix = name.split("\\.");
        if (suffix.length > 1) {
            return "." + suffix[suffix.length - 1];
        }

        return "";
    }

    public static String getUrlSuffix(String url) {
        String path = Uri.parse(url).getPath();
        if (TextUtils.isEmpty(path)) {
            return "";
        }

        String[] suffix = path.split("\\.");
        if (suffix.length > 1) {
            return "." + suffix[suffix.length - 1];
        }

        return "";
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    public static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024 * 4];
            int readLen;
            while ((readLen = is.read(buf)) != -1) {
                bos.write(buf, 0, readLen);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static File getDocumentCacheDir(Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }

        return file;
    }

    /**
     * @param fullName: xx/xx/abc.txt
     * @return
     */
    @Nullable
    private static File generateFileName(String fullName) {
        File file = new File(fullName);

        if (file.exists()) {
            String path;    // /xx/xx/
            String prefix;  // abc
            String suffix;  // .txt
            int index = fullName.lastIndexOf('/');
            if (index != -1 && (index + 1) < fullName.length()) {
                path = fullName.substring(0, index + 1);
                String name = fullName.substring(index + 1);

                int i = name.lastIndexOf('.');
                if (i > 0) {
                    prefix = name.substring(0, i);
                    suffix = name.substring(i);
                } else {
                    prefix = name;
                    suffix = "";
                }

                index = 0;

                do {
                    index++;
                    fullName = path + prefix + '(' + index + ')' + suffix;
                    file = new File(fullName);
                } while (file.exists());
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }

        return file;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private static String getMIMEType(String fName) {
        String type = "*/*";
        if (TextUtils.isEmpty(fName)) return type;

        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") {
            return type;
        }

        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0])) {
                type = MIME_MapTable[i][1];
            }
        }

        return type;
    }

    public static String getMIMEType(File file) {
        String fName = file.getName();
        return getMIMEType(fName);
    }

    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"", "*/*"}
    };

    public static boolean hasImage(String suffix) {
        for (String item : MIME_ImageTable) {
            if (item.equalsIgnoreCase(suffix)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasVideoByFile(File file) {
        if (!file.exists()) {
            return false;
        }
        String suffix = getFileSuffix(file);
        return hasVideo(suffix);
    }

    public static boolean hasVideo(String suffix) {
        for (String item : MIME_VideoTable) {
            if (item.equalsIgnoreCase(suffix)) {
                return true;
            }
        }
        return false;
    }

    private static final String[] MIME_ImageTable = {
            ".bmp", ".gif", ".jpeg", ".jpg", ".png"
    };

    private static final String[] MIME_VideoTable = {
            ".3gp", ".asf", ".avi", ".m4u", ".m4v", ".mov", ".mp4", ".mpe", ".mpeg", ".mpg", ".mpg4"
    };

    /**
     * 获取相册目录
     *
     * @return
     */
    public static String getGalleryDir() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
    }

//    public static String getFileTimePath() {
//        long serverTimeMillisecond = AppRuntime.getInstance().getServerTimeMillisecond();
//        Date currentTime = new Date(serverTimeMillisecond);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyMM", Locale.US);
//        return formatter.format(currentTime);
//    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", size);
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0f);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / 1024.0f / 1024.0f);
        } else {
            return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }


    public static void writeFile(String filePath, byte[] data) {
        FileOutputStream outStream = null;
        String tmpFilePath = filePath + ".tmp";
        boolean bOK = false;

        try {
            File file = new File(tmpFilePath);
            String pathDir = file.getParent();
            File dirFile = new File(pathDir);
            if (dirFile.exists() == false) {
                dirFile.mkdirs();
            }
            file.delete();
            outStream = new FileOutputStream(file);
            outStream.write(data);
            outStream.flush();
            bOK = true;
        } catch (Exception e) {


        } finally {
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {

                }
            }
            if (bOK) {
                File file = new File(tmpFilePath);
                File newFile = new File(filePath);
                if (newFile.exists()) {
                    newFile.delete();
                }
                file.renameTo(newFile);
            }
        }
    }

}
