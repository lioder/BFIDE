package serviceImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

import javafx.scene.control.Alert;
import org.json.*;
import service.UserService;

public class UserServiceImpl implements UserService{

	@Override
	public String login(String username, String password) throws RemoteException {
        String jsonText = readJson();
        JSONArray jsonArray = null;
        String name = "";
        String password1 = "";
        try {
            jsonArray = new JSONArray(jsonText);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jo = (JSONObject)jsonArray.get(i);
                name = jo.getString("name");
                password1 = jo.getString("password");
                if (name.equals(username) && password.equals(password1))
                    return "success";
                else if (name.equals(username) && !password.equals(password1))
                    return "password Wrong";
                else
                    return "fail";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return "fail";
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return true;
	}

	@Override
	public boolean register(String username,String password) throws RemoteException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = null;
        try {
            jsonObject.put("name",username);
            jsonObject.put("password",password);
            String jsonContent = readJson();
            if (jsonContent.equals("")){
                jsonContent = "[]";
            }
            jsonArray = new JSONArray(jsonContent);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jo = (JSONObject)jsonArray.get(i);
                if (jo.get("name").equals(username)) {
                    return false;
                }
            }
            jsonArray.put(jsonArray.length(),jsonObject);
            return writeJson(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
	}


	private String readJson() {
        String jsonText = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("./BFserver/UserData.json"));
            String temp = "";
            while ((temp = reader.readLine()) != null)
                jsonText += temp;
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return jsonText;
    }

    private Boolean writeJson(JSONArray ja){
        try {
            FileWriter writer = new FileWriter(System.getProperty("user.dir")+"/BFserver/UserData.json",false);
            writer.write(ja.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
