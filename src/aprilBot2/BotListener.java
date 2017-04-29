package aprilBot2;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {
	
	 
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
    	
    	
        if (e.getMessage().getRawContent().equalsIgnoreCase("ping")) {
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + "http://i.imgur.com/OYFzoGP.png").queue();
        }
    }
 
}