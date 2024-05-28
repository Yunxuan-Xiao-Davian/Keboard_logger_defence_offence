package ProcessDetector;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessDetector{
    ArrayList<ProcessData> data = new ArrayList<ProcessData>();
    int processId;
    String processName;
    String processPath;
	
	private Process execCommand(String command){
		Process process;
		try{
            process = Runtime.getRuntime().exec(command);
            return process;
		}
		catch(Exception e){
            System.out.println(e);
            return null;
		}
	}


    public ArrayList<ProcessData> scanProcesses(){
        String scan = "";
        int pid= 0;
        float cpuThreshold = 5; // 设定CPU利用率阈值，根据实际需要调整

        try {
            List<String> command = new ArrayList<>();
            command.add("powershell.exe");
            command.add("-Command");
            command.add("Get-Process | Select-Object Id, CPU | Where-Object {$_.CPU -gt " + cpuThreshold + "}");

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(System.getProperty("user.home")));
            pb.redirectErrorStream(true);
            Process p = pb.start();

            Scanner sc = new Scanner(p.getInputStream());
            sc.nextLine();
            sc.nextLine();
            sc.nextLine();
            while (sc.hasNext()){
                scan=sc.nextLine();
                String temp = scan.substring(0,5);
                processId = Integer.parseInt(temp.trim());
                processName = processName(processId);
                processPath = processPath(processId);
                if(!processPath.equals("INVALID") && !pathExists(processPath)){
                    data.add(new ProcessData(processId, processName, processPath));
                }
            }
            p.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this.data;
    }


    String processName(int id){
        int line = 0;
        String processName;
		Process cmd = execCommand("cmd /c tasklist /FI \"PID eq " + id + "\"");
        try{
            Scanner sc = new Scanner(cmd.getInputStream());
            while(sc.hasNext()){
                if(line == 3){
                    processName = sc.nextLine();
                    processName = processName.substring(0, processName.indexOf(' '));
                    return processName;
                }
                sc.nextLine();
                line++;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return "INVALID";
    }

    String processPath(int id){
        int line = 0;
        String processPath;
		Process cmd = execCommand("cmd /c wmic process where \"ProcessID=" + id + "\" get ExecutablePath");
        try{
            Scanner sc = new Scanner(cmd.getInputStream());
            while(sc.hasNext()){
                if(line == 2){
                    processPath = sc.nextLine();
                    return processPath;
                }
                sc.nextLine();
                line++;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return "INVALID";
    }

    Boolean pathExists(String path){
        for(int i=0; i<data.size(); i++){
            if(data.get(i).processPath.equals(path))
                return true;
        }
        return false;
    }
    public String traceSource(int PID){
        String ip = "";
        while(true){
            Process cmd = execCommand("cmd /c netstat -ano |findstr  \"" + PID + "\"");
            try{
                Scanner sc = new Scanner(cmd.getInputStream());
                if (sc.hasNextLine()){
                    System.out.println(ip=sc.nextLine());
                }
                //  ip = sc.nextLine().substring(30,55).trim();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(!ip.equals("")){
                break;
            }
        }
        return ip;
    }
}