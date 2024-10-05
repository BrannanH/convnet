package com.brannanhancock.convnet.app_fabmodel.model;

import java.util.Deque;

public interface RouteStackConstraint {

    String where();

    boolean isMachineWithConfigurationPlausibleNextStep(MachineWithConfigurationRequirements possibleNextStep, Deque<MachineWithConfigurationRequirements> routeStack);
}
