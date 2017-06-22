package serviceImpl;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import service.IOService;

public class IOServiceImpl implements IOService{
	
	@Override
	public boolean writeFile(String file, String userId, String fileName, String fileType) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("@yyyy_MM_dd_kk_mm_ss");
        String time = dateFormat.format(now);
		File f;
        String parentPath  = "./BFServer/"+ userId;
		String filePath = parentPath + "/" + fileName+time+ "." + fileType;
		try {
		    f = new File(parentPath);
		    if (!f.exists())
		        f.mkdir();
		    f = new File(filePath);
		    if (!f.exists())
		        f.createNewFile();
			FileWriter fw = new FileWriter(f, false);
			fw.write(file);
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String readFile(String userId, String fileName, String version, String fileType) {
		// TODO Auto-generated method stub
        String code = "";
        File file = new File("./BFServer/"+userId+"/"+fileName+"@"+version+"."+fileType);
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null){
                code += line;
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
		return code;
	}

	@Override
	public String readFileList(String userId) {
		// TODO Auto-generated method stub
        File file = new File("./BFServer/"+userId);
        String[] filenameList = file.list();
		return String.join(",",filenameList);
	}

	
}
