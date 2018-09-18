package virtualpete.teamcity.plugins.tcAgentAuthorise;

import java.util.*;

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.SBuildAgent;
import jetbrains.buildServer.serverSide.SBuildServer;
import org.jetbrains.annotations.NotNull;

/*
 * you might want some agents to be automatically authorised and this plugin will do that
 * 
 * the agent needs to be configured with a property to be acted upon.
 * 
 * This early version doesnt check who it is so it breaks the security model of teamcity - later version will
 * have an authorisation token mechanism or something you can at least keep secret but for now if you install
 * this code you'd be wanting to make sure its in appropriately secured networks. ie, not on the internet.
 * 
 * If parameter present it will autoauthorise into default pool
 * If parameter value is 'static' then it will not automatically deauthorise - so you can have a long running agent that
 * auto authorises and can come and go without destroying history - good for laptops or systems on a schedule.
 * 
 * eg:
 * 
 * agent_auto_authorise=true
 * 
 * or
 * 
 * agent_auto_authorise=static
 * 
 * 
 * Just deauthorise:
 * 
 * If parameter present then an agent can be deauthorised automnatically without allowing it to be auto-authorised - this 
 * case can be good if you still want to authorise agents yourself but tire of having to tidy up old ones.
 * 
 * eg:
 * 
 * agent_auto_undiscover=true
 * 
 * and no - i have not yet figured out how to make it join a specific pool - this is possible, just not easy to figure out 
 * from the doco.
 */


public class AutoAuthoriseListener extends BuildServerAdapter {

  private final SBuildServer myBuildServer;

  public AutoAuthoriseListener(SBuildServer sBuildServer) {
    myBuildServer = sBuildServer;
  }

  public void register() {
    myBuildServer.addListener(this);
  }

  @Override
  public void agentRegistered(@NotNull SBuildAgent sBuildAgent, long l) {
    Loggers.AGENT.debug("AutoAuthoriseListener has noticed an Agent registration:" + sBuildAgent.getHostName());

    Map<String, String> parameters = sBuildAgent.getAvailableParameters();

    boolean unReady = (sBuildAgent.isOutdated() || sBuildAgent.isUpgrading());
    boolean autoAuth = ( parameters.get("agent_auto_authorise").equals("static") || parameters.get("agent_auto_authorise").equals("true") );
    boolean notAuthorised = !sBuildAgent.isAuthorized();

    if (!unReady && notAuthorised && autoAuth) {
      sBuildAgent.setAuthorized(true, null, "autoAuthorise agent registered");
      Loggers.AGENT.info("AutoAuthoriseListener has set to authorised: " + sBuildAgent.getHostName());
    }
  }

  @Override
  public void agentUnregistered(@NotNull SBuildAgent sBuildAgent) {
    
    Loggers.AGENT.debug("AutoAuthoriseListener has noticed an Agent deregistration:" + sBuildAgent.getHostName());
    Map<String,String> parameters = sBuildAgent.getAvailableParameters();

    if(sBuildAgent.isAuthorized()) {
      if (parameters.get("agent_auto_undiscover").equals("true")) {
        sBuildAgent.setAuthorized(false, null, "autoAuthorise agent auto forgotten");
        Loggers.AGENT.info("AutoAuthoriseListener has auto undiscovered: " + sBuildAgent.getHostName());
      }
      if (parameters.containsKey("agent_auto_authorise") && !parameters.get("agent_auto_authorise").equals("static")) {
        sBuildAgent.setAuthorized(false, null, "autoAuthorise agent has unregistered and will be deAuthorised");
        Loggers.AGENT.info("AutoAuthoriseListener has auto unregistered: " + sBuildAgent.getHostName());
      }
    }
  }
}
