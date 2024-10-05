package com.brannanhancock.convnet.app_fabmodel.model;


import com.brannanhancock.convnet.app_fabmodel.model.machineconfiguration.MachineConfiguration;

import java.util.List;

public record MachineWithConfigurationRequirements(Machine machine, List<MachineConfiguration> configurationRequirements) {
}
