package com.brannanhancock.convnet.app_fabmodel.model;

import com.brannanhancock.convnet.app_fabmodel.model.machineconfiguration.MachineConfiguration;

import java.util.List;

public record Machine (
    int id,
    String machineType,
    double loadTime,
    double unloadTime,
    List<LoadPort> ports,
    List<Chamber> chambers,
    List<PortToChamberMapping> portToChamberMappings,
    List<MachineConfiguration> possibleMachineConfigurations) {
}
