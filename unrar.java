import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

/**
 * <p>
 * Title: ��ѹ���ļ�
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: yourcompany
 * </p>
 * 
 * @author yourcompany
 * @version 1.0
 */
public class CompressFile {
    

    /**
     * ѹ���ļ�
     * 
     * @param srcfile
     *            File[] ��Ҫѹ�����ļ��б�
     * @param zipfile
     *            File ѹ������ļ�
     */
    public static void ZipFiles(java.io.File[] srcfile, java.io.File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                String str = srcfile[i].getName();
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            System.out.println("ѹ�����.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * zip��ѹ��
     * 
     * @param zipfile
     *            File ��Ҫ��ѹ�����ļ�
     * @param descDir
     *            String ��ѹ���Ŀ��Ŀ¼
     */
    public static void unZipFiles(java.io.File zipfile, String descDir) {
        try {
            ZipFile zf = new ZipFile(zipfile);
            for (Enumeration entries = zf.getEntries(); entries
                    .hasMoreElements();) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String zipEntryName = entry.getName();
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
                //System.out.println("��ѹ�����.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    /** 
    * ����ԭʼrar·������ѹ��ָ���ļ�����.      
    * @param srcRarPath ԭʼrar·�� 
    * @param dstDirectoryPath ��ѹ�����ļ���      
    */
    public static void unRarFile(String srcRarPath, String dstDirectoryPath) {
        if (!srcRarPath.toLowerCase().endsWith(".rar")) {
            System.out.println("��rar�ļ���");
            return;
        }
        File dstDiretory = new File(dstDirectoryPath);
        if (!dstDiretory.exists()) {// Ŀ��Ŀ¼������ʱ���������ļ���
            dstDiretory.mkdirs();
        }
        Archive a = null;
        try {
            a = new Archive(new File(srcRarPath));
            if (a != null) {
                a.getMainHeader().print(); // ��ӡ�ļ���Ϣ.
                FileHeader fh = a.nextFileHeader();
                while (fh != null) {
                    if (fh.isDirectory()) { // �ļ��� 
                        File fol = new File(dstDirectoryPath + File.separator
                                + fh.getFileNameString());
                        fol.mkdirs();
                    } else { // �ļ�
                        File out = new File(dstDirectoryPath + File.separator
                                + fh.getFileNameString().trim());
                        //System.out.println(out.getAbsolutePath());
                        try {// ֮������ôдtry������Ϊ��һ�����������쳣����Ӱ�������ѹ. 
                            if (!out.exists()) {
                                if (!out.getParentFile().exists()) {// ���·�����ܶ༶��������Ҫ������Ŀ¼. 
                                    out.getParentFile().mkdirs();
                                }
                                out.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            a.extractFile(fh, os);
                            os.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    fh = a.nextFileHeader();
                }
                a.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}