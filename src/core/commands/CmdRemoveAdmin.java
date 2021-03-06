package core.commands;

import core.exceptions.BadArgumentsException;
import core.exceptions.InvalidUseException;
import core.util.Utils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class CmdRemoveAdmin extends Command {

	@Override
	public Message execCommand(Member caller, String[] args) {
		if (args.length != 1) {
			throw new BadArgumentsException();
		}

		Member m = server.getMember(args[0]);

		if (server.isAdmin(m)) {
			server.removeAdmin(m.getUser().getIdLong());
		} else {
			throw new InvalidUseException(m.getEffectiveName() + " is not an admin");
		}

		return Utils.createMessage(String.format("`%s's admin removed`", m.getEffectiveName()));
	}

	@Override
	public boolean isAdminRequired() {
		return true;
	}

	@Override
	public boolean isGlobalCommand() {
		return true;
	}

	@Override
	public String getName() {
		return "RemoveAdmin";
	}

	@Override
	public String getDescription() {
		return "Removes a user's bot admin privileges";
	}

	@Override
	public String getHelp() {
		return getBaseCommand() + " <username>";
	}

}
