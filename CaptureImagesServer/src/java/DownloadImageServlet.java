import java.io.*;

import java.sql.*;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class DownloadImageServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RandomAccessFile raf1=null , raf2 = null;
        try {
//            String uid = request.getHeader("id");
//            String zone = request.getHeader("zone");
//            String fileNames = request.getHeader("fileNames");
//            
//            String result=Utility.getjdbcconnection();
//          if(!result.equalsIgnoreCase(""))
//            throw new RuntimeException("Error:" + result);
//            String imeiNo = request.getParameter("imei");
////            System.out.println("Download request arrived from IMEI........" + imeiNo);
//            if (imeiNo == null || imeiNo.equalsIgnoreCase("")) {
//                return;
//            }
//            Statement stmt = Utility.conn.createStatement();
//            ResultSet rs =
//                stmt.executeQuery("SELECT * FROM admin WHERE imei = '" + imeiNo + "'");
//            short imeiVersionNo = -9999;
//            while (rs.next()) {
//                imeiVersionNo = rs.getShort(9);
//            }
//            if (imeiVersionNo == -9999) {
//                return;
//            }
//            short currentVersion = 0;
//            ResultSet rs1 = stmt.executeQuery("SELECT value FROM codestbl WHERE type = 'movTrmpBanner'");
//            while (rs1.next()) {
//                currentVersion = rs1.getShort(1);
//            }
//
//            if (imeiVersionNo == currentVersion || imeiVersionNo > currentVersion) {
//                return;
//            }
////          System.out.println("currentVerstion= "+currentVersion+";;;;;;;;;;"+"imeiVersionNo= "+imeiVersionNo);
//            ResultSet rs2 =
//                stmt.executeQuery("SELECT * FROM movingtrumphetupdatebanner WHERE version_no <= " + currentVersion + " AND version_no > " + imeiVersionNo + " AND active_status = 1 ORDER BY version_no");
//            List<String> fileNameLst = new ArrayList<String>();
//            LinkedHashMap<String,Integer> fileSizeMap = new LinkedHashMap<String,Integer>();
//            LinkedHashMap<String,String> filePathMap = new LinkedHashMap<String,String>();
//            LinkedHashMap<String,Integer> assoFileSizeMap = new LinkedHashMap<String,Integer>();
//            LinkedHashMap<String,String> assoFilePathMap = new LinkedHashMap<String,String>();
//            LinkedHashMap<String,String> assoFileNameMap = new LinkedHashMap<String,String>();
//            LinkedHashMap<String,String> assoFileTypeMap = new LinkedHashMap<String,String>();
//            LinkedHashMap<String,String> fromToDateMap = new LinkedHashMap<String,String>();
//            LinkedHashMap<String,Integer> versionMap = new LinkedHashMap<String,Integer>();String fileName = "";
//            while (rs2.next()) {
//                fileName = rs2.getString(2);
//                fileNameLst.add(fileName);
//                fileSizeMap.put(fileName, Integer.valueOf(rs2.getInt(3)));
//                assoFileNameMap.put(fileName,rs2.getString(6));
//                assoFileSizeMap.put(fileName, rs2.getInt(7));
//                assoFileTypeMap.put(fileName, rs2.getString(8));
//                filePathMap.put(fileName, rs2.getString(10));
//                assoFilePathMap.put(fileName,rs2.getString(11));
//                fromToDateMap.put(fileName,rs2.getLong(12) + ":" + rs2.getLong(13));
//                versionMap.put(fileName,rs2.getInt(1));
//            }
//            fileName = fileNameLst.get(0);
////            System.out.println("Actual File path......" + (String)filePathMap.get(fileName));
//            File f1 = new File(filePathMap.get(fileName));
//            raf1 = new RandomAccessFile(f1, "r");
//            int size1 = 0;
//            if(request.getParameter("size1") != null)
//                size1 = Integer.parseInt(request.getParameter("size1"));
//            if(size1 > 0)
//                raf1.seek(size1);
//            byte b1[] = new byte[Long.valueOf(f1.length()).intValue() - size1];
////            System.out.println("Pending file Size..........." + b1.length);
////            System.out.println("Complete file Size..........." + f1.length());
//            raf1.readFully(b1);
//            File f2 = new File(assoFilePathMap.get(fileName));
//             raf2 = new RandomAccessFile(f2, "r");
//            int size2 = 0;
//            if(request.getParameter("size2") != null)
//                size2 = Integer.parseInt(request.getParameter("size2"));
//            if(size2 > 0)
//                raf2.seek(size2);
//            byte b2[] = new byte[Long.valueOf(f2.length()).intValue() - size2];
//            System.out.println("Pending file Size..........." + b2.length);
//            System.out.println("Complete file Size..........." + f2.length());
//            raf2.readFully(b2);
//            byte b3[] = new byte[b1.length + b2.length];
//            System.arraycopy(b1,0,b3,0,b1.length);
//            System.arraycopy(b2,0,b3,b1.length,b2.length);
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//            response.addHeader("fileName", fileName);
//            response.addIntHeader("filesLeft",(fileNameLst.size() - 1));
//            response.addIntHeader("fileSize", b1.length);
//            response.addHeader("assoFileName", assoFileNameMap.get(fileName));
//            response.addIntHeader("assoFileSize",b2.length);
//            response.addHeader("assoFileType",assoFileTypeMap.get(fileName));
//            response.addHeader("fromDate",fromToDateMap.get(fileName));
//            response.addIntHeader("version",versionMap.get(fileName));
//            response.getOutputStream().write(b3);
//            response.getOutputStream().flush();
//            response.getOutputStream().close();
//          Utility.finishjdbcconnection();
        } catch (Exception e) {
          
            e.printStackTrace();
        }
        finally{
            if(raf1 != null )
                raf1.close();
            if(raf2 != null )
                raf2.close();
            
        }

    }

    public DownloadImageServlet() {
    }
}