package com.brannanhancock.convnet.app_fabmodel;

import com.brannanhancock.convnet.app_fabmodel.model.Fab;
import com.brannanhancock.convnet.app_fabmodel.model.Route;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


@Controller
public class Query {

    @QueryMapping
    public Fab fab() {
        return new Fab(Route.getRoutes());
    }
}
