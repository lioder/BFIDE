package rmi;

import service.ExecuteService;
import service.IOService;
import service.UserService;
import serviceImpl.ExecuteImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService,ExecuteService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private IOService iOService;
	private UserService userService;
	private ExecuteService executeService;

	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeService = new ExecuteImpl();
	}

	@Override
	public boolean writeFile(String file, String userId, String fileName, String fileType) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.writeFile(file, userId, fileName, fileType);
	}

	@Override
	public String readFile(String userId, String fileName,String version, String fileType) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFile(userId, fileName, version, fileType);
	}

	@Override
	public String readFileList(String userId) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFileList(userId);
	}

	@Override
	public String login(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.logout(username);
	}
	@Override
	public boolean register(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.register(username,password);
	}

    @Override
    public String bfExecute(String code,String param) throws RemoteException {
        // TODO Auto-generated method stub
        return executeService.bfExecute(code,param);
    }
    @Override
    public String ookExecute(String code,String param) throws RemoteException {
        // TODO Auto-generated method stub
        return executeService.ookExecute(code,param);
    }
}
