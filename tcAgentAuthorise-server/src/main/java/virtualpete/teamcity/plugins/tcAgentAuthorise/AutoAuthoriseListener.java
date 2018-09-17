package virtualpete.teamcity.plugins.tcAgentAuthorise;

import java.util.*;

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.SBuildAgent;
import jetbrains.buildServer.serverSide.SBuildServer;
import org.jetbrains.annotations.NotNull;

/*
 * you might want some agents to be automatically authorised and this plugin will do that
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
    Map<String,String> parameters = sBuildAgent.getAvailableParameters();
    Loggers.SERVER.info("AutoAuthoriseListener has noticed an Agent Registration");
    if (parameters.containsKey("auto_authorise_pool")) {
      sBuildAgent.setAuthorized(true, null, "autoAuthorise agent registered");
      Loggers.SERVER.info("AutoAuthoriseListener has set to authorised: " + sBuildAgent.getHostName());
    }
  }

  @Override
  public void agentUnregistered(@NotNull SBuildAgent sBuildAgent) {
    Map<String,String> parameters = sBuildAgent.getAvailableParameters();
    Loggers.SERVER.info("AutoAuthoriseListener has noticed an Agent Deregistration");
    if (parameters.containsKey("auto_unDiscover")) {
      sBuildAgent.setAuthorized(false, null, "autoAuthorise agent auto forgotten");
      Loggers.SERVER.info("AutoAuthoriseListener has auto undiscovered: " + sBuildAgent.getHostName());
    }
    if (parameters.containsKey("auto_deAuthorise") && parameters.get("auto_deAuthorise").equals("true")) {
      sBuildAgent.setAuthorized(false, null, "autoAuthorise agent has unregistered so will be deAuthorised");
      Loggers.SERVER.info("AutoAuthoriseListener has auto unregistered: " + sBuildAgent.getHostName());
    }
  }
}
