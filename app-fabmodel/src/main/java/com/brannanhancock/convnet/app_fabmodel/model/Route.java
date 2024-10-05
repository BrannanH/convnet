package com.brannanhancock.convnet.app_fabmodel.model;

import com.brannanhancock.convnet.app_fabmodel.model.machineconfiguration.MachineConfiguration;

import java.util.Arrays;
import java.util.List;

public record Route (int id, List<List<RouteStep>> possibleSteps) {

    public static List<Route> getRoutes() {
        MachineConfiguration noSpecialConfig = new MachineConfiguration();
        Machine photoMachine = new Machine(1, "PHOTO", 12.0, 15.0, Arrays.asList(new LoadPort(1)), Arrays.asList(new Chamber(1)), Arrays.asList(new PortToChamberMapping(1, 1)), Arrays.asList(noSpecialConfig));
        MachineWithConfigurationRequirements config1 = new MachineWithConfigurationRequirements(photoMachine, Arrays.asList(noSpecialConfig));

        Machine etchMachine1 = new Machine(2, "ETCH", 12.0, 15.0, Arrays.asList(new LoadPort(1)), Arrays.asList(new Chamber(1)), Arrays.asList(new PortToChamberMapping(1, 1)), Arrays.asList(noSpecialConfig));
        MachineWithConfigurationRequirements etchMachineConfig1 = new MachineWithConfigurationRequirements(etchMachine1, Arrays.asList(noSpecialConfig));

        Machine etchMachine2 = new Machine(3, "ETCH", 12.0, 15.0, Arrays.asList(new LoadPort(1)), Arrays.asList(new Chamber(1)), Arrays.asList(new PortToChamberMapping(1, 1)), Arrays.asList(noSpecialConfig));
        MachineWithConfigurationRequirements etchMachineConfig2 = new MachineWithConfigurationRequirements(etchMachine2, Arrays.asList(noSpecialConfig));

        RouteStep photoStep = new RouteStep(1, List.of(config1), List.of());
        RouteStep cheapEtchStep = new RouteStep(2, Arrays.asList(etchMachineConfig1, etchMachineConfig2), List.of());
        RouteStep stackingEtchStep = new RouteStep(3, Arrays.asList(etchMachineConfig1, etchMachineConfig2), List.of(new StackingRouteStackConstraint()));

        return Arrays.asList(
                new Route(1, Arrays.asList(
                        List.of(photoStep),
                        List.of(cheapEtchStep, photoStep),
                        List.of(photoStep),
                        List.of(cheapEtchStep))),
                new Route(2, Arrays.asList(
                        List.of(photoStep),
                        List.of(cheapEtchStep),
                        List.of(photoStep),
                        List.of(stackingEtchStep)
                )));
    }
}
