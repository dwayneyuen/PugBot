package core.commands;

import core.Constants;
import core.entities.Game;
import core.entities.QueueManager;
import core.entities.Server;
import core.exceptions.BadArgumentsException;
import core.exceptions.InvalidUseException;
import core.util.Utils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class CmdSub extends Command {

	public CmdSub(Server server) {
		this.helpMsg = Constants.SUB_HELP;
		this.description = Constants.SUB_DESC;
		this.name = Constants.SUB_NAME;
		this.pugChannelOnlyCommand = true;
		this.server = server;
	}

	@Override
	public Message execCommand(Member caller, String[] args) {
		QueueManager qm = server.getQueueManager();

		if (args.length > 2 || args.length == 0) {
			throw new BadArgumentsException();
		}

		Member target = server.getMember(args[0]);
		Member substitute;

		if (args.length == 1) {
			substitute = caller;
		} else {
			if(!server.isAdmin(caller)){
				throw new InvalidUseException("Admin required to force-substitute a player");
			}
			
			substitute = server.getMember(args[1]);
		}

		if (qm.isPlayerIngame(substitute.getUser())) {
			throw new InvalidUseException(substitute.getEffectiveName() + " is already in-game");
		}

		if (!qm.isPlayerIngame(target.getUser())) {
			throw new InvalidUseException(target.getEffectiveName() + " is not in-game");
		}

		Game game = qm.getPlayersGame(target.getUser());

		game.sub(target.getUser(), substitute.getUser());
		qm.purgeQueue(substitute.getUser());

		this.response = Utils.createMessage(String.format("`%s has been substituted with %s`",
				target.getEffectiveName(), substitute.getEffectiveName()));

		qm.updateTopic();

		return response;
	}
}
