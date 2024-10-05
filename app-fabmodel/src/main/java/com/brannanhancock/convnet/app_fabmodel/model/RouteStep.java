package com.brannanhancock.convnet.app_fabmodel.model;

import java.util.List;

public record RouteStep (int id, List<MachineWithConfigurationRequirements> possibleMachinesWithGivenConfiguration, List<RouteStackConstraint> routeStackConstraints) {}
