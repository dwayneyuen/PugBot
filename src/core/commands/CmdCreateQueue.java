package core.commands;

import core.Constants;
import core.entities.QueueManager;
import core.entities.Server;
import core.exceptions.BadArgumentsException;
import core.util.Utils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class CmdCreateQueue extends Command {

	public CmdCreateQueue() {
		this.helpMsg = Constants.CREATEQUEUE_HELP;
		this.description = Constants.CREATEQUEUE_DESC;
		this.name = Constants.CREATEQUEUE_NAME;
		this.adminRequired = true;
		this.pugChannelOnlyCommand = true;
	}

	@Override
	public Message execCommand(Server server, Member member, String[] args) {
		QueueManager qm = server.getQueueManager();
		if (args.length == 2) {
			try {
				qm.createQueue(args[0], Integer.valueOf(args[1]));
			} catch (NumberFormatException ex) {
				throw new BadArgumentsException("Max players must be an integer value");
			}
		} else {
			throw new BadArgumentsException();
		}
		qm.updateTopic();
		this.response = Utils.createMessage(String.format("Queue %s created", args[0]), qm.getHeader(), true);
		System.out.println(success());
		
		return response;
	}
}
