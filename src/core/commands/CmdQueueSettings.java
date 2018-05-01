package core.commands;

import core.Constants;
import core.entities.Queue;
import core.entities.QueueManager;
import core.entities.Server;
import core.entities.Setting;
import core.exceptions.BadArgumentsException;
import core.exceptions.InvalidUseException;
import core.util.Utils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class CmdQueueSettings extends Command{
	public CmdQueueSettings(){
		this.name = Constants.QUEUESETTINGS_NAME;
		this.helpMsg = Constants.QUEUESETTINGS_HELP;
		this.description = Constants.QUEUESETTINGS_DESC;
		this.adminRequired = true;
	}
	
	@Override
	public Message execCommand(Server server, Member member, String[] args) {
		if(args.length == 1){
			String s = "";
			Queue queue = getQueue(server.getQueueManager(), args[0]);
			for(Setting setting : queue.settings.getSettingsList()){
				s += String.format("%s = %s", setting.getName(), setting.getValue().toString());
				
				if(setting.getDescriptor() != null){
					s += String.format(" %s%n", setting.getDescriptor());
				}else{
					s += "\n";
				}
			}
			
			response = Utils.createMessage(String.format("Queue '%s' settings", queue.getName()), s, true);
			
		}else if(args.length == 2){
			Queue queue = getQueue(server.getQueueManager(), args[0]);
			Setting setting = queue.settings.getSetting(args[1]);
			String s = String.format("%s = %s", setting.getName(), setting.getValue().toString());
			
			if(setting.getDescriptor() != null){
				s += String.format(" %s%n", setting.getDescriptor());
			}else{
				s += "\n";
			}
			
			s += setting.getDescription();
			
			response = Utils.createMessage(String.format("Queue '%s' settings", queue.getName()), s, true);
			
		}else if(args.length == 3){
			Queue queue = getQueue(server.getQueueManager(), args[0]);
			queue.settings.set(args[1], args[2]);
			
			response = Utils.createMessage(String.format("Queue '%s' settings", queue.getName()), "Queue setting updated", true);
		}else{
			throw new BadArgumentsException();
		}
		
		System.out.println(success());
		return response;
	}
	
	private Queue getQueue(QueueManager qm, String arg){
		Queue queue;
		try{
			queue = qm.getQueue(Integer.valueOf(arg));
		}catch(NumberFormatException ex){
			queue = qm.getQueue(arg);
		}
		
		if(queue != null){
			return queue;
		}else{
			throw new InvalidUseException("Queue does not exist");
		}
	}
}