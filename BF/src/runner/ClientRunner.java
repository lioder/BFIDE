package runner;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javafx.application.Application;
import javafx.stage.Stage;
import rmi.RemoteHelper;
import service.IOService;
import ui.Login;
import ui.MainFrame;

public class ClientRunner extends Application{
	private RemoteHelper remoteHelper;
	private void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://127.0.0.1:8000/DataRemoteObject"));
			System.out.println("linked");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void start(Stage priamryStage) {
        linkToServer();
        Login login = new Login();
    }
	public static void main(String[] args){ launch(args);}
}
